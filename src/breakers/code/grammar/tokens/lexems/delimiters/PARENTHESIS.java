package breakers.code.grammar.tokens.lexems.delimiters;

import breakers.code.grammar.tokens.TYPES;

import java.util.stream.Stream;

public enum PARENTHESIS implements BASIC_DELIMITERS {

    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    LBRACKET("["),
    RBRACKET("]");



    private String data;

    PARENTHESIS(String name) {
        this.data = name;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return TYPES.PARENTHESIS;
    }

    public static Stream<PARENTHESIS> stream() {
        return Stream.of(PARENTHESIS.values());
    }
    public static Stream<String> dataStream() { return Stream.of(PARENTHESIS.values()).map(PARENTHESIS::getData);}
}
