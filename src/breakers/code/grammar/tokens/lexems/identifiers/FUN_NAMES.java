package breakers.code.grammar.tokens.lexems.identifiers;

import breakers.code.grammar.tokens.TYPES;

import java.util.stream.Stream;

public enum FUN_NAMES implements NAMES {

    FUN_NAME("fun_name");

    private String data;

    FUN_NAMES(String data) {
        this.data = data;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return TYPES.FUNCTION_NAME;
    }

    public static Stream<FUN_NAMES> stream() {
        return Stream.of(FUN_NAMES.values());
    }

    public static Stream<String> dataStream() { return Stream.of(FUN_NAMES.values()).map(FUN_NAMES::getData);}
}
