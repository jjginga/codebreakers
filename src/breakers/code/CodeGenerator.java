package breakers.code;

import breakers.code.analysis.syntatic.Node;
import breakers.code.grammar.tokens.TYPES;
import breakers.code.grammar.tokens.lexems.BASIC_GRAMMAR;
import breakers.code.tac.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static breakers.code.grammar.tokens.lexems.GENERAL_SCHEMA.*;
import static breakers.code.grammar.tokens.lexems.INPUT_OUTPUT.READ;
import static breakers.code.grammar.tokens.lexems.INPUT_OUTPUT.WRITE;
import static breakers.code.grammar.tokens.lexems.NEED_FOR_PARSING.ARGUMENTS;
import static breakers.code.grammar.tokens.lexems.NEED_FOR_PARSING.ASSIGNMENT;
import static breakers.code.grammar.tokens.lexems.RESERVED_WORDS.*;
import static breakers.code.grammar.tokens.lexems.identifiers.FUN_NAMES.FUN_NAME;

public class CodeGenerator {

    int i = 0;
    int block = 1;
    final String attr = "t%d = %s%n";
    List<ThreeAddressCode> code = new ArrayList<>();

    public CodeGenerator() {
    }

    public List<ThreeAddressCode> generateCode(Node root) throws Exception {
        for (Node child : root.getChildren())
            code.addAll(generateIntermediateCode(child));
        return code;
    }

    private List<ThreeAddressCode> generateIntermediateCode(Node node) throws Exception {
        List<ThreeAddressCode> code = new ArrayList<>();

        BASIC_GRAMMAR key = node.getToken().getKey();
        //Alternatively node.getToken().getKey(
        if (CONST.equals(key) || GLOBAL.equals(key) || LOCAL.equals(key))
            code.addAll(generateAttribution(node));
        else if (MAIN.equals(key) || FUN_NAME.equals(key))
            code.addAll(generateFunction(node));
        else if (WRITE.equals(key))
            code.addAll(generateWrite(node));
        else if (READ.equals(key))
            code.addAll(generateRead(node));
        else if (ASSIGNMENT.equals(key))
            code.addAll(generateAssignment(node));
        else if (IF.equals(key))
            code.addAll(generateIf(node));
        else if (WHILE.equals(key))
            code.addAll(generateWhile(node));
        else if (FOR.equals(key))
            code.addAll(generateFor(node));
        else if (STRUCTS.equals(key))
            code.addAll(generateStruct(node));

        else if(ARGUMENTS.equals(key))
            code.addAll(generateArguments(node));
        //else
            //System.out.println("Not implemented yet: " + key);
        return code;
    }

    private List<ThreeAddressCode> generateFunction(Node node) throws Exception {
        List<ThreeAddressCode> tac = new ArrayList<>();
        tac.add(new ThreeAddressCode("", "", "","function "+node.getToken().getValue()));
        for (Node child : node.getChildren()) {
            if (child.getToken().getKey().getType().equals(TYPES.BASIC_TYPE))
                continue;
            tac.addAll(generateIntermediateCode(child));
        }
        tac.add(new ThreeAddressCode("", "", "","end function "+ node.getToken().getValue())) ;
        return tac;
    }


    private List<ThreeAddressCode> generateAttribution(Node node) {
        List<ThreeAddressCode> tac = new ArrayList<>();

        for (Node child : node.getChildren()) {
            String name = child.getToken().getValue();
            if (child.getChildren().size() > 1) {
                //chid 0 is the type and child 1 is the value
                String value = child.getChildren().get(1).getToken().getValue();
                String tx = String.format("t%d", i++);
                tac.add(new ThreeAddressCode("=", value, "", tx));
                tac.add(new ThreeAddressCode("=", tx, "", name));
            } else {
                //I think that when there is no attribution - just deffinition nothing should be here
                //code.append(String.format("%s\n", name));
            }
        }

        return tac;
    }

    private List<ThreeAddressCode> generateWrite(Node node) {
        List<ThreeAddressCode> tac = new ArrayList<>();
        if (node.getChildren().size() > 1) {
            for (Node child : node.getChildren()) {
                String value = child.getToken().getValue();
                String tx = String.format("t%d", i++);
                tac.add(new ThreeAddressCode("=", value, "", tx));
                //in here we should consult the table to check the type of the variable
                tac.add(new IOThreeAddressCode("print", tx));
            }
        } else {
            String value = node.getChildren().get(0).getToken().getValue();
            String tx = String.format("t%d", i++);
            tac.add(new ThreeAddressCode("=", value, "", tx));
            tac.add(new IOThreeAddressCode("print", tx));

        }
        tac.add(new IOThreeAddressCode("print", "\"\\n\""));
        return tac;
    }

    private List<ThreeAddressCode> generateAssignment(Node node) {
        //is a complex operation
        if(node.getChildren().get(1).getChildren().size()>1)
            return generateComplexOperation(node);
        if(node.getChildren().get(1).getToken().getKey().equals(READ))
            return generateRead(node);
        List<ThreeAddressCode> tac = new ArrayList<>();
        String right = node.getChildren().get(1).getToken().getValue();
        String left = node.getChildren().get(0).getToken().getValue();
        String tx = String.format("t%d", i++);
        tac.add(new ThreeAddressCode("=", right, "", tx));
        tac.add(new ThreeAddressCode("=", tx, "", left));
        return tac;
    }

    private List<ThreeAddressCode> generateComplexOperation(Node node){
        List<ThreeAddressCode> tac = new ArrayList<>();

        String operation = node.getChildren().get(1).getToken().getValue();
        String left = node.getChildren().get(1).getChildren().get(0).getToken().getValue();
        String right = node.getChildren().get(1).getChildren().get(1).getToken().getValue();
        String tx1 = String.format("t%d", i++);
        String tx2 = String.format("t%d", i++);
        String result = node.getChildren().get(0).getToken().getValue();

        tac.add(new ThreeAddressCode("=", left, "", tx1));
        tac.add(new ThreeAddressCode("=", right, "", tx2));
        tac.add(new MOThreeAddressCode(operation, tx1, tx2, result));

        return tac;
    }

    //TODO: this isn't oke - special case of assignment
    private List<ThreeAddressCode> generateRead(Node node) {
        List<ThreeAddressCode> tac = new ArrayList<>();
        String tx = String.format("t%d", i++);
        tac.add(new IOThreeAddressCode("read", tx));
        String readInto = node.getChildren().get(0).getToken().getValue();
        tac.add(new ThreeAddressCode("=", tx, "", readInto));
        return tac;
    }


    private List<ThreeAddressCode> generateIf(Node node) throws Exception {
        List<ThreeAddressCode> tac = new ArrayList<>();

        // Generate code for the boolean condition
        List<ThreeAddressCode> conditions = generateBooleanExpressions(node.getChildren().get(0));
        tac.addAll(conditions);

        int block1 = block++;
        int block2 = block++;
        int block3 = block++; // if final

        // if condition goto L1
        tac.add(new IfThreeAddressCode("goto", conditions.get(conditions.size() - 1).getResult(), "", "L" + block1));
        // goto L2
        if(node.getChildren().size()>2)
            tac.add(new GoThreeAddressCode("goto", "L" + block2));
        // L1:
        tac.add(new LabelThreeAddressCode("L" + block1));

        List<ThreeAddressCode> thenBlock = new ArrayList<>();
        // Adjusted to handle "if_body" node
        for(Node child : node.getChildren().get(1).getChildren()){
            thenBlock.addAll(generateIntermediateCode(child));
        }

        tac.addAll(thenBlock);


        // Adjusted to handle "else" node, if it exists
        if (node.getChildren().size() > 2) {
            // goto L3 after if block
            tac.add(new GoThreeAddressCode("goto", "L" + block3));
            // L2:
            tac.add(new LabelThreeAddressCode("L" + block2));
            for(Node child : node.getChildren().get(2).getChildren()){
                tac.addAll(generateIntermediateCode(child));
            }
        }

        // L3:
        tac.add(new LabelThreeAddressCode("L" + block3));

        return tac;
    }
    private  List<ThreeAddressCode> generateWhile(Node node) throws Exception {
        // Inicializa um StringBuilder para construir o código intermediário.
        List<ThreeAddressCode> tac = new ArrayList<>();


        // Adiciona um rótulo de código para o início do loop while.
        int block1 = block++;
        tac.add(new LabelThreeAddressCode("L" + block1));
        // Gera código intermediário para a condição do while, que é o primeiro filho do nó while.
        List<ThreeAddressCode> conditions = generateBooleanExpressions(node.getChildren().get(0));
        tac.addAll(conditions);
        // Prepara a variável block1 para o bloco do corpo do loop.
        int block2 = block++;


        // Adiciona uma instrução if-goto que vai para o bloco do corpo do loop (block1) se a condição do while for verdadeira.
        tac.add(new IfThreeAddressCode("goto", conditions.get(conditions.size() - 1).getResult(), "", "L" + block2));


        // Adiciona uma instrução goto que vai para o próximo bloco de código se a condição do while for falsa.
        tac.add(new GoThreeAddressCode("goto", "L" + ++block));


        // Adiciona um rótulo de código para o bloco do corpo do loop, que é o corpo do while.
        tac.add(new LabelThreeAddressCode("L" + block2));


        // Gera código intermediário para o corpo do while, que é o segundo filho do nó while.
        List<ThreeAddressCode> body = generateIntermediateCode(node.getChildren().get(1));

        // Adiciona uma instrução goto que volta ao início do loop while depois que o corpo do loop foi executado.
        tac.addAll(body);
        // Retorna o código intermediário gerado como uma String.
        tac.add(new ThreeAddressCode("goto", "", "", "L" + block1));

        return tac;
    }


    private  List<ThreeAddressCode> generateFor(Node node) throws Exception {
        List<ThreeAddressCode> tac = new ArrayList<>();

        // Inicialização
        String varName = getChildValue(node.getChildren().get(0));
        String initValue = getChildValue(node.getChildren().get(1));

        String tx = String.format("t%d", i++);
        tac.add(new ThreeAddressCode("=", initValue, "", tx));
        tac.add(new ThreeAddressCode("=", tx, "", varName));

        // Condição
        String endValue = getChildValue(node.getChildren().get(2));
        String increment = getChildValue(node.getChildren().get(3));

        // If is a var we shout consut the table
        String booleanOperator = Integer.parseInt(increment) > 0 ? "<" : ">";

        int block1 = block++;
        tac.add(new LabelThreeAddressCode("L" + block1));

        tx = String.format("t%d", i++);
        tac.add(new BooleanThreeAddressCode(booleanOperator, varName, endValue, tx));
        tac.add(new IfThreeAddressCode("goto", tx, "", "L" + ++block));
        // Corpo
        List<ThreeAddressCode> body = generateIntermediateCode(node.getChildren().get(4));
        tac.addAll(body);

        // Incremento
        tx = String.format("t%d", i++);
        tac.add(new MOThreeAddressCode("+", varName, increment, tx));
        tac.add(new ThreeAddressCode("=", tx, "", varName));

        // Volta para a condição
        tac.add(new GoThreeAddressCode("goto", "L" + block++));
        tac.add(new ThreeAddressCode("", "", "","end function for")) ;

        return tac;
    }

    private String getChildValue(Node node) {
        return node.getChildren().get(0).getToken().getValue();
    }

    private  List<ThreeAddressCode> generateStruct(Node node) throws Exception {
        List<ThreeAddressCode> tac = new ArrayList<>();

        tac.add(new ThreeAddressCode("struct", node.getChildren().get(0).getToken().getValue(), "", ""));

        List<ThreeAddressCode> body = generateIntermediateCode(node.getChildren().get(1));
        tac.addAll(body);

        tac.add(new ThreeAddressCode("end struct", node.getChildren().get(0).getToken().getValue(), "", ""));

        return tac;
    }
    private  List<ThreeAddressCode> generateArguments(Node node) throws  Exception {
        List<ThreeAddressCode> tac = new ArrayList<>();
        for (Node child : node.getChildren()) {
            tac.addAll(generateIntermediateCode(child));
        }
        return tac;
    }

    private List<ThreeAddressCode> generateBooleanExpressions(Node node) {
        List<ThreeAddressCode> tac = new ArrayList<>();
        String tempResult = null;
        for (Node child : node.getChildren()) {
            List<ThreeAddressCode> booleanExpression = generateBooleanExpression(child);
            tac.addAll(booleanExpression);
            if (tempResult == null) {
                tempResult = booleanExpression.get(booleanExpression.size() - 1).getResult();
            } else {
                String newTemp = "t" + i++;
                tac.add(new BooleanThreeAddressCode("or", tempResult, booleanExpression.get(booleanExpression.size() - 1).getResult(), newTemp));
                tempResult = newTemp;
            }
        }
        return tac;
    }

   private  List<ThreeAddressCode> generateBooleanExpression(Node node) {
        List<ThreeAddressCode> tac = new ArrayList<>();
        String name = node.getChildren().get(0).getToken().getValue();
        String op = node.getChildren().get(1).getToken().getValue();
        String value = node.getChildren().get(2).getToken().getValue();

        tac.add(new BooleanThreeAddressCode(op, name, value, "t" + i++));
        return tac;
    }


}
