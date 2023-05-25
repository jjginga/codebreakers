package breakers.code.symboltable;

public class SymbolTable {
    // Primeira tabela de símbolos
    public EntryTable root;

    // Escopo atual
    public int currentScope;


    public SymbolTable() {
        root = null;
        currentScope = 0;
    }

    /**
     * Adiciona uma nova entrada na tabela de símbolos
     * A nova entrada será adicionada no topo da tabela (root)
     * @param newTable Nova entrada a ser adicionada
     */
    public void addTable(EntryTable newTable) {
        newTable.next = root;
        root = newTable;
        newTable.symbolTable = this;
        currentScope++;
    }

    public void beginScope() {
        currentScope++;
    }

    public void endScope() {
        while (root != null && root.scope == currentScope) {
            root = root.next;
        }
        currentScope--;
    }

    public EntryConst getConstEntry() {
        EntryTable current = this.root;
        while (current.next != null) {
            if(current instanceof EntryConst)
                return (EntryConst) current;
            current = current.next;
        }
        return null;
    }
}
