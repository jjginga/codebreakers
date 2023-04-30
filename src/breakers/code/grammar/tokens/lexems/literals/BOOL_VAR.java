package breakers.code.grammar.tokens.lexems.literals;

import breakers.code.grammar.tokens.TYPES;
import breakers.code.grammar.tokens.lexems.BASIC_GRAMMAR;

import java.util.stream.Stream;

public enum BOOL_VAR implements BASIC_GRAMMAR {

    TRUE("true"),
    FALSE("false");

    private String data;

    BOOL_VAR(String data) {
        this.data = data;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return TYPES.BASIC_TYPE;
    }

    public static Stream<BOOL_VAR> stream() {
        return Stream.of(BOOL_VAR.values());
    }

    public static Stream<String> dataStream() { return Stream.of(BOOL_VAR.values()).map(BOOL_VAR::getData);}

}
