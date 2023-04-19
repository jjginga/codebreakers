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

    public static Stream<BASIC_VAR> stream() {
        return Stream.of(BASIC_VAR.values());
    }
}
