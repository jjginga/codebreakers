package breakers.code.grammar.tokens.lexems;

import breakers.code.grammar.tokens.TYPES;

import java.util.stream.Stream;

public enum NEED_FOR_PARSING implements BASIC_GRAMMAR {

    BOOLEAN_EXPRESSION("boolean_expression"),
    IF_BODY_("if_body"),
    STRUCT_NAME("struct_name"),
    FOR_VAR("for_VAR"),
    FOR_INIT("for_init"),
    FOR_END("for_end"),
    FOR_INCREMENT("for_increment"),
    ASSIGNMENT("assignment"),
    EXPRESSION("expression"),
    ARGUMENTS("arguments");

    private String data;

    NEED_FOR_PARSING(String data) {
        this.data = data;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return TYPES.INFORMATION_FLOW;
    }

    public static Stream<NEED_FOR_PARSING> stream() {
        return Stream.of(NEED_FOR_PARSING.values());
    }

    public static Stream<String> dataStream() { return Stream.of(NEED_FOR_PARSING.values()).map(NEED_FOR_PARSING::getData);}
}
