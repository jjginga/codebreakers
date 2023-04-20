package breakers.code.analysis.syntatic;

import breakers.code.grammar.tokens.KeyValueToken;
import breakers.code.grammar.tokens.Token;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SyntaticAnalysis {
    public boolean validateSyntax (List<List<KeyValueToken>> lines) {

        for (List<KeyValueToken> line : lines) {
            line.stream().forEach(keyValueToken -> {
                if(keyValueToken.getKey().getType() == "BASIC_VAR"){
                    //validateBasicVar()
                }
                if(keyValueToken.getKey().getType() == "PARENTHESES"){
                    validateLogicParentheses()
                }
            });

            //[ {BASIC_VAR, const }, {BRACKET, "{" }, {BASIC_VAR, "int"}], [{FUNC_TYPE, max}], [{NUMBER, 100}, {DELIMITER, ";"}, ]
            // verify if the next token after a basic_var is null, if it is null, identify it has a VAR_NAME
            // after verify if the name is correct
            // validations will take place here
        }

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
    * This validation should be a part of the math expression validation
    * */
    public boolean validateNumberOrVariableBeforeAfterSymbol(List<KeyValueToken> line) {
        // TODO -> use enum instead to enumerate the math symbols and have them in the grammar
        List<String> mathSymbols = new ArrayList<>(List.of("+", "-", "/", "*"));

        List<Integer> positions = findPositionsOfMathSymbols(mathSymbols, line);

        if(positions.size() > 0) {
            positions.stream().forEach(index -> {
                String tokenBeforeSymbol = line.get(index - 1).getValue();
                String tokenAfterSymbol = line.get(index + 1).getValue();

                // TODO -> check if it's variable or number before or after the symbol
            });
        }
        return false;
    }

    private List<Integer> findPositionsOfMathSymbols (
            List<String> mathSymbols,
            List<KeyValueToken> line
    ){
        List<Integer> mathSymbolsPositions = new ArrayList<>();
        for (String mathSymbol : mathSymbols) {
            Integer index = line.indexOf(mathSymbol);

            mathSymbolsPositions.add(index);
        }

        return mathSymbolsPositions;
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
