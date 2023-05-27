package breakers.code.symboltable;

import breakers.code.analysis.syntatic.Node;
import breakers.code.grammar.tokens.lexems.identifiers.FUN_NAMES;

import java.util.LinkedList;
import java.util.List;

import static breakers.code.grammar.tokens.lexems.NEED_FOR_PARSING.ASSIGNMENT;
import static breakers.code.grammar.tokens.lexems.RESERVED_WORDS.*;

public class SymbolTableFiller {
    Node syntaxTreeRoot;
    SymbolTable symbolTable;

    LinkedList<String> errors = new LinkedList<String>();

    List<List<Node>> childrenInScopes = new LinkedList<>();

    int currentScopeLevel = -1;

    String currentScopeName = "";

    private final LinkedList<String> newScopeCreators = new LinkedList<>() {{
        add(FUN_NAMES.FUN_NAME.getData());
        add(IF.getData());
        add(ELSE.getData());
        add(WHILE.getData());
        add(FOR.getData());
    }};


    /**
     * Receives the root node of the syntax tree and fills the symbol table
     * @param node
     */
    public SymbolTableFiller(Node node, SymbolTable symbolTable) {
        this.syntaxTreeRoot = node;
        this.symbolTable = symbolTable;
    }

    public void fillSymbolTable() {
        Node currentNode = syntaxTreeRoot;

        traverseTree(currentNode);
    }


    private void traverseTree(Node node) {
        currentScopeLevel = symbolTable.currentScope;
        if (node == null)
            return;

        checkAndInsertConstEntry(node);
        checkAndInsertVarEntry(node);

        // When the scope has ended go back to the previous symbol table #TODO
        if(childrenInScopes.size() > 0 && childrenInScopes.size() > currentScopeLevel && childrenInScopes.get(currentScopeLevel).isEmpty() && symbolTable.prev != null) {
            symbolTable = symbolTable.prev;
            currentScopeLevel = symbolTable.currentScope;
        }
//
//        // Reduce if it is a direct child in the scope
        if(childrenInScopes.size() > 0  && childrenInScopes.size() > currentScopeLevel && childrenInScopes.get(currentScopeLevel).contains(node)){
            childrenInScopes.get(currentScopeLevel).remove(node);
        }

        if(
                isSymbolScopeCreator(
                    node.getToken() != null &&
                    node.getToken().getKey() != null  &&
                    node.getToken().getKey().getData() != null ?
                    node.getToken().getKey().getData() : null
                ) ||
                isFunctionReturnAssignment(node)
        ) {
            // If it is a function declaration then creates the entry inside the current symbol table to have the correct scope
            // And add a new symbol table to the current symbol table so as the variables inside the function
            // have the correct scope
            if(isFunction(node) && isFunctionDeclaration(node)) {
                insertFunVar(node);
                symbolTable = new SymbolTable(symbolTable);
                List<Node> children = new LinkedList<>();

                for (Node child : node.getChildren()) {
                    children.add(child);
                }

                childrenInScopes.add(children);
                currentScopeLevel++;
            } else if(isFunction(node) && isFunctionCall(node)) {
                // false alarm -- do not create new scope
                // check if arguments were already declared is superior scope
            } else if(isFunctionReturnAssignment(node)) { // TODO --> Error when it's assignment with operator (*=)
                // if it's value return change inside function
                // verify if function was declared in superior scope
                // if it was, then check if the return type is the same as the variable type
                // if it's not, then throw error
                System.out.println("Function return assignment" + node.toString());
            }
            else {
                symbolTable = new SymbolTable(symbolTable);
                currentScopeLevel++;
                List<Node> children = new LinkedList<>();

                for (Node child : node.getChildren()) {
                    children.add(child);
                }

                childrenInScopes.add(children);
            }
        }


        for (Node child : node.getChildren()) {
            traverseTree(child);
        }


//
//
//        for (Node child : currentNode.getChildren()) {
//            if(child.getToken() != null && child.getToken().getKey() != null && child.getToken().getKey().getData() != null) {
//                System.out.println(child.getToken().getKey().getData());
//            }
//        }
//
//
//        // If symbol is scope creator then create a new SymbolTable and point current to it
//        // Continue while flow to the next node (children)
//        if(
//                currentNode.getToken() != null &&
//                        currentNode.getToken().getKey() != null  &&
//                        currentNode.getToken().getKey().getData() != null &&
//                        isSymbolScopeCreator(
//                                currentNode.getToken().getKey().getData()
//                        )
//        ) {
//            symbolTable = new SymbolTable(symbolTable);
//            continue;
//        }
//
//
//        checkAndInsertConstEntry(currentNode);
//
//        if(currentNode.getChildren().size() > 0) {
//            currentNode = currentNode.getChildren().get(0);
//        } else {
//            // go to the next branch
//            currentDirectChildPosition++;
//            currentNode = syntaxTreeRoot;
//            currentNode.getChildren().get(currentDirectChildPosition);
//        }
    }

    private void checkAndInsertConstEntry(Node node) {
       if (node.getToken() != null && node.getToken().getKey() != null && node.getToken().getKey().getData().equals("const")) {
           String name = node.getChildren().get(0).getToken().getValue();

           // If it was already inserted, return because it's a duplicate (already declared)
           if (validateIfVariableWasAlreadyInserted(name) == true)
               return;


           String type = node.getChildren().get(0).getChildren().get(0).getToken().getValue();
           String value = node.getChildren().get(0).getChildren().get(1).getToken().getValue();

           EntryConst entryConst = new EntryConst(name, type, value, 0,  symbolTable);

           symbolTable.addTableEntry(entryConst);
       }
    }

    private void checkAndInsertVarEntry(Node node) {
        if (node.getToken() != null && node.getToken().getKey() != null && node.getToken().getKey().getData().equals("var")) {
            String name = node.getChildren().get(0).getToken().getValue();

            // If it was already inserted, return because it's a duplicate (already declared)
            if (validateIfVariableWasAlreadyInserted(name) == true)
                return;

            String type = node.getChildren().get(0).getChildren().get(0).getToken().getValue();
            int dimension = 0; //var has dimension 0 (scalar) --> other dimensions are assigned as vec

            EntryVar entryVar = new EntryVar(name, type, dimension);

            symbolTable.addTableEntry(entryVar);
        }
    }

    private void insertFunVar(Node node) {
        String returnType = node.getChildren().get(1).getToken().getValue();
        String name = node.getToken().getValue();
        int dimension = 0;

        // According to the YAIL language, function is also a var
        EntryVar entryVar = new EntryVar(name, returnType, dimension, true);
        symbolTable.addTableEntry(entryVar);
    }

    private boolean validateIfVariableWasAlreadyInserted(String name) {
        if(symbolTable.getSymbolTableEntry(name) != null) {
            errors.add("Variável " + name + " já declarada");
            System.err.println("Variável " + name + " já declarada");
            return true;
        }
        return false;
    }

    private boolean verifyVariableWasAlreadyAssigned(String name) {
        if(symbolTable.getSymbolTableEntry(name) == null) {
            errors.add("Variável " + name + " não declarada");
            System.err.println("Variável " + name + " não declarada");
            return true;
        }
        return false;
    }


    private boolean isSymbolScopeCreator(String name) {
        if(newScopeCreators.contains(name)) {
            return true;
        }
        return false;
    }

    private boolean isFunctionDeclaration(Node node) {
        if(
                node.getChildren().size() > 1 &&
                node.getChildren().get(1) != null && // return type
                node.getChildren().get(1).getToken() != null &&
                node.getChildren().get(1).getToken().getValue() != null

        ) {
            return true;
        }


        return false;
    }

    private boolean isFunctionCall(Node node) {
        if(node.getChildren().size() < 2) {
            return false;
        }

        if (node.getChildren().get(1) == null) { // return type
            return false;
        }

        return true;
    }


    private boolean isFunctionReturnAssignment(Node node) {
        if (
                node.getToken() != null &&
                node.getToken().getKey() != null &&
                node.getToken().getKey().equals(ASSIGNMENT) == true &&
                node.getChildren().size() > 1 &&
                node.getChildren().get(0) != null && // return type
                node.getChildren().get(0).getToken() != null &&
                isFunction(node.getChildren().get(0))
        ) {
            return true;
        }


        return false;
    }

    private boolean isFunction(Node node) {
        return node.getToken().getKey().getData().equals(FUN_NAMES.FUN_NAME.getData());
    }

    public SymbolTable getCurrentSymbolTable() {
        return symbolTable;
    }

    private boolean isEndOfScope(Node node) {
        if(node.getToken() != null && node.getToken().getKey() != null && node.getToken().getKey().getData().equals("end")) {
            return true;
        }
        return false;
    }
}
