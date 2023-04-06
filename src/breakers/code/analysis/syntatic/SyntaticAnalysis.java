package breakers.code.analysis.syntatic;

public class SyntaticAnalysis {
    int value = 1 + 2 * 3;
    public boolean validateSyntax () {
        return false;
    }

    public boolean validateMathExpressions(int currentPosition) {
        // A math expression must contain a valid number or variable before and after the operator
        // Exception when a number is negative e.g: -2

        // Go back until finds something that is not number or variable


        return false;
    }

    public boolean validateIfStatement () {
        return false;
    }
}
