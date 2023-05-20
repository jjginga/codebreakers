package breakers.code.symboltable;

import java.util.List;

public class EntryGlobal extends EntryTable {
    public List<EntryVar> globalVars; // Lista de variáveis globais

    public EntryGlobal () {
        this.globalVars = List.of();
    }

    /**
     * Adiciona uma nova variável global na lista de variáveis globais
     * @param globalVar Nova variável global a ser adicionada
     */
    public void addGlobalVar (EntryVar globalVar) {
        globalVar.symbolTable = this.symbolTable.root.symbolTable;
        globalVars.add(globalVar);
    }
}
