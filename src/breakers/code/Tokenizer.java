package breakers.code;

import breakers.code.grammar.tokens.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static breakers.code.grammar.tokens.STRING_DELIMITERS.TEXT;


/*This class gets the source code and splits it into tokens
* it reads the sourceCode char by char and splits it into tokens by a delimiter
* the delimeters are: space , + , - , * , / , . , ; , ( , ), [ , ] , { , } */
public class Tokenizer {

    /* The argument, a string with the source code */
    private final String sourceCode;
    /*The result - a list of lists with the tokens */
    private List<List<KeyValueToken>> lines;

    private String delimiters = BASIC_DELIMITERS.types().collect(Collectors.joining());
    private String statmentTerminator = STATEMENT_TERMINATOR.types().collect(Collectors.joining());
    private String commentDelimiter = COMMENT_DELIMITER.types().collect(Collectors.joining());
    private String stringDelimiter = STRING_DELIMITERS.types().collect(Collectors.joining());

    private final List<String> composedDelimiters = COMPOSED_DELIMITER.types().collect(Collectors.toList());
    private TypeToKey typeToKey;

    private StringBuilder currentValue = new StringBuilder();
    private List<KeyValueToken> currentLine = new ArrayList<KeyValueToken>();

    /* Constructor */
    public Tokenizer(String sourceCode) {
        this.sourceCode = sourceCode;
        this.lines = new ArrayList<List<KeyValueToken>>();
        this.typeToKey = new TypeToKey();
    }

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
                if(!currentValue.toString().isEmpty()) {
                    token = getKeyValueToken(null, currentValue.toString());
                    storeFoundToken(token);
                }

                //the char is statment terminator or delimiter
                if(!isToBeIgnored(currentChar)) {

                    if(isStringDelimiter(currentChar)) {
                        currentValue.append(currentChar);
                        currentChar = sourceCode.charAt(++i);
                        while(!isStringDelimiter(currentChar)) {
                            currentValue.append(currentChar);
                            currentChar = sourceCode.charAt(++i);
                        }
                        currentValue.append(currentChar);
                        token = getKeyValueToken(TEXT, currentValue.toString());
                        storeFoundToken(token);
                        continue;
                    }

                    // composed delimiter like "==" --> We add the two chars as a single token and move the counter 2 indexes ahead
                    String composedString = getComposedString(currentChar, i);
                    if(isComposedDelimiter(composedString)){
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
                if(isStatementTerminator(currentChar)) {
                    lines.add(currentLine);
                    currentLine = getNewLine();
                }

                continue;
            }

            currentValue.append(currentChar);

        }
        lines.stream().forEach(line -> line.stream().forEach(token -> System.out.println(token.getKey()+" : "+token.getValue())));

        return lines;
    }

    private static KeyValueToken getKeyValueToken(BASIC_GRAMMAR grammarKey, String currentValue) {
        return new KeyValueToken(grammarKey, currentValue);
    }

    //Array factory
    private List<KeyValueToken> getNewLine() {
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
    private void storeFoundToken(KeyValueToken token) {
        if(token.getKey() == null) {
            BASIC_GRAMMAR key = typeToKey.getKeyToType().getOrDefault(token.getValue(), null);
            token.setKey(key);

        }
        currentLine.add(token);
        currentValue = new StringBuilder();
    }

}
