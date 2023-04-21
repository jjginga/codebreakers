package breakers.code.grammar.tokens;

import breakers.code.grammar.tokens.lexems.*;
import breakers.code.grammar.tokens.lexems.delimiters.COMPOSED_OPERATORS;
import breakers.code.grammar.tokens.lexems.delimiters.OPERATORS;
import breakers.code.grammar.tokens.lexems.delimiters.PARENTHESIS;
import breakers.code.grammar.tokens.lexems.delimiters.PONCTUATION;

import java.util.EnumSet;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AssignType {

    public Map<String, TYPES> getKeyToType() {
        return keyToType;
    }

    private final Map<String, TYPES> keyToType;
    private Map<BASIC_GRAMMAR, String> dataToKey;



    private Map<String, BASIC_GRAMMAR> keyToData;

    public AssignType() {
        dataToKey = dataToKeyMapping();
        keyToData = keyToDataMapping(dataToKey);
        keyToType = keyToTypeMapping();
    }

    private Map<BASIC_GRAMMAR, String> dataToKeyMapping() {
            return Stream.of(
                        EnumSet.allOf(OPERATORS.class),
                        EnumSet.allOf(PARENTHESIS.class),
                        EnumSet.allOf(PONCTUATION.class),
                        EnumSet.allOf(COMPOSED_OPERATORS.class),
                        EnumSet.allOf(COMMENT_DELIMITER.class),
                        EnumSet.allOf(GENERAL_SCHEMA.class),
                        EnumSet.allOf(INPUT_OUTPUT.class),
                        EnumSet.allOf(BASIC_VAR.class),
                        EnumSet.allOf(RESERVED_WORDS.class),
                        EnumSet.allOf(INTERNAL_FUNCTIONS.class),
                        EnumSet.allOf(STATEMENT_TERMINATOR.class)
                )
                .flatMap(Set::stream)
                .collect(Collectors.toMap(
                        Function.identity(),
                        BASIC_GRAMMAR::getData
                ));
    }
    private Map<String, TYPES> keyToTypeMapping() {
        return Stream.of(
                        BASIC_VAR.values(),
                        RESERVED_WORDS.values(),
                        INTERNAL_FUNCTIONS.values(),
                        STATEMENT_TERMINATOR.values(),
                        OPERATORS.values(),
                        PARENTHESIS.values(),
                        PONCTUATION.values(),
                        COMPOSED_OPERATORS.values(),
                        GENERAL_SCHEMA.values(),
                        INPUT_OUTPUT.values()
                )
                .flatMap(Stream::of)
                .collect(Collectors.toMap(
                        BASIC_GRAMMAR::getData,
                        BASIC_GRAMMAR::getType
                ));
    }
    private Map<String, BASIC_GRAMMAR> keyToDataMapping(Map<BASIC_GRAMMAR, String> map) {

        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getValue(),
                        entry -> entry.getKey()
                ));
    }

    public Map<BASIC_GRAMMAR, String> getDataToKey() {
        return dataToKey;
    }

    public void setDataToKey(Map<BASIC_GRAMMAR, String> dataToKey) {
        this.dataToKey = dataToKey;
    }

    public Map<String, BASIC_GRAMMAR> getKeyToData() {
        return keyToData;
    }

    public void setKeyToData(Map<String, BASIC_GRAMMAR> keyToData) {
        this.keyToData = keyToData;
    }
}
