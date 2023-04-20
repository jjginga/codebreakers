package breakers.code.grammar.tokens;

import java.util.EnumSet;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TypeToKey {

    private Map<BASIC_GRAMMAR, String> typeToKey;



    private Map<String, BASIC_GRAMMAR> keyToType;

    public TypeToKey() {
        typeToKey = TypeToKeyMapping();
        keyToType = KeyToTypeMapping(typeToKey);
    }

    public BASIC_GRAMMAR getType(String key) {
        return keyToType.get(key);
    }

    private Map<BASIC_GRAMMAR, String> TypeToKeyMapping() {
        Map<BASIC_GRAMMAR, String> map = Stream.of(
                        EnumSet.allOf(BASIC_DELIMITERS.class),
                        EnumSet.allOf(BASIC_VAR.class),
                        EnumSet.allOf(COMMENT_DELIMITER.class),
                        EnumSet.allOf(COMPOSED_DELIMITER.class),
                        EnumSet.allOf(GENERAL_SCHEMA.class),
                        EnumSet.allOf(INPUT_OUTPUT.class),
                        EnumSet.allOf(RESERVED_WORDS.class),
                        EnumSet.allOf(STATEMENT_TERMINATOR.class)
                )
                .flatMap(Set::stream)
                .collect(Collectors.toMap(
                        Function.identity(),
                        BASIC_GRAMMAR::getType
                ));

        return map;
    }

    private Map<String, BASIC_GRAMMAR> KeyToTypeMapping(Map<BASIC_GRAMMAR, String> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getValue,
                        Map.Entry::getKey
                ));
    }

    public Map<BASIC_GRAMMAR, String> getTypeToKey() {
        return typeToKey;
    }

    public void setTypeToKey(Map<BASIC_GRAMMAR, String> typeToKey) {
        this.typeToKey = typeToKey;
    }

    public Map<String, BASIC_GRAMMAR> getKeyToType() {
        return keyToType;
    }

    public void setKeyToType(Map<String, BASIC_GRAMMAR> keyToType) {
        this.keyToType = keyToType;
    }
}
