package breakers.code;

import breakers.code.analysis.syntatic.Node;
import breakers.code.grammar.tokens.TYPES;
import breakers.code.grammar.tokens.lexems.BASIC_GRAMMAR;
import breakers.code.tac.ThreeAddressCode;

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
        else
            System.out.println(String.format("Falta implementar: %s%n", key.getData()));
        return code;
    }

    private List<ThreeAddressCode> generateFunction(Node node) throws Exception {
        List<ThreeAddressCode> tac = new ArrayList<>();
        tac.add(new ThreeAddressCode("function", node.getToken().getValue(), "", ""));
        for (Node child : node.getChildren()) {
            if (child.getToken().getKey().getType().equals(TYPES.BASIC_TYPE))
                continue;
            tac.addAll(generateIntermediateCode(child));
        }
        tac.add(new ThreeAddressCode("end function", node.getToken().getValue(), "", "")) ;
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
                tac.add(new ThreeAddressCode("print", tx, "", ""));
            }
        } else {
            String value = node.getChildren().get(0).getToken().getValue();
            String tx = String.format("t%d", i++);
            tac.add(new ThreeAddressCode("=", value, "", tx));
            tac.add(new ThreeAddressCode("print", tx, "", ""));

        }
        return tac;
    }

    private List<ThreeAddressCode> generateAssignment(Node node) {
        //is a complex operation
        if(node.getChildren().get(1).getChildren().size()>1)
            return generateComplexOperation(node);
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
        tac.add(new ThreeAddressCode(operation, tx1, tx2, result));

        return tac;
    }

    //TODO: this isn't oke - special case of assignment
    private List<ThreeAddressCode> generateRead(Node node) {
        List<ThreeAddressCode> tac = new ArrayList<>();
        String readInto = node.getChildren().get(0).getToken().getValue();
        tac.add(new ThreeAddressCode("read", "", "", readInto));
        return tac;
    }

    private List<ThreeAddressCode> generateIf(Node node) throws Exception {
        List<ThreeAddressCode> tac = new ArrayList<>();

        // Generate code for the boolean condition
        List<ThreeAddressCode> conditions = generateBooleanExpressions(node.getChildren().get(0));
        tac.addAll(conditions);

        int block1 = block++;
        tac.add(new ThreeAddressCode("if", conditions.get(conditions.size() - 1).getResult(), "goto", "L" + block1));

        //TODO: if contains else
        int block2 = block++;
        int block3 = block++;//if final
        tac.add(new ThreeAddressCode("goto", "", "", "L" + block2));

        tac.add(new ThreeAddressCode("L" + block1, "", "", ""));

        List<ThreeAddressCode> thenBlock = generateIntermediateCode(node.getChildren().get(1));

        tac.addAll(thenBlock);

        tac.add(new ThreeAddressCode("goto", "", "", "L" + block3));
        tac.add(new ThreeAddressCode("L" + block2, "", "", ""));

        List<ThreeAddressCode> elseBlock = generateIntermediateCode(node.getChildren().get(2));
        tac.addAll(elseBlock);

        tac.add(new ThreeAddressCode("L"+block3, "", "", ""));

        return tac;
    }
    private String generateWhile(Node node) throws Exception {
        // Inicializa um StringBuilder para construir o código intermediário.
        StringBuilder code = new StringBuilder();
        // Adiciona um rótulo de código para o início do loop while.
        code.append(String.format("L%d:%n", block++));
        // Gera código intermediário para a condição do while, que é o primeiro filho do nó while.
        String conditions = generateBooleanExpressions(node.getChildren().get(0));
        // Prepara a variável block1 para o bloco do corpo do loop.
        int block1 = block++;
        // Adiciona uma instrução if-goto que vai para o bloco do corpo do loop (block1) se a condição do while for verdadeira.
        code.append(String.format("if %s goto L%d%n", conditions, block1));
        // Adiciona uma instrução goto que vai para o próximo bloco de código se a condição do while for falsa.
        code.append(String.format("goto L%d%n", block++));
        // Adiciona um rótulo de código para o bloco do corpo do loop, que é o corpo do while.
        code.append(String.format("L%d:%n", block1));
        // Gera código intermediário para o corpo do while, que é o segundo filho do nó while.
        code.append(generateIntermediateCode(node.getChildren().get(1)));
        // Adiciona uma instrução goto que volta ao início do loop while depois que o corpo do loop foi executado.
        code.append(String.format("goto L%d%n", block - 2));
        // Retorna o código intermediário gerado como uma String.
        return code.toString();
    }


    private String generateFor(Node node) throws Exception {
        StringBuilder code = new StringBuilder();
        // Inicialização
        String varName = getChildValue(node.getChildren().get(0));
        String initValue = getChildValue(node.getChildren().get(1));

        code.append(String.format(attr, i, initValue));
        code.append(String.format("%s = t%d%n", varName, i++));

        // Condição
        String endValue = getChildValue(node.getChildren().get(2));
        String increment = getChildValue(node.getChildren().get(3));

        // If is a var we shout consut the table
        String booleanOperator = Integer.parseInt(increment) > 0 ? "<" : ">";

        int block1 = block++;
        code.append(String.format("L%d:%n", block1));
        code.append(String.format("if %s %s %s goto L%d%n", varName, booleanOperator, endValue, block++));

        // Corpo
        code.append(generateIntermediateCode(node.getChildren().get(4)));

        // Incremento
        code.append(String.format(attr, i, increment));
        //if the increment is negative it doesn't matter
        code.append(String.format("%s = %s + t%d%n", varName, varName, i++));

        // Volta para a condição
        code.append(String.format("goto L%d%n", block1));
        code.append(String.format("end for L%d%n", block1));

        return code.toString();
    }

    private String getChildValue(Node node) {
        return node.getChildren().get(0).getToken().getValue();
    }






    private String generateStruct(Node node) throws Exception {
        StringBuilder code = new StringBuilder();
        code.append(String.format("struct %s {%n", node.getChildren().get(0).getToken().getValue()));
        code.append(generateIntermediateCode(node.getChildren().get(1)));
        code.append(String.format("}%n"));
        return code.toString();
    }
    private String generateArguments(Node node) throws  Exception {
        StringBuilder code = new StringBuilder();
        for (Node child : node.getChildren()) {
            code.append(generateIntermediateCode(child));
        }
        return code.toString();
    }


    private String generateBooleanExpressions(Node node) {
        StringBuilder code = new StringBuilder();

        for (Node child : node.getChildren()) {
            code.append(generateBooleanExpression(child));
        }

        return code.toString();
    }


    private String generateBooleanExpression(Node node) {
        String name = node.getChildren().get(0).getToken().getValue();
        String op = node.getChildren().get(1).getToken().getValue();
        String value = node.getChildren().get(2).getToken().getValue();

        return String.format("%s %s %s", name, op, value);
    }


}
