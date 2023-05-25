package breakers.code.symboltable;

import breakers.code.analysis.syntatic.Node;

public class SymbolTableFiller {
    Node syntaxTreeRoot;
    SymbolTable symbolTable;


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

            currentTreeNode = currentTreeNode.getChildren().get(0);
        }
    }
    private void checkAndInsertConstEntry(Node node) {
       if (node.getToken() != null && node.getToken().getKey() != null && node.getToken().getKey().getData().equals("const")) {
           String name = node.getChildren().get(0).getToken().getValue();

           String type = node.getChildren().get(0).getChildren().get(0).getToken().getValue();
           String value = node.getChildren().get(0).getChildren().get(1).getToken().getValue();

           EntryConst entryConst = new EntryConst(name, type, value, 0, null, symbolTable);

           symbolTable.addTable(entryConst);
       }
    }
}
