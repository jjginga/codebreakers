package breakers.code.grammar.tokens.lexems.identifiers;

import breakers.code.grammar.tokens.TYPES;

import java.util.stream.Stream;

public enum CON_NAMES implements NAMES {

    CON_NAME("con_names");

    private String data;

    CON_NAMES(String data) {
        this.data = data;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return TYPES.CONSTANT_NAME;
    }

    public static Stream<CON_NAMES> stream() {
        return Stream.of(CON_NAMES.values());
    }

    public static Stream<String> dataStream() { return Stream.of(CON_NAMES.values()).map(CON_NAMES::getData);}
}
