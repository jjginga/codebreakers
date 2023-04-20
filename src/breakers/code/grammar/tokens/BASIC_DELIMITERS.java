package breakers.code.grammar.tokens;

import java.util.stream.Stream;

public enum BASIC_DELIMITERS implements BASIC_GRAMMAR {

    PLUS("+"),
    MINUS("-"),
    MUL("*"),
    DIV("/"),
    EQUALS("="),
    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    LBRACKET("["),
    RBRACKET("]"),
    LANGLE("<"),
    RANGLE(">"),
    COMMA(",");


    private String type;

    BASIC_DELIMITERS(String type) {
        this.type = type;
    }

    // standard getters and setters
    public String getType() {
        return this.type;
    }

    public static Stream<BASIC_DELIMITERS> stream() {
        return Stream.of(BASIC_DELIMITERS.values());
    }
    public static Stream<String> types() { return Stream.of(BASIC_DELIMITERS.values()).map(BASIC_DELIMITERS::getType);}
}
