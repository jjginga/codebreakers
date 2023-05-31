package breakers.code.symboltable;

import java.util.Hashtable;

public class SymbolTable {
    public SymbolTable prev;

    // Escopo atual
    public int currentScope;

    // Nome identificador do símbolo e entrada da tabela
    private Hashtable<String, EntryTable> table;


    public SymbolTable(SymbolTable previousSymbolTable) {
        table = new Hashtable<>();
        prev = previousSymbolTable;
        if(previousSymbolTable != null) {
            currentScope = previousSymbolTable.currentScope + 1;
        } else {
            currentScope = 0;
        }
    }



    public EntryTable getSymbolTableEntry(String name) {
        SymbolTable current = this;
        while (current.prev != null) {
            if(current.table.containsKey(name))
                return current.table.get(name);
            current = current.prev;
        }
        return null;
    }

    public void addTableEntry(EntryTable entry) {
        table.put(entry.name, entry);
    }

    public void printAllSymbolEntries() {
        SymbolTable current = this;
        do {
            System.out.println("Scope: " + current.currentScope);
            for (EntryTable entry : current.table.values()) {
                System.out.println("Name: " + entry.name);
            }
            current = current.prev;
        } while (current.prev != null);
    }
}
