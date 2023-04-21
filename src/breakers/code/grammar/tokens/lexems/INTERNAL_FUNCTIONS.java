package breakers.code.grammar.tokens.lexems;

import breakers.code.grammar.tokens.TYPES;

import java.util.stream.Stream;

public enum INTERNAL_FUNCTIONS implements BASIC_GRAMMAR {
   POW("pow"),
   SQUARE_ROOT("square_root"),
   GEN("gen");

    private String data;

    INTERNAL_FUNCTIONS(String data) {
        this.data = data;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return TYPES.INTERNAL_FUNCTIONS;
    }

    public static Stream<INTERNAL_FUNCTIONS> stream() {
        return Stream.of(INTERNAL_FUNCTIONS.values());
    }

    public static Stream<String> dataStream() { return Stream.of(INTERNAL_FUNCTIONS.values()).map(INTERNAL_FUNCTIONS::getData);}
}
