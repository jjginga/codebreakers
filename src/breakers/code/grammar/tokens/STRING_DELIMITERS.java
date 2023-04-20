package breakers.code.grammar.tokens;

import java.util.stream.Stream;

public enum STRING_DELIMITERS implements BASIC_GRAMMAR {

    //name for \"
    DQUOTE("\""),
    QUOTE("”"),
    TEXT("text");



    private String type;

    STRING_DELIMITERS(String type) {
        this.type = type;
    }

    // standard getters and setters
    public String getType() {
        return this.type;
    }

    public static Stream<STRING_DELIMITERS> stream() {
        return Stream.of(STRING_DELIMITERS.values());
    }
    public static Stream<String> types() { return Stream.of(STRING_DELIMITERS.values()).map(STRING_DELIMITERS::getType);}
}
