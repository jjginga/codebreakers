package breakers.code.symboltable;

import java.util.List;

public class EntryGlobal extends EntryTable {
    public List<EntryVar> globalVars; // Lista de variáveis globais

    public EntryGlobal () {
        super("global");
        this.globalVars = List.of();
    }


}
