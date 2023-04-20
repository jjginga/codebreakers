package breakers.code.grammar.tokens;

import java.util.stream.Stream;

public enum INPUT_OUTPUT implements BASIC_GRAMMAR {
    WRITE("write"),
    WRITE_ALL("write_all"),
    WRITE_STRING("write_string"),
    READ("read"),
    READ_ALL("read_all"),
    READ_STRING("read_string");

    private String type;

    INPUT_OUTPUT(String type) {
        this.type = type;
    }

    // standard getters and setters
    public String getType() {
        return this.type;
    }

    public static Stream<INPUT_OUTPUT> stream() {
        return Stream.of(INPUT_OUTPUT.values());
    }

    public static Stream<String> types() { return Stream.of(INPUT_OUTPUT.values()).map(INPUT_OUTPUT::getType);}
}
