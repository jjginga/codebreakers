package breakers.code.grammar.tokens.lexems.delimiters;

import breakers.code.grammar.tokens.TYPES;

import java.util.stream.Stream;

public enum PONCTUATION implements BASIC_DELIMITERS {

    COMMA(",");


    private String data;

    PONCTUATION(String name) {
        this.data = name;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return TYPES.PONCTUATION;
    }

    public static Stream<PONCTUATION> stream() {
        return Stream.of(PONCTUATION.values());
    }
    public static Stream<String> dataStream() { return Stream.of(PONCTUATION.values()).map(PONCTUATION::getData);}
}
