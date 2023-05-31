package breakers.code.tac;


public class IOThreeAddressCode extends ThreeAddressCode {
    public IOThreeAddressCode(String operator, String operand1) {
        super(operator, operand1, "", "");
    }

    @Override
    public String toString() {
        if(getOperator()=="print"){
            StringBuilder sb = new StringBuilder();
            sb.append(getOperator()).append(" ").append(getOperand1());
            return sb.toString();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(getOperand1()).append(" = ").append(getOperator());
        return sb.toString();
    }
}