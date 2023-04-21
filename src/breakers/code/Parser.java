package breakers.code;

import breakers.code.grammar.tokens.Token;

import java.util.*;

public class Parser {


    private List<Token> tokenMapListLine;
    private List<List<Token>> lines;

    // Each line ends with ";"
    // [
    //  [] -> Line 1
    //  [] -> line 2
    // ]


    // E.g: int a=12; bool b =a==3; intc = 1+2;
    // [
    //  [(BASIC_VAR, "int"), (BASIC_SYMBOL, " "), ("VAR_NAME", "a"), ("BASIC_SYMBOL", "="), ("NUMBER", "12"), ("BASIC_SYMBOL", ";")] -> Line 1
    //  [(BASIC_VAR, "bool"), (BASIC_SYMBOL, " "), ("VAR_NAME", "b"), ("BASIC_SYMBOL", "="), ("VAR_NAME", "a"), ("BASIC_SYMBOL", "=="), ("NUMBER", "3"), ("BASIC_SYMBOL", ";")] -> line 2
    //  [(BASIC_VAR, "int"), ("VAR_NAME", "c"), (BASIC_SYMBOL, " "), ("BASIC_SYMBOL", "="), (BASIC_SYMBOL, " "), ("NUMBER", "1"), ("BASIC_SYMBOL", "+"), ("NUMBER", "2"), ("BASIC_SYMBOL", ";")] -> line 3 --> INVALID due to no spaces in between "int" and "c"
    // ]


    private Integer index = 0;

    public Parser() {
    }

    public List<List<Token>> parse(List<List<Token>> lines) {
        return null;
    }

}