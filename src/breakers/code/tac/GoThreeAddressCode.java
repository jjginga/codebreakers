package breakers.code.tac;

public class GoThreeAddressCode extends ThreeAddressCode {
    public GoThreeAddressCode(String operator, String operand1) {
        super(operator, operand1, "", "");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getOperator()).append(" ").append(getOperand1());
        return sb.toString();
    }
}