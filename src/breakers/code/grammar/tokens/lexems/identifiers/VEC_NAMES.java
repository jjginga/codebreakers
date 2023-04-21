package breakers.code.grammar.tokens.lexems.identifiers;

import breakers.code.grammar.tokens.TYPES;

import java.util.stream.Stream;

public enum VEC_NAMES implements NAMES {

    VEC_NAME("vec_name");

    private String data;

    VEC_NAMES(String data) {
        this.data = data;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return TYPES.VARIABLE_NAME;
    }

    public static Stream<VEC_NAMES> stream() {
        return Stream.of(VEC_NAMES.values());
    }

    public static Stream<String> dataStream() { return Stream.of(VEC_NAMES.values()).map(VEC_NAMES::getData);}
}
