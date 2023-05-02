package breakers.code.grammar.tokens.lexems.delimiters;

import breakers.code.grammar.tokens.TYPES;
import breakers.code.grammar.tokens.lexems.BASIC_GRAMMAR;

import java.util.stream.Stream;

import static breakers.code.grammar.tokens.TYPES.COMPARASION_OPERATOR;

public enum COMPOSED_OPERATORS implements BASIC_GRAMMAR {
    EQUALS_EQUALS("=="),
    EQUALS_NOT_EQUALS("!="),
    LESS_THAN_OR_EQUALS("<="),
    GREATER_THAN_OR_EQUALS(">="),
    MULEQUALS("*="),

    OR("or");

    private String data;

    COMPOSED_OPERATORS(String data) {
        this.data = data;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return COMPARASION_OPERATOR;
    }

    public static Stream<COMPOSED_OPERATORS> stream() {
        return Stream.of(COMPOSED_OPERATORS.values());
    }

    public static Stream<String> dataStream() { return Stream.of(COMPOSED_OPERATORS.values()).map(COMPOSED_OPERATORS::getData);}
}
