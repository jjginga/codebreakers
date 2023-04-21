package breakers.code.grammar.tokens.lexems.delimiters;

import breakers.code.grammar.tokens.TYPES;
import breakers.code.grammar.tokens.lexems.BASIC_GRAMMAR;

import java.util.stream.Stream;

import static breakers.code.grammar.tokens.TYPES.OPERATOR;

public enum OPERATORS implements BASIC_DELIMITERS {

    PLUS("+"),
    MINUS("-"),
    MUL("*"),
    DIV("/"),
    EQUALS("="),
    GREATER_THAN(">"),
    LESS_THAN("<");




    private String data;

    OPERATORS(String name) {
        this.data = name;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return OPERATOR;
    }

    public static Stream<OPERATORS> stream() {
        return Stream.of(OPERATORS.values());
    }
    public static Stream<String> dataStream() { return Stream.of(OPERATORS.values()).map(OPERATORS::getData);}
}
