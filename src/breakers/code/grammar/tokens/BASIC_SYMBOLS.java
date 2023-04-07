package breakers.code.grammar.tokens;


import java.util.stream.Stream;

public enum BASIC_SYMBOLS {

    COMMENT("#"),
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    EQUALS("="),
    SEMICOLON(";"),
    OPEN_PARENTHESIS("("),
    CLOSE_PARENTHESIS(")"),
    OPEN_BRACKET("{"),
    CLOSE_BRACKET("}"),
    OPEN_SQUARE_BRACKET("["),
    CLOSE_SQUARE_BRACKET("]"),
    COMMA(","),
    COLON(":"),
    LESS_THAN("<"),
    SPACE(" "),
    GREATER_THAN(">");


    private String type;

    BASIC_SYMBOLS(String type) {
        this.type = type;
    }

    // standard getters and setters
    public String getType() {
        return this.type;
    }

    public static Stream<BASIC_SYMBOLS> stream() {
        return Stream.of(BASIC_SYMBOLS.values());
    }

}
