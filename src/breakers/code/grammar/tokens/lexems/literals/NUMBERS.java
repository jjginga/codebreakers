package breakers.code.grammar.tokens.lexems.literals;

import breakers.code.grammar.tokens.TYPES;
import breakers.code.grammar.tokens.lexems.BASIC_GRAMMAR;

import java.util.stream.Stream;

public enum NUMBERS implements BASIC_GRAMMAR {

    NUMBER("number");



    private String data;

    NUMBERS(String data) {
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

    public static Stream<NUMBERS> stream() {
        return Stream.of(NUMBERS.values());
    }
    public static Stream<String> dataStream() { return Stream.of(NUMBERS.values()).map(NUMBERS::getData);}
}
