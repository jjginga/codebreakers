package breakers.code.grammar.tokens.lexems.literals;

import breakers.code.grammar.tokens.TYPES;
import breakers.code.grammar.tokens.lexems.BASIC_GRAMMAR;

import java.util.stream.Stream;

public enum VEC_VALUES implements BASIC_GRAMMAR {

    VEC_VALUE("vec_value");

    private String data;

    VEC_VALUES(String data) {
        this.data = data;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return TYPES.STRING;
    }

    public static Stream<VEC_VALUES> stream() {
        return Stream.of(VEC_VALUES.values());
    }
    public static Stream<String> dataStream() { return Stream.of(VEC_VALUES.values()).map(VEC_VALUES::getData);}
}
