package breakers.code.analysis.syntatic;

import breakers.code.grammar.tokens.Token;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SyntaticAnalysis {
    public boolean validateSyntax (List<List<Token>> lines) {

        for (List<Token> line : lines) {
            line.stream().forEach(token -> {
                if(token.getKey().getData() == "BASIC_VAR"){
                    //validateBasicVar()
                }
                if(token.getKey().getData() == "PARENTHESES"){
                    //validateLogicParentheses()
                }
            });

            //[ {BASIC_VAR, const }, {BRACKET, "{" }, {BASIC_VAR, "int"}], [{FUNC_TYPE, max}], [{NUMBER, 100}, {DELIMITER, ";"}, ]
            // verify if the next token after a basic_var is null, if it is null, identify it has a VAR_NAME
            // after verify if the name is correct
            // validations will take place here
        }

        return false;
    }

    //TODO: Variable and constant definitions validation
    //variable declared in a const block, i.e. const{}
    public boolean validateConstantDefinition(List<Token> line) {

        return false;
    }

    //different from variable definition
    public boolean validateVariableDefinition(List<Token> line) {
        return false;
    }


    //TODO: validate funtion declaration
    //name | parameters | return type
    //the function code block dealt with elswere, this is just function declaration
    public boolean validateFunctionDeclaration(List<Token> line) {
        return false;
    }


    //square_root, pow, gen
    public boolean validateSpecialInternalFunctions(int currentPosition) {
        return false;
    }

    //TODO: validate flow Control
    public boolean validateIfStatement(List<Token> line) {
        return false;
    }

    public boolean validateWhileStatement(List<Token> line) {
        return false;
    }

    public boolean validateForStatement(List<Token> line) {
        return false;
    }

    //the variable, constant, function has been declared before being used?
    public boolean hasBeenDeclared(List<Token> line) {
        return false;
    }

    //validate if the function arguments are correct and in the correct order
    public boolean validateFunctionArguments(){
        return false;
    }

    //validafe if all statments end with a semicolon
    public boolean validateStatementsEndWithSemicolon(List<Token> line) {
        return false;
    }

    //TODO: Validate IO funtions
    //validate the use of the read | read_all |read_string command
    public boolean validateIORead(List<Token> line) {
        return false;
    }

    //validate the use of the write | write_all |write_string command
    public boolean validateIOWrite(List<Token> line) {
        return false;
    }


    //TODO: Think of variables with members x.t


    //TODO MATH expressions should also follow precedence rules
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
    public boolean validateNumberOrVariableBeforeAfterSymbol(List<Token> line) {
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
            List<Token> line
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
    public boolean validateLogicParentheses(List<Token> line) {
        return false;
    }


    /*
    * Validate if the var name is syntactically correct
    * that is, starts with a lietter (upper or lower case) and contains only letters, numbers and underscores
    * */
    public boolean validateVariableName(Token variableName) {
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
