package breakers.code.grammar.tokens.lexems;

import breakers.code.grammar.tokens.TYPES;

import java.util.stream.Stream;

public enum GENERAL_SCHEMA implements BASIC_GRAMMAR {
    STRUCTS("structs"),
    CONST("const"),
    GLOBAL("global"),
    MAIN("main"),
    LOCAL("local");

    private String data;

    GENERAL_SCHEMA(String data) {
        this.data = data;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return TYPES.GENERAL;
    }

    public static Stream<GENERAL_SCHEMA> stream() {
        return Stream.of(GENERAL_SCHEMA.values());
    }

    public static Stream<String> dataStream() { return Stream.of(GENERAL_SCHEMA.values()).map(GENERAL_SCHEMA::getData);}
}
