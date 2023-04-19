package breakers.code.grammar.tokens;

import java.util.stream.Stream;

public enum NAMES implements BASIC_GRAMMAR {

    VAR_NAME("var_name"),
    FUN_NAME("fun_name"),
    VEC_NAME("vec_name");

    private String type;

    NAMES(String type) {
        this.type = type;
    }

    // standard getters and setters
    public String getType() {
        return this.type;
    }

    public static Stream<NAMES> stream() {
        return Stream.of(NAMES.values());
    }
}
