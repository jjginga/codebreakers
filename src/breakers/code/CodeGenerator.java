package breakers.code;

import breakers.code.analysis.syntatic.Node;
import breakers.code.grammar.tokens.TYPES;
import breakers.code.grammar.tokens.lexems.BASIC_GRAMMAR;

import static breakers.code.grammar.tokens.lexems.GENERAL_SCHEMA.*;
import static breakers.code.grammar.tokens.lexems.INPUT_OUTPUT.READ;
import static breakers.code.grammar.tokens.lexems.INPUT_OUTPUT.WRITE;
import static breakers.code.grammar.tokens.lexems.NEED_FOR_PARSING.ASSIGNMENT;
import static breakers.code.grammar.tokens.lexems.RESERVED_WORDS.*;
import static breakers.code.grammar.tokens.lexems.identifiers.FUN_NAMES.FUN_NAME;

public class CodeGenerator {

    int i = 0;
    int block = 1;
    final String attr = "t%d = %s%n";

    public CodeGenerator() {
    }

    public String generateCode(Node root) throws Exception {
        StringBuilder code = new StringBuilder();
        for(Node child : root.getChildren())
            code.append(generateIntermediateCode(child));
        return code.toString();
    }

    private String generateIntermediateCode(Node node) throws Exception {
        StringBuilder code = new StringBuilder();

        BASIC_GRAMMAR key = node.getToken().getKey();
        //Alternatively node.getToken().getKey(
        if( CONST.equals(key) || GLOBAL.equals(key) || LOCAL.equals(key))
            code.append(generateAttribution(node));
        else if ( MAIN.equals(key) || FUN_NAME.equals(key))
            code.append(generateFunction(node));
        else if ( WRITE.equals(key))
            code.append(generateWrite(node));
        else if ( READ.equals(key))
            code.append(generateRead(node));
        else if ( ASSIGNMENT.equals(key))
            code.append(generateAssignment(node));
        else if ( IF.equals(key))
            code.append(generateIf(node));
        else if ( WHILE.equals(key))
            code.append(String.format("Falta implementar: %s%n", key.getData()));
        else if ( FOR.equals(key))
            code.append(String.format("Falta implementar: %s%n", key.getData()));
        else if ( STRUCTS.equals(key))
            code.append(String.format("Falta implementar: %s%n", key.getData()));
        else
            code.append(String.format("Falta implementar: %s%n", key.getData()));
        return code.toString();
    }

    private String generateFunction(Node node) throws Exception {
        StringBuilder code = new StringBuilder();
        code.append(String.format("function %s:%n", node.getToken().getValue()));
        for(Node child : node.getChildren()){
            if(child.getToken().getKey().getType().equals(TYPES.BASIC_TYPE))
                continue;
            code.append(generateIntermediateCode(child));
        }
        code.append(String.format("end function %s%n", node.getToken().getValue()));
        return code.toString();
    }


    private String generateAttribution(Node node) {
        StringBuilder code = new StringBuilder();

        for (Node child : node.getChildren()) {
            String name = child.getToken().getValue();
            if (child.getChildren().size() > 1) {
                //chid 0 is the type and child 1 is the value
                String value = child.getChildren().get(1).getToken().getValue();
                code.append(String.format(attr, i, value));
                code.append(String.format("%s = t%d%n", name, i++));
            } else {
                //I think that when there is no attribution - just deffinition nothing should be here
                //code.append(String.format("%s\n", name));
            }
        }

        return code.toString();
    }

    private String generateWrite(Node node) {
        StringBuilder code = new StringBuilder();
        if(node.getChildren().size() > 1) {
            StringBuilder aux = new StringBuilder();
            for(Node child : node.getChildren()) {
                code.append(String.format(attr, i, node.getChildren().get(1).getToken().getValue()));
                //in here we should consult the table to check the type of the variable
                aux.append(String.format("t%d ", i++));
            }

            code.append("print ").append(aux.append(String.format("%n")).toString());

        } else {
            code.append(String.format(attr, i, node.getChildren().get(0).getToken().getValue()));
            code.append(String.format("print t%d%n", i++));

        }
        return code.toString();
    }

    private String generateAssignment(Node node) {
        StringBuilder code = new StringBuilder();
        code.append(String.format(attr, i, node.getChildren().get(1).getToken().getValue()));
        code.append(String.format("%s = t%d%n", node.getChildren().get(0).getToken().getValue(), i++));
        return code.toString();
    }

    //TODO: this isn't oke - special case of assignment
    private String generateRead(Node node) {
        StringBuilder code = new StringBuilder();
        code.append(String.format("%s = read%n", node.getChildren().get(0).getToken().getValue()));
        return code.toString();
    }

    private String generateIf(Node node) throws Exception {
        StringBuilder code = new StringBuilder();
        String conditions = generateBooleanExpressions(node.getChildren().get(0));
        int block1 = block++;
        code.append(String.format("if %s goto L%d%n", conditions, block1));
        //TODO: if contains else
        int block2 = block++;
        int block3 = block++;//if final
        code.append(String.format("goto L%d%n", block2));
        code.append(String.format("L%d:%n", block1));
        code.append(generateIntermediateCode(node.getChildren().get(1)));
        code.append(String.format("goto L%d%n", block3));

        code.append(String.format("L%d:%n", block2));
        code.append(generateIntermediateCode(node.getChildren().get(2)));
        code.append(String.format("L%d:%n", block3));

        return code.toString();
    }

    private String generateBooleanExpressions(Node node) {
        StringBuilder code = new StringBuilder();

        for(Node child : node.getChildren()) {
            code.append(generateBooleanExpression(child));
        }

        return code.toString();
    }

    private String  generateBooleanExpression(Node node) {
        String name = node.getChildren().get(0).getToken().getValue();
        String op = node.getChildren().get(1).getToken().getValue();
        String value = node.getChildren().get(2).getToken().getValue();

        return String.format("%s %s %s", name, op, value);
    }


}
