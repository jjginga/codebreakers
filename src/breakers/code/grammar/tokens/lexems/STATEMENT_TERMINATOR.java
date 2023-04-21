package breakers.code.grammar.tokens.lexems;


import breakers.code.grammar.tokens.TYPES;

import java.util.stream.Stream;

public enum STATEMENT_TERMINATOR implements BASIC_GRAMMAR{

    SEMICOLON(";");

    private String data;

    STATEMENT_TERMINATOR(String data) {
        this.data = data;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return TYPES.SEMICOLON;
    }

    public static Stream<STATEMENT_TERMINATOR> stream() {
        return Stream.of(STATEMENT_TERMINATOR.values());
    }

    public static Stream<String> dataStream() { return Stream.of(STATEMENT_TERMINATOR.values()).map(STATEMENT_TERMINATOR::getData);}

}
