package breakers.code.symboltable;

public class EntryTable { // Representa o símbolo que será inserido na tabela
    public String name; // Nome do símbolo
    public EntryTable next; // Próxima entrada na tabela
    public int scope; // Escopo da qual a tabela faz parte
    public SymbolTable symbolTable; // Tabela de símbolos do qual a tabela faz parte
}
