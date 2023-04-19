package breakers.code.grammar.tokens;


import java.util.stream.Stream;

public enum COMMENT_DELIMITER implements BASIC_GRAMMAR{

    COMMENT("#");

    private String type;

    COMMENT_DELIMITER(String type) {
        this.type = type;
    }

    // standard getters and setters
    public String getType() {
        return this.type;
    }

    public static Stream<COMMENT_DELIMITER> stream() {
        return Stream.of(COMMENT_DELIMITER.values());
    }

}
