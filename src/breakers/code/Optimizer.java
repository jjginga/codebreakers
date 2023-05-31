package breakers.code;

import breakers.code.tac.*;

import java.util.*;


public class Optimizer {


    private List<ThreeAddressCode> tacList;

    public void setTacList(List<ThreeAddressCode> tacList) {
        this.tacList = tacList;
    }

    private void constantPropagation() {
        Map<String, String> constMap = new HashMap<>();
        List<ThreeAddressCode> newTacList = new ArrayList<>();

        for (ThreeAddressCode tac : tacList) {
            if(isSpecialTac(tac)) {
                newTacList.add(tac);
                continue;
            }

            String operator = tac.getOperator();

            if (operator.equals("=") && isConstant(tac.getOperand1()) && tac.getOperand2().isEmpty()) {
                // If the operation is an assignment and the right-hand side is a constant
                constMap.put(tac.getResult(), tac.getOperand1());
            } else {
                // Otherwise, replace operands with their constant values if available
                if (constMap.containsKey(tac.getOperand1())) {
                    tac.setOperand1(constMap.get(tac.getOperand1()));
                    constMap.remove(tac.getResult());
                }
                if (constMap.containsKey(tac.getOperand2())) {
                    tac.setOperand2(constMap.get(tac.getOperand2()));
                    constMap.remove(tac.getResult());
                }
                newTacList.add(tac);
            }
        }

        tacList = newTacList;
    }

    private void simplifyIOOperations() {
        for (int i = 0; i < tacList.size() - 1; i++) {
            ThreeAddressCode tac = tacList.get(i);
            ThreeAddressCode nextTac = tacList.get(i + 1);

            if (tac.getOperator().equals("=") && nextTac instanceof IOThreeAddressCode && nextTac.getOperand1().equals(tac.getResult())) {
                nextTac.setOperand1(tac.getOperand1());
                tacList.remove(i);
                i--;  // Decrement index to account for the removed item
            } else if (nextTac.getOperator().equals("=") && tac instanceof IOThreeAddressCode && nextTac.getOperand1().equals(tac.getOperand1())) {
                tac.setOperand1(nextTac.getResult());
                tacList.remove(i+1);
                i--;  // Decrement index to account for the removed item
            }
        }
    }

    private void optimizePrints() {
        List<ThreeAddressCode> optimizedTacList = new ArrayList<>();
        ThreeAddressCode currentPrint = null;
        String currentPrintString = "";

        for (ThreeAddressCode tac : tacList) {
            if (tac.getOperator().equals("print")) {
                if (currentPrint == null) {
                    currentPrint = tac;
                    currentPrintString = tac.getOperand1();
                } else {
                    if (tac.getOperand1().equals("\"\\n\"")) {
                        currentPrintString += " + " + tac.getOperand1();
                        currentPrint.setOperand1(currentPrintString);
                        optimizedTacList.add(currentPrint);
                        currentPrint = null;
                        currentPrintString = "";
                    } else {
                        currentPrintString += " + " + tac.getOperand1();
                    }
                }
            } else {
                if (currentPrint != null) {
                    currentPrint.setOperand1(currentPrintString);
                    optimizedTacList.add(currentPrint);
                    currentPrint = null;
                    currentPrintString = "";
                }
                optimizedTacList.add(tac);
            }
        }

        if (currentPrint != null) {
            currentPrint.setOperand1(currentPrintString);
            optimizedTacList.add(currentPrint);
        }

        tacList = optimizedTacList;
    }
    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private static boolean isBoolean(String str) {
        return str.matches("true|false");
    }

    private static boolean isConstant(String str) {
        return isNumeric(str) || isBoolean(str);
    }

    private static boolean isSpecialTac(ThreeAddressCode tac) {
        return tac instanceof IOThreeAddressCode || tac instanceof MOThreeAddressCode || tac instanceof LabelThreeAddressCode
                || tac instanceof IfThreeAddressCode || tac instanceof GoThreeAddressCode || tac instanceof BooleanThreeAddressCode;
    }


    // Executa todas as otimizações

    public List<ThreeAddressCode> optimize() {
        simplifyIOOperations();
        constantPropagation();
        optimizePrints();
        return tacList;
    }
}

