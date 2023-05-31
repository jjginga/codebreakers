package breakers.code.tac;

public class IfThreeAddressCode extends ThreeAddressCode {
    public IfThreeAddressCode(String operator, String operand1, String operand2, String result) {
        super(operator, operand1, operand2, result);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("if ").append(getOperand1()).append(" ").append(getOperator()).append(" ").append(getResult());
        return sb.toString();
    }
}