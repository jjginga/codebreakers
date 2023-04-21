package breakers.code.grammar.tokens.lexems;

import breakers.code.grammar.tokens.TYPES;

import java.util.stream.Stream;

public enum INPUT_OUTPUT implements BASIC_GRAMMAR {
    WRITE("write"),
    WRITE_ALL("write_all"),
    WRITE_STRING("write_string"),
    READ("read"),
    READ_ALL("read_all"),
    READ_STRING("read_string");

    private String data;

    INPUT_OUTPUT(String data) {
        this.data = data;
    }

    // standard getters and setters
    public String getData() {
        return this.data;
    }

    @Override
    public TYPES getType() {
        return TYPES.IO_FUNCTION;
    }

    public static Stream<INPUT_OUTPUT> stream() {
        return Stream.of(INPUT_OUTPUT.values());
    }

    public static Stream<String> dataStream() { return Stream.of(INPUT_OUTPUT.values()).map(INPUT_OUTPUT::getData);}
}
