package breakers.code.analysis.syntatic;

import breakers.code.grammar.tokens.Token;

import java.util.LinkedList;
import java.util.Map;

public class SyntaticAnalysis {
    public boolean validateSyntax () {
        return false;
    }

    public boolean validateMathExpressions(int currentPosition) {
        // A math expression must contain a valid number or variable before and after the operator
        // Exception when a number is negative e.g: -2

        // Go back until finds something that is not number or variable


        return false;
    }


    /*
    * Validate if a line that contains parentheses has it's syntax correct according to "parentheses rules"
    * e.g: if it has one opening and one closing parentheses at the same line
    * */

    /*
    * param -> line containing tokens: e.g: [(BASIC_VAR, "int"), ("VAR_NAME", "a"), ("BASIC_SYMBOL", "="), ("NUMBER", "12"), ("BASIC_SYMBOL", ";")]
     * */
    public boolean validateLogicParentheses() {
        return false;
    }

    public boolean validateLogicMathSymbols(){
        return false;
    }

    public boolean validateLogicBrackets(){
        return false;
    }

    public boolean validateIfStatement () {
        return false;
    }
}
