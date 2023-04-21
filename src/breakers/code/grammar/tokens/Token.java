package breakers.code.grammar.tokens;


import breakers.code.grammar.tokens.lexems.BASIC_GRAMMAR;

/*
* Object that stores the type of the token and its value such as "BASIC_VALUE", "int"
* */
public class Token {
    private BASIC_GRAMMAR key;
    private String value;
    private TYPES type;

    public Token(BASIC_GRAMMAR k, String v){
        this.key = k;
        this.value = v;
    }

    public Token(BASIC_GRAMMAR key, String value, TYPES type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public BASIC_GRAMMAR getKey() {
        return key;
    }

    public void setKey(BASIC_GRAMMAR key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TYPES getType() {
        return type;
    }

    public void setType(TYPES type) {
        this.type = type;
    }

    public void setTypeAndKey(TYPES type, BASIC_GRAMMAR key){
        this.type = type;
        this.key = key;
    }
}