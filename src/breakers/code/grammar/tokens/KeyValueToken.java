package breakers.code.grammar.tokens;


/*
* Object that stores the type of the token and its value such as "BASIC_VALUE", "int"
* */
public class KeyValueToken {
    private String key;
    private String value;

    public KeyValueToken(String k, String v){
        this.key = k;
        this.value = v;
    }
}
