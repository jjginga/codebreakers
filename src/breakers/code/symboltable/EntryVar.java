package breakers.code.symboltable;

public class EntryVar extends EntryTable {
    public String type; // Tipo da variável
    public int varDimension; // Dimensão da variável // 0 = escalar, 1 = vetor, 2 = matriz
    public boolean isFunctionType;

    public EntryVar (String name, String type, int varDimension, boolean isFunctionType) {
        super(name);
        this.type = type;
        this.varDimension = varDimension;
        this.isFunctionType = isFunctionType;
    }

    public EntryVar (String name, String type, int varDimension) {
        super(name);
        this.type = type;
        this.varDimension = varDimension;
        this.isFunctionType = false;
    }
}
