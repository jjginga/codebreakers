package breakers.code;

import breakers.code.tac.*;

import java.util.List;

public class FinalCodeGenerator {

    private int labelCount = 1;

    public String generateFinalCode(List<ThreeAddressCode> optimizedCode) {
        StringBuilder finalCode = new StringBuilder();

        for (ThreeAddressCode code : optimizedCode) {
            String operation = code.getOperator();
            String arg1 = code.getOperand1();
            String arg2 = code.getOperand2();
            String result = code.getResult();

            if (code instanceof MOThreeAddressCode) {
                finalCode.append(getMOInstruction(operation)).append(" ").append(result).append(", ").append(arg1).append("\n");
            } else if (code instanceof GoThreeAddressCode) {
                finalCode.append(getGoInstruction(operation)).append(" ").append(result).append("\n");
            } else if (code instanceof IfThreeAddressCode) {
                finalCode.append(getIfInstruction(operation, arg1, arg2)).append(" L").append(labelCount).append("\n");
                finalCode.append("JMP L").append(labelCount + 1).append("\n");
                finalCode.append("L").append(labelCount).append(":\n");
                labelCount++;
            } else if (code instanceof IOThreeAddressCode) {
                finalCode.append(getIOInstruction(operation, arg1)).append("\n");
            } else if (code instanceof LabelThreeAddressCode) {
                finalCode.append(result).append(":\n");
                labelCount++;
            } else {
                finalCode.append(getGenericInstruction(result, arg1)).append("\n");
            }
        }

        return finalCode.toString();
    }

    private String getMOInstruction(String operation) {
        switch (operation) {
            case "=":
                return "MOV";
            case "+":
                return "ADD";
            case "-":
                return "SUB";
            case "*":
                return "MUL";
            case "/":
                return "DIV";
            default:
                return "";
        }
    }

    private String getGoInstruction(String operation) {
        switch (operation) {
            case "goto":
                return "JMP";
            default:
                return "";
        }
    }

    private String getIfInstruction(String operation, String arg1, String arg2) {
        switch (operation) {
            case "if":
                return "CMP " + arg1 + ", " + arg2 + "\nJNZ";
            default:
                return "";
        }
    }

    private String getIOInstruction(String operation, String arg1) {
        switch (operation) {
            case "print":
                return "PRINT " + arg1;
            case "read":
                return "READ " + arg1;
            default:
                return "";
        }
    }

    private String getGenericInstruction(String result, String arg1) {
        return "MOV " + result + ", " + arg1;
    }
}