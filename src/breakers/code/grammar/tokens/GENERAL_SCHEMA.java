package breakers.code.grammar.tokens;

import java.util.stream.Stream;

public enum GENERAL_SCHEMA implements BASIC_GRAMMAR {
    STRUCTS("structs"),
    CONST("const"),
    GLOBAL("global");

    private String type;

    GENERAL_SCHEMA(String type) {
        this.type = type;
    }

    // standard getters and setters
    public String getType() {
        return this.type;
    }

    public static Stream<GENERAL_SCHEMA> stream() {
        return Stream.of(GENERAL_SCHEMA.values());
    }

    public static Stream<String> types() { return Stream.of(GENERAL_SCHEMA.values()).map(GENERAL_SCHEMA::getType);}
}
