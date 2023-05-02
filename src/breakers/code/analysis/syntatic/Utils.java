package breakers.code.analysis.syntatic;

import breakers.code.grammar.tokens.Token;
import static breakers.code.grammar.tokens.lexems.literals.BOOL_VAR.FALSE;
import static breakers.code.grammar.tokens.lexems.literals.BOOL_VAR.TRUE;

public class Utils {

    public Utils() {

    }

    public boolean isBoolean(Token currentToken) {
        return currentToken.getKey() == TRUE || currentToken.getKey() == FALSE;
    }


    private boolean isVariable(Token token) {
        return token.getKey().getData().equals("BASIC_VAR") || token.getKey().getData().equals("VAR_NAME");
    }

    public boolean isNumberOrVariable(String tokenValue) {
        return isNumeric(tokenValue) || tokenValue.matches("[a-zA-Z]+");
    }

    private boolean isNumeric(String tokenValue) {
        return tokenValue.matches("-?\\d+(\\.\\d+)?");
    }

    public boolean isNumber(Token token) {
        return isNumeric(token.getValue());
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

}
