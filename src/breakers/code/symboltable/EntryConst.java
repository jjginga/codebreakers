package breakers.code.symboltable;

public class EntryConst extends EntryTable {
    public String type;
    public String value;
    public int dimension;

    public EntryConst(String name, String type, String value, int dimension, SymbolTable symbolTable) {
        super(name);
        this.name = name;
        this.type = type;
        this.dimension = dimension;
        this.value = value;
    }
}

