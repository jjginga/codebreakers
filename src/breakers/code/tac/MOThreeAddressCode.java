package breakers.code.tac;

public class MOThreeAddressCode extends ThreeAddressCode{

    public MOThreeAddressCode(String operator, String operand1, String operand2, String result) {
        super(operator, operand1, operand2, result);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getResult()).append(" = ").append(getOperand1()).append(" ").append(getOperator()).append(" ").append(getOperand2());
        return sb.toString();
    }

}
