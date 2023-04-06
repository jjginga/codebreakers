package breakers.code;

import breakers.code.grammar.tokens.BASIC_SYMBOLS;
import breakers.code.grammar.tokens.BASIC_VAR;
import breakers.code.grammar.tokens.Token;

import java.util.*;

public class Parser {
    private String currentRead = "";
    private String fullString;
    //JOEL - isto é uma especie de "arvore"? Com uma operação na Linked Hash Map de cada nó?
    //IMO devia ser uma linked list porque queremos aceder ao ultimo elemento
    private LinkedList<Map<Token, String>> stringTokenMapList;

    private Integer index = 0;

    public Parser() {
        this.fullString = "";
        this.stringTokenMapList = new LinkedList<>();
    }

    public List<Map<Token, String>> tokenize(String string) {
        this.fullString = string;

        while (index < fullString.length()) {

            // Spaces and new lines are
            if(isToBeIgnored(fullString.charAt(index))) {
                index++;
                continue;
            }

            currentRead += fullString.charAt(index);

            //if is comment ignore the rest of line until it finds a new line
            /* # comentários começam com o símbolo # e vão até ao fim da linha */
            if (BASIC_SYMBOLS.COMMENT.getType().equals(currentRead)) {
                // Ignore the rest of the line
                while (index < fullString.length() && !isNewLine(fullString.charAt(index))){
                    index++;
                }

                currentRead = "";
                continue;
            }

            // Check if the current read is a basic symbol --> + - * / = ; ( ) { } [ ] , : < >
            // This isn't right, because it should know what the previous was (for instance, if it was a variable,
            // it should be either a = or a ;
            // TODO: If previous is a ; then next should be in new map
            BASIC_SYMBOLS basicSymbol = BASIC_SYMBOLS.stream()
                    .filter(item -> currentRead.equals(item.getType()))
                    .findFirst()
                    .orElse(null);

            if(basicSymbol != null) {
                Token token = Token.BASIC_SYMBOL;



                stringTokenMapList.peekLast().put(token, currentRead);
                System.out.println(currentRead);
                //Reset currentRead
                currentRead = "";
                continue;
            }

            if(checkLastEntryType(Token.BASIC_SYMBOL)){
                //if is a = then is should be a number
                String str = fullString.substring(index);
                str = getNumberAfterEquals(str);
                Token token = Token.NUMERIC;

                System.out.println(str);

                stringTokenMapList.peekLast().put(token, str);
            }



            // If basic var token is the last entry on the list then check if the next one is a valid variable name
            if(lastEntryIsBasicType()) {
                String str = fullString.substring(index);
                // VALIDATE VARIABLE NAME HERE -> Call getVariableAfterVarDeclaration with substring starting on the current index
                str = getVariableAfterVarDeclaration(str);
                Token token = Token.VARIABLE;
                System.out.println(str);
                stringTokenMapList.peekLast().put(token, str);
                currentRead = "";
                index++;
                continue;
            };



            // Check if the current read is a basic type --> INT / FLOAT / BOOL
            BASIC_VAR basicVar = BASIC_VAR.stream()
                    .filter(item -> currentRead.equals(item.getType()))
                    .findFirst()
                    .orElse(null);



            if(basicVar != null) {
                Token token = Token.BASIC_TYPE;

                // Add found token
                //JOEL - isto não devia ser um container ordenado para podermos ver qual foi a ultima operação
                Map<Token, String> tokenMap = new LinkedHashMap<>();
                tokenMap.put(token, currentRead);

                stringTokenMapList.add(tokenMap);
                System.out.println(currentRead);
                //Reset currentRead
                currentRead = "";
            }

            index++;
        }

        return stringTokenMapList;
    }

    private String getNumberAfterEquals(String str) {
        String numeric = "";

        //There are several cases that can end a number (space, ;, +, -, *, /, =, <, >, ), ])
        //We need to find the first one and get the number
        //we cannot use regex because we need the index
        int firstIndex = Arrays.stream(new int[]{str.indexOf(" "), str.indexOf(";"), str.indexOf("+"), str.indexOf("-"),
                str.indexOf("*"), str.indexOf("/"), str.indexOf("="), str.indexOf("<"), str.indexOf(">"),
                str.indexOf(")"), str.indexOf("]")}).min().orElse(-1);

        if(firstIndex != -1) {
            numeric = str.substring(0, firstIndex-1).trim();

            index = index + firstIndex;
        } else {
            throw new RuntimeException("Syntax error");
        }

        return numeric;
    }

    private String getVariableAfterVarDeclaration(String string) {
        String variableName = "";
        // If the last token was a variable declaration then the next one should be the variable name
        // Find the = or ; value and get the variable name
        int firstIndexEqualsChar = Math.min(string.indexOf("="), string.indexOf(";"));

        // It means that there's a "=" on the rest of the string
        if(firstIndexEqualsChar != -1 ) {
            index = index + firstIndexEqualsChar;
            variableName = string.substring(0, firstIndexEqualsChar - 1);

            // Remove spaces in between to check variable name
            // validate if it's a valid variable name
            variableName = variableName.trim();
            //starts with a letter and can contain letters, numbers and underscores
            if(!variableName.matches("[a-zA-Z][a-zA-Z0-9_]*"))
                throw new RuntimeException("Invalid variable name");

        } else {
            throw new RuntimeException("Syntax error");
        }

        return variableName;
    }


    private boolean lastEntryIsBasicType () {
         return checkLastEntryType(Token.BASIC_TYPE);
    }

    private boolean checkLastEntryType(Token token) {
        return !stringTokenMapList.isEmpty() && stringTokenMapList.peekLast().containsKey(token);
    }

    private boolean isNewLine(char c) {
        return !String.valueOf(c).matches(".");
    }

    private boolean isToBeIgnored(char c) {
        return String.valueOf(c).matches(" ") || isNewLine(c);
    }
}