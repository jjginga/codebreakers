package breakers.code.symboltable;

import java.util.List;

public class EntryFunction extends EntryTable {
    public EntryVar returnType; // Tipo de retorno da função -- entryVar tem de ser escalar --> (varDim = 0)
    public List<EntryVar> params; // Parâmetros da função
    public String name; // Nome da função


    public EntryFunction (String name, EntryVar returnType) {
        this.name = name;
        this.returnType = returnType;
        this.params = List.of();
    }

    /**
     * Adiciona um novo parâmetro na lista de parâmetros
     * @param param Novo parâmetro a ser adicionado
     */
    public void addParam (EntryVar param) {
        params.add(param);
    }
}
