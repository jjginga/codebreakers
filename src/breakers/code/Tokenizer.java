package breakers.code;

import breakers.code.grammar.tokens.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static breakers.code.grammar.tokens.COMPOSED_DELIMITER.EQUALS_EQUALS;

/*This class gets the source code and splits it into tokens
* it reads the sourceCode char by char and splits it into tokens by a delimiter
* the delimeters are: space , + , - , * , / , . , ; , ( , ), [ , ] , { , } */
public class Tokenizer {

    /* The argument, a string with the source code */
    private final String sourceCode;
    /*The result - a list of lists with the tokens */
    private List<List<KeyValueToken>> lines;

    //TODO:obtain delimiters and terminators from the grammar
    private String delimiters = "+-*/=()[]{}<>";
    private String statmentTerminator = ";";
    private String commentDelimiter = "#";

    private final List<String> composedDelimiters = Arrays.asList("==", "!=", ">=", "<=", "++", "--");

    private StringBuilder currentValue = new StringBuilder();
    private List<KeyValueToken> currentLine = new ArrayList<KeyValueToken>();


    /* Constructor */
    public Tokenizer(String sourceCode) {
        this.sourceCode = sourceCode;
        this.lines = new ArrayList<List<KeyValueToken>>();
    }

    //TODO: composed delimiters, like "==", "!=", ">=", "<=", "++", "--"
    public List<List<KeyValueToken>> tokenize() {
        //we process the text one char at a time
        for (int i = 0; i < sourceCode.length(); i++) {

            //if is comment ignore the rest of line until it finds a new line
            /* # comentários começam com o símbolo # e vão até ao fim da linha */
            if (isCommentDelimiter(sourceCode.charAt(i))) {
                // Ignore the rest of the line
                while (i < sourceCode.length() && !isNewLine(sourceCode.charAt(i))){
                    i++;
                }
                continue;
            }

            char currentChar = sourceCode.charAt(i);

            //if is space or new line we ignore
            if(currentValue.length()==0 && isToBeIgnored(currentChar))
                continue;

            //if the char is a delimiter or statment terminator we have a token and have to add it to the current line
            if(isValueChanger(currentChar)) {
                KeyValueToken token;

                //add the current value
                //TODO: not preform this for empty current value
                if(!currentValue.toString().isEmpty()) {
                    token = getKeyValueToken(null, currentValue.toString());
                    storeFoundToken(token);
                }

                //the char is statment terminator or delimiter
                if(!isToBeIgnored(currentChar)) {
                    //TODO: before creating token we should peek to see if it is composed like "==".

                    // composed delimiter like "==" --> We add the two chars as a single token and move the counter 2 indexes ahead
                    String composedString = getComposedString(currentChar, i);
                    if(isComposedDelimiter(composedString)){
                        //TODO --> Need to add the correct one (for now it's hard coded to be only "EQUALS EQUALS" we need the other composed delimiters as well
                        token = getKeyValueToken(EQUALS_EQUALS, composedString);
                        currentLine.add(token);

                        // Move
                        i++;
                        continue;
                    }

                    // TODO --> Assign correct key according to grammar
                    token = getKeyValueToken(null, String.valueOf(currentChar));
                    currentLine.add(token);
                }

                //if is statement terminator we should add the current line to the list of lines and
                //create a new one
                if(isStatementTerminator(currentChar)) {
                    lines.add(currentLine);
                    currentLine = getNewList();
                }

                continue;
            }



            currentValue.append(currentChar);

        }
        lines.stream().forEach(line -> line.stream().forEach(token -> System.out.println(token.getValue())));
        return lines;

    }


    private static KeyValueToken getKeyValueToken(BASIC_GRAMMAR grammarKey, String currentValue) {
        return new KeyValueToken(grammarKey, currentValue); // @Joel --> Joel, why was this being set to null? We need to keep track of the types of token we have for later validations
        // For instance we cannot have 2 BASIC_VARs in a row like "int int", to validate that we need to know their types --> which will be the KEY on the KeyValueToken
    }


    //Array factory
    private List<KeyValueToken> getNewList() {
        return new ArrayList<KeyValueToken>();
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
        return isDelimiter(c) || isStatementTerminator(c) || isToBeIgnored(c);
    }

    private boolean isComposedDelimiter (String composedString) {
        if (composedDelimiters.contains(composedString)) {
            return true;
        }

        return false;
    }

    private String getComposedString (char c, int currentIndex) {
        StringBuilder sb = new StringBuilder();
        sb.append(c);
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
    private void storeFoundToken(KeyValueToken token) {
        // TODO --> Assign correct key according to grammar
        currentLine.add(token);

        currentValue = new StringBuilder();
    }

}
