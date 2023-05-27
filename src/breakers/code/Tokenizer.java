package breakers.code;

import breakers.code.grammar.tokens.*;
import breakers.code.grammar.tokens.lexems.*;
import breakers.code.grammar.tokens.lexems.delimiters.COMPOSED_OPERATORS;
import breakers.code.grammar.tokens.lexems.delimiters.OPERATORS;
import breakers.code.grammar.tokens.lexems.delimiters.PARENTHESIS;
import breakers.code.grammar.tokens.lexems.delimiters.PONCTUATION;
import breakers.code.grammar.tokens.lexems.literals.BOOL_VAR;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static breakers.code.grammar.tokens.lexems.GENERAL_SCHEMA.CONST;
import static breakers.code.grammar.tokens.lexems.delimiters.PARENTHESIS.*;
import static breakers.code.grammar.tokens.lexems.identifiers.CON_NAMES.CON_NAME;
import static breakers.code.grammar.tokens.lexems.identifiers.FUN_NAMES.FUN_NAME;
import static breakers.code.grammar.tokens.lexems.identifiers.VAR_NAMES.VAR_NAME;
import static breakers.code.grammar.tokens.lexems.identifiers.VEC_NAMES.VEC_NAME;
import static breakers.code.grammar.tokens.lexems.literals.NUMBERS.NUMBER;
import static breakers.code.grammar.tokens.lexems.literals.STRING.TEXT;


/*This class gets the source code and splits it into tokens
* it reads the sourceCode char by char and splits it into tokens by a delimiter
* the delimeters are: space , + , - , * , / , . , ; , ( , ), [ , ] , { , } */
public class Tokenizer {

    /* The argument, a string with the source code */
    private final String sourceCode;
    /*The result - a list of lists with the tokens */
    private List<List<Token>> lines;

    private String delimiters = Stream.of(
            OPERATORS.dataStream(),
            PARENTHESIS.dataStream(),
            PONCTUATION.dataStream()
            ).flatMap(s->s).collect(Collectors.joining());
    private String statmentTerminator = STATEMENT_TERMINATOR.dataStream().collect(Collectors.joining());
    private String commentDelimiter = COMMENT_DELIMITER.dataStream().collect(Collectors.joining());

    private final List<String> composedDelimiters = COMPOSED_OPERATORS.dataStream().collect(Collectors.toList());
    private AssignType assignType;

    private StringBuilder currentValue = new StringBuilder();
    private List<Token> currentLine = new ArrayList<Token>();

    /* Constructor */
    public Tokenizer(String sourceCode) {
        this.sourceCode = sourceCode;
        this.lines = new ArrayList<List<Token>>();
        this.assignType = new AssignType();
    }

    public List<List<Token>> tokenize() {

        //we process the text one char at a time
        for (int i = 0; i < sourceCode.length(); i++) {

            //if is comment ignore the rest of line until it finds a new line
            /* # comentários começam com o símbolo # e vão até ao fim da linha */
            if (isCommentDelimiter(sourceCode.charAt(i))) {
                // Ignore the rest of the line
                while (i < sourceCode.length() && !isNewLine(sourceCode.charAt(i))) {
                    i++;
                }
                continue;
            }

            char currentChar = sourceCode.charAt(i);

            //if is space or new line we ignore
            if (currentValue.length() == 0 && isToBeIgnored(currentChar))
                continue;

            //if the char is a delimiter or statment terminator we have a token and have to add it to the current line
            if (isValueChanger(currentChar)) {
                Token token;


                //add the current value
                if (!currentValue.toString().isEmpty()) {
                    token = getKeyValueToken(null, currentValue.toString());
                    storeFoundToken(token);
                }

                //the char is statment terminator or delimiter
                if (!isToBeIgnored(currentChar)) {

                    if (isStringDelimiter(currentChar)) {
                        currentValue.append(currentChar);
                        currentChar = sourceCode.charAt(++i);
                        while (!isStringDelimiter(currentChar)) {
                            currentValue.append(currentChar);
                            currentChar = sourceCode.charAt(++i);
                        }
                        currentValue.append(currentChar);
                        token = getKeyValueToken(TEXT, currentValue.toString());
                        token.setType(TYPES.STRING);
                        storeFoundToken(token);
                        continue;
                    }

                    // composed delimiter like "==" --> We add the two chars as a single token and move the counter 2 indexes ahead
                    String composedString = getComposedString(currentChar, i);
                    if (isComposedDelimiter(composedString)) {
                        token = getKeyValueToken(null, composedString);
                        storeFoundToken(token);
                        // Move
                        i++;
                        continue;
                    }

                    token = getKeyValueToken(null, String.valueOf(currentChar));
                    storeFoundToken(token);
                }

                //if is statement terminator we should add the current line to the list of lines and
                //create a new one
                //TODO - o } é uma linha
                if (isStatementTerminator(currentChar)) {
                    lines.add(currentLine);
                    currentLine = getNewLine();
                }

                continue;
            }

            currentValue.append(currentChar);

        }
        //adding the last line
        lines.add(currentLine);
        assignVectorName();

        assignConstantName();

        assignFunctionName();

        assignVariableName();

        verifyNullTokenTypesAndAssignAlreadyPresentVariables();

        assignNumeric();

        assignBooleanValues();

        //print for debugging purposes
        //lines.stream().forEach(line -> line.stream().forEach(token -> System.out.println(token.getType() + " - " + token.getValue() + " - " + token.getKey())));

        return lines;
    }

    //tokens with type null that match numeric pattern
    private void assignNumeric() {
        lines.stream().forEach(line -> line.stream()
                .filter(token -> token.getType() == null && token.getValue().matches("\\d+"))
                .forEach(token -> token.setTypeAndKey(TYPES.NUMERIC, NUMBER)));
    }

    //if previous is BASIC_TYPE and has next OR if it's after or before OPERATOR and it's not a number
    private void assignVariableName() {
        lines.stream().forEach(line -> line.stream()
                .filter(token -> token.getType() == null) // filter null elements
                .filter(token -> {
                    int currentIndex = line.indexOf(token);
                    return (currentIndex > 0 && currentIndex < line.size() - 1 // has previous and next element
                            && line.get(currentIndex - 1).getType() == TYPES.BASIC_TYPE); // previous is BASIC_TYPE
                })
                .forEach(token -> token.setTypeAndKey(TYPES.VARIABLE_NAME, VAR_NAME)));
    }

    // Might be needed in the near future
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public void verifyNullTokenTypesAndAssignAlreadyPresentVariables() {
        int lineIndexCurrentToken = 0;
        for (List<Token> line : lines) {
            for (Token currentToken : line) {
                if(currentToken.getType() == null) {
                    for (int j = 0; j < lineIndexCurrentToken; j++) {
                        List<Token> previousLine = lines.get(j);
                        previousLine.stream().forEach(previousToken -> {
                            if (previousToken.getType() != null
                                    && previousToken.getValue().equals(currentToken.getValue())
                            ) {
                                currentToken.setTypeAndKey(previousToken.getType(), previousToken.getKey());
                            }
                        });
                    }
                }
            }
            lineIndexCurrentToken++;
        }
    }

    //if first in line or previous is  }
    private void assignFunctionName() {
        Set<String> set = new HashSet();
        lines.stream().forEach(line -> line.stream()
                .filter(token -> token.getType() == null)
                .filter(token -> {
                    int currentIndex = line.indexOf(token);
                        return (currentIndex==0 && line.size() > 1 && line.get(currentIndex + 1).getKey() == LPAREN)
                                || (currentIndex > 0 && line.get(currentIndex - 1).getKey() == RBRACE);
                })
                .forEach(token -> {
                    token.setTypeAndKey(TYPES.FUNCTION_NAME, FUN_NAME);
                    set.add(token.getValue());
                }
                ));
        lines.stream().forEach(line -> line.stream()
                .filter(token -> token.getType() == null)
                .filter(token -> set.contains(token.getValue()))
                .forEach(token -> token.setTypeAndKey(TYPES.FUNCTION_NAME, FUN_NAME))
        );
    }

    //if
    private void assignConstantName() {
        lines.stream().forEach(line -> line.stream()
                .filter(token -> token.getType() == null) 
                .filter(token -> {
                    int currentIndex = line.indexOf(token);
                    return line.get(0).getKey() == CONST 
                            && currentIndex > 0 && currentIndex < line.size() - 1 
                            && line.get(currentIndex - 1).getType() == TYPES.BASIC_TYPE; 
                })
                .forEach(token -> token.setTypeAndKey(TYPES.CONSTANT_NAME, CON_NAME )));
    }

    private void assignVectorName() {
        lines.stream().forEach(line -> line.stream()
                .filter(token -> token.getType() == null) 
                .filter(token -> {
                    int currentIndex = line.indexOf(token);
                    return currentIndex > 0 && currentIndex < line.size() - 1 
                            && line.get(currentIndex - 1).getType() == TYPES.BASIC_TYPE 
                            && line.get(currentIndex + 1).getKey() == LBRACKET; 
                })
                .forEach(token -> token.setTypeAndKey(TYPES.VECTOR_NAME, VEC_NAME)));
    }

    private void assignBooleanValues() {
        lines.stream().forEach(line -> line.stream()
                .filter(token -> token.getType() == null && token.getValue().matches("true|false"))
                .forEach(token -> {
                    if(token.getValue().equals("true")) {
                        token.setTypeAndKey(TYPES.BOOLEAN, BOOL_VAR.TRUE);
                    } else {
                        token.setTypeAndKey(TYPES.BOOLEAN, BOOL_VAR.FALSE);
                    }
                })
        );
    }


    private static Token getKeyValueToken(BASIC_GRAMMAR grammarKey, String currentValue) {
        return new Token(grammarKey, currentValue);
    }

    //Array factory
    private List<Token> getNewLine() {
        return new ArrayList<Token>();
    }

    //checks if the character is a new line
    private boolean isNewLine(char c) {
        return !String.valueOf(c).matches(".");
    }

    //checks if the character is a space
    private boolean isSpace(char c) {
        return String.valueOf(c).matches(" ");
    }

    //checks if the character is to be ignored by the tokenizer
    private boolean isToBeIgnored(char c) {
        return isSpace(c) || isNewLine(c);
    }

    private boolean isDelimiter(char c) {
        return delimiters.contains(String.valueOf(c));
    }

    private boolean isStatementTerminator(char c) {
        return statmentTerminator.equals(String.valueOf(c));
    }

    private boolean isValueChanger(char c){
        return isDelimiter(c) || isStatementTerminator(c) || isToBeIgnored(c) || isStringDelimiter(c);
    }

    private boolean isComposedDelimiter (String composedString) {
        if (composedDelimiters.contains(composedString)) {
            return true;
        }

        return false;
    }


    private boolean isStringDelimiter (char c) {
        String str = String.valueOf(c);
        return (str.indexOf(8220)>-1 || str.indexOf(8221)>-1 || str.indexOf(34)>-1);
    }

    private String getComposedString (char c, int currentIndex) {
        StringBuilder sb = new StringBuilder();
        sb.append(c);
        if(sourceCode.length() > currentIndex + 1)
            sb.append(sourceCode.charAt(currentIndex + 1));

        String composedString = sb.toString();

        return composedString;
    }

    private boolean isCommentDelimiter(char c) {
        return commentDelimiter.equals(String.valueOf(c));
    }

    /*
    * Once a delimiter or a statement terminator is found we store the "cached token" into the read values
    * Meaning that the token that was being cached is now finished and a new one is supposed to be read
    * */
    private void storeFoundToken(Token token) {
        if(token.getKey() == null) {
            BASIC_GRAMMAR key = assignType.getKeyToData().getOrDefault(token.getValue(), null);
            TYPES type = assignType.getKeyToType().getOrDefault(token.getValue(), null);
            token.setKey(key);
            token.setType(type);
        }
        currentLine.add(token);
        currentValue = new StringBuilder();
    }


}
