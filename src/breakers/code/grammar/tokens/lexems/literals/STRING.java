package breakers.code.grammar.tokens.lexems.literals;

import breakers.code.grammar.tokens.TYPES;
import breakers.code.grammar.tokens.lexems.BASIC_GRAMMAR;

import java.util.stream.Stream;

public enum STRING implements BASIC_GRAMMAR {

    TEXT("text");


    private String data;

    STRING(String data) {
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

    public static Stream<STRING> stream() {
        return Stream.of(STRING.values());
    }
    public static Stream<String> dataStream() { return Stream.of(STRING.values()).map(STRING::getData);}
}
