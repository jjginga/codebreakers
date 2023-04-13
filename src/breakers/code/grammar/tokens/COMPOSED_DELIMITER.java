package breakers.code.grammar.tokens;

import java.util.stream.Stream;

public enum COMPOSED_DELIMITER implements BASIC_GRAMMAR {
    EQUALS_EQUALS("==");

    private String type;

    COMPOSED_DELIMITER(String type) {
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
