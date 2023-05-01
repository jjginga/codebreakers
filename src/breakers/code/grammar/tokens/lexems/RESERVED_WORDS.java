package breakers.code.grammar.tokens.lexems;

import breakers.code.grammar.tokens.TYPES;

import java.util.stream.Stream;

public enum RESERVED_WORDS implements BASIC_GRAMMAR {
   IF("if"),
   ELSE("else"),
   WHILE("while"),
   FOR("for"),
   RETURN("return");

    private String data;

    RESERVED_WORDS(String data) {
        this.data = data;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return TYPES.INFORMATION_FLOW;
    }

    public static Stream<RESERVED_WORDS> stream() {
        return Stream.of(RESERVED_WORDS.values());
    }

    public static Stream<String> dataStream() { return Stream.of(RESERVED_WORDS.values()).map(RESERVED_WORDS::getData);}
}
