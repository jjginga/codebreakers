package breakers.code.grammar.tokens;

import java.util.stream.Stream;

public enum BASIC_VAR {

    INT("int"),
    FLOAT("float"),
    BOOL("bool");

    private String type;

    BASIC_VAR(String type) {
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