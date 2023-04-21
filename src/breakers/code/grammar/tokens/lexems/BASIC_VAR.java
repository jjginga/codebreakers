package breakers.code.grammar.tokens.lexems;

import breakers.code.grammar.tokens.TYPES;

import java.util.stream.Stream;

public enum BASIC_VAR implements BASIC_GRAMMAR {

    INT("int"),
    FLOAT("float"),
    BOOL("bool");

    private String data;

    BASIC_VAR(String data) {
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

    public static Stream<BASIC_VAR> stream() {
        return Stream.of(BASIC_VAR.values());
    }

    public static Stream<String> dataStream() { return Stream.of(BASIC_VAR.values()).map(BASIC_VAR::getData);}

}
