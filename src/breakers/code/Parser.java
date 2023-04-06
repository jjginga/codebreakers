package breakers.code;

import breakers.code.grammar.tokens.BASIC_VAR;
import breakers.code.grammar.tokens.Token;

import java.util.*;

public class Parser {
    private String currentRead = "";
    private String fullString;
    private List<Map<Token, String>> stringTokenMapList;
    private Integer index = 0;

    public Parser() {
        this.fullString = "";
        this.stringTokenMapList = new ArrayList<>();
    }

    public List<Map<Token, String>> tokenize(String string) {
        this.fullString = string;

        while (index < fullString.length()) {
            currentRead += fullString.charAt(index);

            // If basic var token is the last entry on the list then check if the next one is a valid variable name
            if(lastEntryIsBasicType(stringTokenMapList)) {
                String substring = this.fullString.substring(index);
                // VALIDATE VARIABLE NAME HERE -> Call getVariableAfterVarDeclaration with substring starting on the current index
            };

            // Check if the current read is a basic type --> INT / FLOAT / BOOL
            BASIC_VAR basicVar = BASIC_VAR.stream().filter(item -> currentRead.equals(item.getType())).findFirst().orElse(null);

            if(basicVar != null) {
                Token token = Token.BASIC_TYPE;

                // Add found token
                Map<Token, String> tokenMap = new LinkedHashMap<>();
                tokenMap.put(token, currentRead);

                stringTokenMapList.add(tokenMap);

                //Reset currentRead
                currentRead = "";
            }

            index++;
        }

        return stringTokenMapList;
    }

    private String getVariableAfterVarDeclaration(String string) {
        String variableName = "";
        // If the last token was a variable declaration then the next one should be the variable name
        // Find the = value and get the variable name
        int firstIndexEqualsChar = string.indexOf("=");

        // It means that there's a "=" on the rest of the string
        if(firstIndexEqualsChar != -1 ) {
            // validate if it's a valid variable name
            variableName = string.substring(0, firstIndexEqualsChar - 1);

            // Remove spaces in between to check variable name
            variableName = variableName.trim();

            // #TODO -> is this substring a correct variable name ?
        } else {
            throw new RuntimeException("Syntax error");
        }

        return variableName;
    }


    private boolean lastEntryIsBasicType (List<Map<Token, String>> list) {
         return list.get(list.size() - 1).containsKey(Token.BASIC_TYPE);
    }
}