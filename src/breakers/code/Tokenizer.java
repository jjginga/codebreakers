package breakers.code;

import breakers.code.grammar.tokens.BASIC_GRAMMAR;
import breakers.code.grammar.tokens.BASIC_SYMBOLS;
import breakers.code.grammar.tokens.KeyValueToken;

import java.util.ArrayList;
import java.util.List;

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

    /* Constructor */
    public Tokenizer(String sourceCode) {
        this.sourceCode = sourceCode;
        this.lines = new ArrayList<List<KeyValueToken>>();
    }

    //TODO: composed delimiters, like "==", "!=", ">=", "<=", "++", "--"
    public List<List<KeyValueToken>> tokenize() {

        StringBuilder currentValue = new StringBuilder();
        List<KeyValueToken> currentLine = getNewList();


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

                //add the current value
                //TODO: not preform this for empty current value
                KeyValueToken token = getKeyValueToken(currentValue.toString());
                currentLine.add(token);

                currentValue = new StringBuilder();

                //the char is statment terminator or delimiter
                if(!isToBeIgnored(currentChar)) {
                    //TODO: before creating token we should peek to see if it is composed like "==".
                    token = getKeyValueToken(String.valueOf(currentChar));
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


    private static KeyValueToken getKeyValueToken(String currentValue) {
        return new KeyValueToken(null, currentValue);
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

    private boolean isCommentDelimiter(char c) {
        return commentDelimiter.equals(String.valueOf(c));
    }

}
