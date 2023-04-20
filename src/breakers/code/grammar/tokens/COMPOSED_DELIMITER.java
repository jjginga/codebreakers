package breakers.code.grammar.tokens;

import java.util.stream.Stream;

public enum COMPOSED_DELIMITER implements BASIC_GRAMMAR {
    EQUALS_EQUALS("=="),
    EQUALS_NOT_EQUALS("!="),
    LESS_THAN_OR_EQUALS("<="),
    GREATER_THAN_OR_EQUALS(">="),;

    private String type;

    COMPOSED_DELIMITER(String type) {
        this.type = type;
    }

    // standard getters and setters
    public String getType() {
        return this.type;
    }

    public static Stream<COMPOSED_DELIMITER> stream() {
        return Stream.of(COMPOSED_DELIMITER.values());
    }

    public static Stream<String> types() { return Stream.of(COMPOSED_DELIMITER.values()).map(COMPOSED_DELIMITER::getType);}
}
