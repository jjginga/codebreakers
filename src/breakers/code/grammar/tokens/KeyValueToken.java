package breakers.code.grammar.tokens;


/*
* Object that stores the type of the token and its value such as "BASIC_VALUE", "int"
* */
public class KeyValueToken {
    private BASIC_GRAMMAR key;
    private String value;

    public KeyValueToken(BASIC_GRAMMAR k, String v){
        this.key = k;
        this.value = v;
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

}