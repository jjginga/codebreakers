package breakers.code.grammar.tokens.lexems.identifiers;

import breakers.code.grammar.tokens.TYPES;
import breakers.code.grammar.tokens.lexems.BASIC_GRAMMAR;

import java.util.stream.Stream;

public enum VAR_NAMES implements NAMES {

    VAR_NAME("var_name");

    private String data;

    VAR_NAMES(String data) {
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

    public static Stream<VAR_NAMES> stream() {
        return Stream.of(VAR_NAMES.values());
    }

    public static Stream<String> dataStream() { return Stream.of(VAR_NAMES.values()).map(VAR_NAMES::getData);}
}
