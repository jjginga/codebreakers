package breakers.code.grammar.tokens.lexems;


import breakers.code.grammar.tokens.TYPES;

import java.util.stream.Stream;

public enum COMMENT_DELIMITER implements BASIC_GRAMMAR{

    COMMENT("#");

    private String data;

    COMMENT_DELIMITER(String data) {
        this.data = data;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return null;
    }

    public static Stream<COMMENT_DELIMITER> stream() {
        return Stream.of(COMMENT_DELIMITER.values());
    }

    public static Stream<String> dataStream() { return Stream.of(COMMENT_DELIMITER.values()).map(COMMENT_DELIMITER::getData);}

}
