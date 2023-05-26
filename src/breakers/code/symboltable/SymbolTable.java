package breakers.code.symboltable;

import java.util.Hashtable;

public class SymbolTable {
    public SymbolTable prev;

    // Escopo atual
    public int currentScope;

    // Id e entrada da tabela
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


    public void beginScope() {
        currentScope++;
    }

//    public void endScope() {
//        while (root != null && root.scope == currentScope) {
//            root = root.next;
//        }
//        currentScope--;
//    }

//    public EntryConst getConstEntry() {
//        EntryTable current = this.root;
//        while (current.next != null) {
//            if(current instanceof EntryConst)
//                return (EntryConst) current;
//            current = current.next;
//        }
//        return null;
//    }
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
