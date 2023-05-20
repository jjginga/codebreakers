package breakers.code.symboltable;

public class EntryVar extends EntryTable {
    public EntryTable type; // Tipo da variável
    public int varDimension; // Dimensão da variável // 0 = escalar, 1 = vetor, 2 = matriz

    public EntryVar (String name, EntryTable type, int varDimension) {
        this.name = name;
        this.type = type;
        this.varDimension = varDimension;
    }
}
