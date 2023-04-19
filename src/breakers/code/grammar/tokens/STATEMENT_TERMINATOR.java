package breakers.code.grammar.tokens;


import java.util.stream.Stream;

public enum STATEMENT_TERMINATOR implements BASIC_GRAMMAR{

    SEMICOLON(";");

    private String type;

    STATEMENT_TERMINATOR(String type) {
        this.type = type;
    }

    // standard getters and setters
    public String getType() {
        return this.type;
    }

    public static Stream<STATEMENT_TERMINATOR> stream() {
        return Stream.of(STATEMENT_TERMINATOR.values());
    }

}
