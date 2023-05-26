package breakers.code.symboltable;

import breakers.code.analysis.syntatic.Node;

import java.util.LinkedList;

public class SymbolTableFiller {
    Node syntaxTreeRoot;
    SymbolTable symbolTable;

    LinkedList<String> errors = new LinkedList<String>();


    /**
     * Receives the root node of the syntax tree and fills the symbol table
     * @param node
     */
    public SymbolTableFiller(Node node, SymbolTable symbolTable) {
        this.syntaxTreeRoot = node;
        this.symbolTable = symbolTable;
    }

    public void fillSymbolTable() {
        Node currentTreeNode = syntaxTreeRoot;

        while(currentTreeNode != null) {
           checkAndInsertConstEntry(currentTreeNode);

            if(currentTreeNode.getChildren().size() > 0) {
                currentTreeNode = currentTreeNode.getChildren().get(0);
            } else {
                return;
            }

        }
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

}
