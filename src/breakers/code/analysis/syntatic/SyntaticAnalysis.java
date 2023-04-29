package breakers.code.analysis.syntatic;

import breakers.code.grammar.tokens.TYPES;
import breakers.code.grammar.tokens.Token;
import breakers.code.grammar.tokens.lexems.delimiters.PARENTHESIS;
import breakers.code.grammar.tokens.lexems.identifiers.VAR_NAMES;

import java.util.*;

import static breakers.code.grammar.tokens.lexems.delimiters.PARENTHESIS.LPAREN;
import static breakers.code.grammar.tokens.lexems.delimiters.PARENTHESIS.RPAREN;

public class SyntaticAnalysis {
    public boolean validateSyntax(List<List<Token>> lines) {
        boolean isValid = true;
        for (int currentLinePosition = 0; currentLinePosition < lines.size(); currentLinePosition++) {
            List<Token> line = lines.get(currentLinePosition);
            for (int currentTokenPosition = 0; currentTokenPosition < line.size(); currentTokenPosition++) {
                Token token = line.get(currentTokenPosition);

                if (token.getKey() == PARENTHESIS.LPAREN || token.getKey() == PARENTHESIS.RPAREN) {
                    ValidationResult validationResult = validateLogicParentheses(line);
                    if (!validationResult.isValid()) {
                        int currentLineToPrint = currentLinePosition + 1;
                        int currentErrorPositionToPrint = validationResult.getErrorPosition() + 1;
                        System.out.println("Invalid syntax at line " + currentLineToPrint + " at position " + currentErrorPositionToPrint);
                    }
                }

                if (token.getKey() == VAR_NAMES.VAR_NAME) {
                    boolean isVariableNameValid = validateVariableName(token);

                    int currentLineToPrint = currentLinePosition + 1;
                    int currentErrorPositionToPrint = currentTokenPosition + 1;

                    if (!isVariableNameValid) {
                        System.out.println("Invalid syntax at line " + currentLineToPrint + " at position " + currentErrorPositionToPrint);
                    }
                }
            }



            //[ {BASIC_VAR, const }, {BRACKET, "{" }, {BASIC_VAR, "int"}], [{FUNC_TYPE, max}], [{NUMBER, 100}, {DELIMITER, ";"}, ]
            // verify if the next token after a basic_var is null, if it is null, identify it has a VAR_NAME
            // after verify if the name is correct
            // validations will take place here
        }

        return false;
    }

    public class ValidationResult {
        private boolean isValid;
        private int errorPosition;

        public ValidationResult(boolean isValid, int errorPosition) {
            this.isValid = isValid;
            this.errorPosition = errorPosition;
        }

        public boolean isValid() {
            return isValid;
        }

        public int getErrorPosition() {
            return errorPosition;
        }
    }

    //TODO: Variable and constant definitions validation
    //variable declared in a const block, i.e. const{}
    public boolean validateConstantDefinition(List<Token> line) {
        // Return false if the line does not have enough tokens
        if (line.size() < 2) {
            return false;
        }

        boolean isConstantDefinition = false;

        // Check if the first token is a const keyword
        if (line.get(0).getValue().equals("const")) {
            isConstantDefinition = true;

            // Check if all tokens are valid constants
            for (int i = 1; i < line.size(); i++) {
                Token currentToken = line.get(i);

                // If the current token is a comma, continue to the next token
                if (currentToken.getKey().getData().equals("DELIMITER") && currentToken.getValue().equals(",")) {
                    continue;
                }

                // If the current token is not a number, return false
                if (!currentToken.getKey().getData().equals("NUMBER")) {
                    isConstantDefinition = false;
                    break;
                }
            }
        }

        return isConstantDefinition;
    }

    //different from  definition
    public boolean validateVariableDefinition(List<Token> line) {
        boolean isVariableDefinition = false;
        //check if the first token is a basic_var
        if (line.size() > 0 && line.get(0).getKey().getData().equals("BASIC_VAR")) {
            isVariableDefinition = true;

            //check if all tokens are valid variables
            for (int i = 1; i < line.size(); i++) {
                Token currentToken = line.get(i);

                //if the current token is a comma, continue to the next token
                if (currentToken.getKey().getData().equals("DELIMITER") && currentToken.getValue().equals(",")) {
                    continue;
                }

                //if the current token is not a number or a valid variable name, return false
                if (!currentToken.getKey().getData().equals("NUMBER") && !validateVariableName(currentToken)) {
                    isVariableDefinition = false;
                    break;
                }
            }
        }

        return isVariableDefinition;
    }


    //TODO: validate function declaration
    //name | parameters | return type
    //the function code block dealt with elswere, this is just function declaration
    //verify if a function declaration is valid and exists

    public boolean validateFunctionDeclaration(List<Token> line) {
        return false;
    }


    //square_root, pow, gen
    public boolean validateSpecialInternalFunctions(int currentPosition) {
        return false;
    }

    //TODO: validate flow Control
    public ValidationResult validateIfStatement(List<Token> line) {
        int position = 0;
        boolean ifKeywordFound = false;
        boolean openParenthesesFound = false;
        boolean closeParenthesesFound = false;
        boolean conditionFound = false;
        boolean openingBraceFound = false;
        boolean closingBraceFound = false;
        boolean elseKeywordFound = false;

        for (Token token : line) {
            if (token.getKey().getData().equals("IF") && token.getValue().equals("if")) {
                ifKeywordFound = true;
                position++;
                continue;
            }

            if (ifKeywordFound && !openParenthesesFound && token.getKey().getData().equals("PARENTHESES") && token.getValue().equals("(")) {
                openParenthesesFound = true;
                position++;
                continue;
            }

            // Verificar a presença de uma condição válida
            if (openParenthesesFound && !closeParenthesesFound) {

                position++;
                continue;
            }

            if (openParenthesesFound && !closeParenthesesFound && token.getKey().getData().equals("PARENTHESES") && token.getValue().equals(")")) {
                closeParenthesesFound = true;
                position++;
                continue;
            }

            if (closeParenthesesFound && !openingBraceFound && token.getKey().getData().equals("BRACKET") && token.getValue().equals("{")) {
                openingBraceFound = true;
                position++;
                continue;
            }

            if (openingBraceFound && !closingBraceFound && token.getKey().getData().equals("BRACKET") && token.getValue().equals("}")) {
                closingBraceFound = true;
                position++;
                continue;
            }

            if (closingBraceFound && token.getKey().getData().equals("ELSE") && token.getValue().equals("else")) {
                elseKeywordFound = true;
                position++;
                continue;
            }

            if (elseKeywordFound && token.getKey().getData().equals("BRACKET") && token.getValue().equals("{")) {

            }

            position++;
        }

        if (ifKeywordFound && openParenthesesFound && closeParenthesesFound && conditionFound && openingBraceFound && closingBraceFound) {
            return new ValidationResult(true, position);
        }

        return new ValidationResult(false, position);
    }

    public ValidationResult validateForStatement(List<Token> line) {
        int position = 0;
        boolean forKeywordFound = false;
        boolean openParenthesesFound = false;
        boolean closeParenthesesFound = false;
        boolean semicolonFound = false;
        int semicolonCount = 0;
        int parameterCount = 0;

        for (Token token : line) {
            if (token.getKey().getData().equals("FOR") && token.getValue().equals("for")) {
                forKeywordFound = true;
                position++;
                continue;
            }

            if (forKeywordFound && !openParenthesesFound && token.getKey().getData().equals("PARENTHESES") && token.getValue().equals("(")) {
                openParenthesesFound = true;
                position++;
                continue;
            }

            if (openParenthesesFound && !closeParenthesesFound) {
                if (token.getKey().getData().equals("PARENTHESES") && token.getValue().equals(")")) {
                    closeParenthesesFound = true;
                    position++;
                    continue;
                }

                if (token.getKey().getData().equals("DELIMITER") && token.getValue().equals(";")) {
                    semicolonFound = true;
                    semicolonCount++;
                    position++;
                    continue;
                }

                if (isVariable(token) || token.getKey().getData().equals("NUMBER")) {
                    parameterCount++;
                    position++;
                    continue;
                }
            }

            if (closeParenthesesFound && token.getKey().getData().equals("BRACKET") && token.getValue().equals("{")) {
                if (semicolonCount == 2 && parameterCount == 4) {
                    return new ValidationResult(true, position);
                } else {
                    return new ValidationResult(false, position);
                }
            }
            position++;
        }

        return new ValidationResult(false, position);
    }

    private boolean isVariable(Token token) {
        return token.getKey().getData().equals("BASIC_VAR") || token.getKey().getData().equals("VAR_NAME");
    }

    public ValidationResult validateWhileStatement(List<Token> line) {
        int position = 0;
        boolean whileKeywordFound = false;
        boolean openParenthesesFound = false;
        boolean closeParenthesesFound = false;

        for (Token token : line) {
            if (token.getKey().getData().equals("WHILE") && token.getValue().equals("while")) {
                whileKeywordFound = true;
                position++;
                continue;
            }

            if (whileKeywordFound && !openParenthesesFound && token.getKey().getData().equals("PARENTHESES") && token.getValue().equals("(")) {
                openParenthesesFound = true;
                position++;
                continue;
            }

            if (openParenthesesFound && !closeParenthesesFound && token.getKey().getData().equals("PARENTHESES") && token.getValue().equals(")")) {
                closeParenthesesFound = true;
                position++;
                continue;
            }

            if (closeParenthesesFound && token.getKey().getData().equals("BRACKET") && token.getValue().equals("{")) {
                return new ValidationResult(true, position);
            }
            position++;
        }

        return new ValidationResult(false, position);
    }


    //the variable, constant, function has been declared before being used?
    public boolean hasBeenDeclared(List<Token> line) {
        return false;
    }

    //validate if the function arguments are correct and in the correct order
    public boolean validateFunctionArguments() {
        return false;
    }

    //validate if all statments end with a semicolon
    public boolean validateStatementsEndWithSemicolon(List<Token> line) {
        // Verifica se a linha está vazia
        if (line.isEmpty()) {
            return true;
        }

        // Verifica se o último token é um ponto e vírgula
        Token lastToken = line.get(line.size() - 1);
        return lastToken.getKey().getData().equals("DELIMITER") && lastToken.getValue().equals(";");
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
    public boolean validateMathExpressions(List<Token> line) {
        ValidationResult parenthesesValidation = validateLogicParentheses(line);
        if (!parenthesesValidation.isValid()) {
            // Tratar erro de parênteses, se necessário
            return false;
        }

        boolean numberOrVariableValidation = validateNumberOrVariableBeforeAfterSymbol(line);
        return numberOrVariableValidation;
    }

    /*
     * This validation should be a part of the math expression validation
     * */
    public boolean validateNumberOrVariableBeforeAfterSymbol(List<Token> line) {
        List<String> mathSymbols = new ArrayList<>(List.of("+", "-", "/", "*"));

        List<Integer> positions = findPositionsOfMathSymbols(mathSymbols, line);

        if (positions.size() > 0) {
            for (int index : positions) {
                String tokenBeforeSymbol = line.get(index - 1).getValue();
                String tokenAfterSymbol = line.get(index + 1).getValue();

                if (!isNumberOrVariable(tokenBeforeSymbol) || !isNumberOrVariable(tokenAfterSymbol)) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Integer> findPositionsOfMathSymbols(List<String> mathSymbols, List<Token> line) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < line.size(); i++) {
            if (mathSymbols.contains(line.get(i).getValue())) {
                positions.add(i);
            }
        }
        return positions;
    }
    public boolean isNumberOrVariable(String tokenValue) {
        return tokenValue.matches("-?\\d+(\\.\\d+)?") || tokenValue.matches("[a-zA-Z]+");
    }


    /*
     * Validate if a line that contains parentheses has it's syntax correct according to "parentheses rules"
     * e.g: if it has one opening and one closing parentheses at the same line
     * */

    /*
     * param -> line containing tokens: e.g: [(BASIC_VAR, "int"), ("VAR_NAME", "a"), ("BASIC_SYMBOL", "="), ("NUMBER", "12"), ("BASIC_SYMBOL", ";")]
     * */

    public ValidationResult validateLogicParentheses(List<Token> line) {
        LinkedList<Token> stack = new LinkedList<>();
        int position = 0;
        int openParenthesesPosition = -1;
        for (Token token : line) {
            if (token.getType() != null && token.getType().equals(TYPES.PARENTHESIS) && token.getValue() != null && token.getValue().equals(LPAREN.getData())) {
                stack.push(token);
                openParenthesesPosition = position;
            }
            if (token.getType() != null && token.getType().equals(TYPES.PARENTHESIS) && token.getValue() != null  && token.getValue().equals(RPAREN.getData())) {
                if (stack.isEmpty()) {
                    return new ValidationResult(false, position);
                }
                stack.pop();
                openParenthesesPosition = stack.isEmpty() ? -1 : openParenthesesPosition;
            }
            position++;
        }
        if (!stack.isEmpty()) {
            return new ValidationResult(false, openParenthesesPosition);
        }
        return new ValidationResult(true, -1);
    }

    /*
     *
     * Os nomes das variáveis são case-sensitive (x diferente de X), começam sempre com uma letra, maiúscula ou minúscula,
     * seguida de zero ou mais letras, dígitos ou ‘_’ (underscore).
     * */
    public boolean validateVariableName(Token variableName) {
        String name = variableName.getValue();
        if (name == null || name.isEmpty()) {
            return false;
        }
        char firstChar = name.charAt(0);
        if (!Character.isLetter(firstChar)) {
            return false;
        }
        for (char c : name.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '_') {
                return false;
            }
        }
        return true;
    }


    public boolean validateLogicMathSymbols() {
        return false;
    }

    public boolean validateGreaterThan(List<Token> Line) {
        int position = 0;
        boolean foundGreaterThanSymbol = false;
        Token previousToken = null;
        for (Token token : Line) {
            if (token.getKey().getData().equals("BASIC_SYMBOL") && token.getValue().equals(">")) {
                if (previousToken != null && (previousToken.getKey().getData().equals("BASIC_VAR") || previousToken.getKey().getData().equals("NUMBER"))) {
                    foundGreaterThanSymbol = true;
                    position++;
                    continue;
                } else {
                    return false;
                }
            }
            if (foundGreaterThanSymbol) {
                return token.getKey().getData().equals("BASIC_VAR") || token.getKey().getData().equals("NUMBER");
            }
            position++;
            previousToken = token;
        }
        return false;
    }

    public boolean validateGreaterOrEqualThan(List<Token> line) {
        int position = 0;
        boolean foundGreaterOrEqualThanSymbol = false;
        Token previousToken = null;

        for (Token token : line) {
            if (token.getKey().getData().equals("BASIC_SYMBOL") && token.getValue().equals(">=")) {
                if (previousToken != null && (previousToken.getKey().getData().equals("BASIC_VAR") || previousToken.getKey().getData().equals("NUMBER"))) {
                    foundGreaterOrEqualThanSymbol = true;
                    position++;
                    continue;
                } else {
                    return false;
                }
            }

            if (foundGreaterOrEqualThanSymbol) {
                return token.getKey().getData().equals("BASIC_VAR") || token.getKey().getData().equals("NUMBER");
            }

            position++;
            previousToken = token;
        }
        return false;
    }


    public boolean validateLessThan(List<Token> line) {
        int position = 0;
        boolean foundLessThanSymbol = false;
        Token previousToken = null;

        for (Token token : line) {
            if (token.getKey().getData().equals("BASIC_SYMBOL") && token.getValue().equals("<")) {
                if (previousToken != null && (previousToken.getKey().getData().equals("BASIC_VAR") || previousToken.getKey().getData().equals("NUMBER"))) {
                    foundLessThanSymbol = true;
                    position++;
                    continue;
                } else {
                    return false;
                }
            }

            if (foundLessThanSymbol) {
                if (token.getKey().getData().equals("BASIC_VAR") || token.getKey().getData().equals("NUMBER")) {
                    return true;
                }
                return false;
            }

            position++;
            previousToken = token;
        }
        return false;
    }


    public boolean validateLessOrEqualThan(List<Token> line) {
        int position = 0;
        boolean foundLessOrEqualThanSymbol = false;
        Token previousToken = null;

        for (Token token : line) {
            if (token.getKey().getData().equals("BASIC_SYMBOL") && token.getValue().equals("<=")) {
                if (previousToken != null && (previousToken.getKey().getData().equals("BASIC_VAR") || previousToken.getKey().getData().equals("NUMBER"))) {
                    foundLessOrEqualThanSymbol = true;
                    position++;
                    continue;
                } else {
                    return false;
                }
            }

            if (foundLessOrEqualThanSymbol) {
                if (token.getKey().getData().equals("BASIC_VAR") || token.getKey().getData().equals("NUMBER")) {
                    return true;
                }
                return false;
            }

            position++;
            previousToken = token;
        }
        return false;
    }

    public boolean validateEqual(List<Token> line) {
        int position = 0;
        boolean foundEqualSymbol = false;
        Token previousToken = null;

        for (Token token : line) {
            if (token.getKey().getData().equals("BASIC_SYMBOL") && token.getValue().equals("==")) {
                if (previousToken != null && (previousToken.getKey().getData().equals("BASIC_VAR") || previousToken.getKey().getData().equals("NUMBER"))) {
                    foundEqualSymbol = true;
                    position++;
                    continue;
                } else {
                    return false;
                }
            }

            if (foundEqualSymbol) {
                if (token.getKey().getData().equals("BASIC_VAR") || token.getKey().getData().equals("NUMBER")) {
                    return true;
                }
                return false;
            }

            position++;
            previousToken = token;
        }
        return false;
    }

    public boolean validateNotEqual(List<Token> line) {
        int position = 0;
        boolean foundNotEqualSymbol = false;
        Token previousToken = null;

        for (Token token : line) {
            if (token.getKey().getData().equals("BASIC_SYMBOL") && token.getValue().equals("!=")) {
                if (previousToken != null && (previousToken.getKey().getData().equals("BASIC_VAR") || previousToken.getKey().getData().equals("NUMBER"))) {
                    foundNotEqualSymbol = true;
                    position++;
                    continue;
                } else {
                    return false;
                }
            }

            if (foundNotEqualSymbol) {
                if (token.getKey().getData().equals("BASIC_VAR") || token.getKey().getData().equals("NUMBER")) {
                    return true;
                }
                return false;
            }

            position++;
            previousToken = token;
        }
        return false;
    }
}
