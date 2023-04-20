package breakers.code.grammar.tokens;

import java.util.stream.Stream;

public enum RESERVED_WORDS implements BASIC_GRAMMAR {
   POW("pow"),
   SQUARE_ROOT("square_root"),
   IF("if"),
   ELSE("else"),
   WHILE("while");

    private String type;

    RESERVED_WORDS(String type) {
        this.type = type;
    }

    // standard getters and setters
    public String getType() {
        return this.type;
    }

    public static Stream<RESERVED_WORDS> stream() {
        return Stream.of(RESERVED_WORDS.values());
    }

    public static Stream<String> types() { return Stream.of(RESERVED_WORDS.values()).map(RESERVED_WORDS::getType);}
}
