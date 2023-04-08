package breakers.code.analysis.syntatic;

import breakers.code.grammar.tokens.KeyValueToken;
import breakers.code.grammar.tokens.Token;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SyntaticAnalysis {
    public boolean validateSyntax () {
        return false;
    }

    public boolean validateMathExpressions(int currentPosition) {
        // A math expression must contain a valid number or variable before and after the operator
        // Exception when a number is negative e.g: -2

        // Go back until finds something that is not number or variable

        //y=x+z; -> válido
        //y=x+; -> invalido
        //2+3-> valido
        //2+
        return false;
    }


    /*
    * Validate if a line that contains parentheses has it's syntax correct according to "parentheses rules"
    * e.g: if it has one opening and one closing parentheses at the same line
    * */

    /*
    * param -> line containing tokens: e.g: [(BASIC_VAR, "int"), ("VAR_NAME", "a"), ("BASIC_SYMBOL", "="), ("NUMBER", "12"), ("BASIC_SYMBOL", ";")]
     * */
    public boolean validateLogicParentheses(List<KeyValueToken> line) {
        return false;
    }


    /*
    * Validate if the var name is syntactically correct
    * that is, starts with a lietter (upper or lower case) and contains only letters, numbers and underscores
    * */
    public boolean validateVariableName(KeyValueToken variableName) {
        return false;
    }



    public boolean validateLogicMathSymbols(){
        return false;
    }

    public boolean validateGreaterThan(){
        return false;
    }

    public boolean validateIfStatement () {
        return false;
    }
}
