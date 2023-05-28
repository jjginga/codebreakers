package breakers.code;

import breakers.code.analysis.syntatic.Node;
import breakers.code.analysis.syntatic.Utils;
import breakers.code.grammar.tokens.TYPES;
import breakers.code.grammar.tokens.Token;
import breakers.code.grammar.tokens.lexems.BASIC_GRAMMAR;
import breakers.code.grammar.tokens.lexems.BASIC_VAR;
import breakers.code.grammar.tokens.lexems.delimiters.COMPOSED_OPERATORS;

import java.util.*;
import java.util.stream.Collectors;

import static breakers.code.grammar.tokens.TYPES.*;
import static breakers.code.grammar.tokens.lexems.GENERAL_SCHEMA.*;
import static breakers.code.grammar.tokens.lexems.INPUT_OUTPUT.*;
import static breakers.code.grammar.tokens.lexems.INTERNAL_FUNCTIONS.*;
import static breakers.code.grammar.tokens.lexems.NEED_FOR_PARSING.*;
import static breakers.code.grammar.tokens.lexems.RESERVED_WORDS.*;
import static breakers.code.grammar.tokens.lexems.STATEMENT_TERMINATOR.SEMICOLON;
import static breakers.code.grammar.tokens.lexems.delimiters.COMPOSED_OPERATORS.MULEQUALS;
import static breakers.code.grammar.tokens.lexems.delimiters.COMPOSED_OPERATORS.OR;
import static breakers.code.grammar.tokens.lexems.delimiters.OPERATORS.*;
import static breakers.code.grammar.tokens.lexems.delimiters.PARENTHESIS.*;
import static breakers.code.grammar.tokens.lexems.delimiters.PONCTUATION.COMMA;
import static breakers.code.grammar.tokens.lexems.identifiers.CON_NAMES.CON_NAME;
import static breakers.code.grammar.tokens.lexems.identifiers.FUN_NAMES.FUN_NAME;
import static breakers.code.grammar.tokens.lexems.identifiers.VAR_NAMES.VAR_NAME;
import static breakers.code.grammar.tokens.lexems.identifiers.VEC_NAMES.VEC_NAME;
import static breakers.code.grammar.tokens.lexems.literals.BOOL_VAR.FALSE;
import static breakers.code.grammar.tokens.lexems.literals.BOOL_VAR.TRUE;
import static breakers.code.grammar.tokens.lexems.literals.NUMBERS.NUMBER;
import static breakers.code.grammar.tokens.lexems.literals.STRING.TEXT;
import static breakers.code.grammar.tokens.lexems.literals.VEC_VALUES.VEC_VALUE;

public class Parser {
    private List<Token> tokens;
    private int index;
    private Token currentToken;
    private String currentScope = "global";
    private Node root;
    private Set<String> constantNames = new HashSet<String>(); //constant names must be unique
    private Set<String> structNames = new HashSet<String>(); //struct names must be unique
    private Set<String> globalVariableNames = new HashSet<String>(); //global variable name can be overwritten
    private Map<String,Set<String>> localVariableNames= new HashMap<>();

    private SyntaxErrorCapturer syntaxErrorCapturer = new SyntaxErrorCapturer();
    private Utils utils;

    public Parser(List<List<Token>> tokens){
        //this.tokens is a flat map of tokens
        this.tokens = tokens.stream().flatMap(List::stream).collect(Collectors.toList());
        this.index = 0;
        this.utils = new Utils();
    }

    public Node parse() throws Exception {
        root = new Node(new Token(null, "program", TYPES.PROGRAM));
        while(index<tokens.size()){
            Node child = parseStatement();
            if(child != null){
                root.addChild(child);
            }
        }
        return root;
    }

    private Node parseStatement() throws Exception {

        Node node = null;

        currentToken = getCurrentToken();
        BASIC_GRAMMAR key = currentToken.getKey();
        //first we must take care of the constants
        if (key.equals(CONST)) {
            node = parseConstants();
            //the structs must be declared first because they may be used in global variables
            //struct is a type
        } else if (key.equals(STRUCTS)) {
            node = parseStruct();
            //then we parse the global variables
        } else if (key.equals(GLOBAL)) {
            node = parseGlobal();
        } else if (key.equals(MAIN)) {
            node = parseMain();
        } else if (key.equals(LOCAL)) {
            node = parseLocal();
        } else if (key.equals(FUN_NAME) && currentScope.equals(currentToken.getValue())){
            node = parseAssignment();
        } else if (key.equals(FUN_NAME)) {
            node = parseFunctionDeclaration();
        } else if (key.equals(IF)) {
            node = parseIf();
        } else if (key.equals(WHILE)) {
            node = parseWhile();
        } else if (key.equals(FOR)) {
            node = parseFor();
        } else if (key.equals(WRITE) || key.equals(WRITE_ALL) || key.equals(WRITE_STRING)) {
            node = parseWrite();
        } else {
            node = parseAssignment();
        }
        return node;
    }

    /**Parse Structs**/
    private Node parseStruct() throws Exception {
        Node structNode = new Node(currentToken);
        eat(STRUCTS);
        eat(LBRACE);

        while(currentToken.getKey() != RBRACE) {


            Node structName = parseIdentifier(STRUCT_NAME);
            if (structNames.contains(structName.getToken().getValue())) {
                syntaxErrorCapturer.addSyntaxError("Duplicate struct name: " + structName.getToken().getValue());
            }

            structNames.add(structName.getToken().getValue());
            eat(STRUCT_NAME);

            eat(LBRACE);

            Node structType = parseType();
            structName.addChild(structType);
            while (currentToken.getKey() != RBRACE) {
                Node field = parseStructField();
                structName.addChild(field);
            }

            eat(RBRACE);
            eat(SEMICOLON);
            structNode.addChild(structName);
        }


        return structNode;
    }

    private Node parseStructField() throws Exception {
        Node fieldType = parseType();
        Node fieldName = parseIdentifier(VAR_NAME);

        if (fieldType.getChildren().stream().anyMatch(n -> n.getToken().getValue().equals(fieldName.getToken().getValue()))) {
            syntaxErrorCapturer.addSyntaxError("Duplicate field name: " + fieldName.getToken().getValue());
        }

        eat(VAR_NAME);
        eat(SEMICOLON);

        fieldName.addChild(fieldType);
        return fieldName;
    }


    /**Parsing Main**/
    private Node parseMain() throws Exception {
        Node main = new Node(currentToken);

        eat(MAIN);

        if(currentToken.getKey() == EQUALS){
            //TODO: check if return type is of same type has main return type.
            eat(EQUALS);
            Node return_value = parseExpression();
            main.addChild(return_value);
            eat(SEMICOLON);
            return main;
        }


        eat(LPAREN);
        eat(RPAREN);

        Node return_type = parseType();

        main.addChild(return_type);

        eat(LBRACE);
        currentScope = "main";
        createScope();
        while(currentToken.getKey()!=RBRACE){
            main.addChild(parseStatement());
        }
        eat(RBRACE);
        currentScope = "global";
        return main;
    }

    /**Parsing Function Declaration**/
    private Node parseFunctionDeclaration() throws Exception {
        Node func_name = new Node(currentToken);
        currentScope = func_name.getToken().getValue();
        createScope();
        eat(FUN_NAME);

        eat(LPAREN);

        Node arguments = new Node(new Token(ARGUMENTS, "arguments", FUNCTION));
        while (currentToken.getKey() != RPAREN) {
            Node argumentType = parseType();
            Node argumentName = new Node(currentToken);
            eat(currentToken.getKey());

            insertVariableNameInScope(argumentName);
            argumentName.addChild(argumentType);
            arguments.addChild(argumentName);

            if (currentToken.getKey() == COMMA) {
                eat(COMMA);
            }
        }
        if(arguments.getChildren().size()>0)
            func_name.addChild(arguments);

        eat(RPAREN);

        Node return_type = parseType();

        func_name.addChild(return_type);

        eat(LBRACE);

        //If is LBRACE we are on local function definition
        /**if(currentToken.getKey()==LBRACE){
            eat(LBRACE);
            Node localVariables = parseLocalVariableInsideFunction();
            func_name.addChild(localVariables);
            eat(RBRACE);
        }**/
        while(currentToken.getKey()!=RBRACE){
            /*if(currentToken.getKey()==FUN_NAME){
                func_name.addChild(parseAssignment());
            }*/
             func_name.addChild(parseStatement());
        }
        eat(RBRACE);
        currentScope = "global";
        return  func_name;
    }

    /**Parsing of the different assignments in constant block**/

    private Node parseConstants() throws Exception {
        //we create a node with constant declaration
        Node constants = new Node(currentToken);
        eat(CONST);
        eat(LBRACE);
        while(currentToken.getKey()!=RBRACE){
            constants.addChild(parseConstant());
        }
        eat(RBRACE);
        return constants;
    }

    private Node parseConstant() throws Exception {
        Node const_type = parseType();

        if(constantNames.contains(currentToken.getValue()))
            syntaxErrorCapturer.addSyntaxError("Constant name already declared: " + currentToken.getValue());

        Node const_name = parseIdentifier(CON_NAME);
        constantNames.add(const_name.getToken().getValue());
        Node const_value = null;
        if(currentToken.getKey()!=EQUALS) {
            //the declaration can be implicit
            const_value = parseConstOrVarImplicitValue(const_type);
        } else {
            eat(EQUALS);
            const_value = parseValue();
        }
        eat(SEMICOLON);
        const_name.addChild(const_type);
        const_name.addChild(const_value);
        return const_name;
    }



    private Node parseConstOrVarImplicitValue(Node constType) {
        BASIC_VAR basicVar = (BASIC_VAR) constType.getToken().getKey();
        switch (basicVar){
            case INT:
                return new Node(new Token(NUMBER, "0", NUMERIC));
            case FLOAT:
                return new Node(new Token(NUMBER, "0.0", NUMERIC));
            default:
                return new Node(new Token(FALSE, "false", BOOLEAN));
        }
    }

    /**Parsing of the different assignments in global block**/

    private Node parseGlobal() throws Exception {
        Node globalVariables = new Node(currentToken);
        eat(GLOBAL);
        eat(LBRACE);
        while(currentToken!=null && currentToken.getKey()!=RBRACE){
            globalVariables.addChild(parseGlobalVariable());
        }
        if(currentToken==null)
            syntaxErrorCapturer.addSyntaxError("Missing closing brace");
        eat(RBRACE);
        return globalVariables;
    }

    private Node parseGlobalVariable() throws Exception {
        Node var_type = parseType();

        if(currentToken!=null && constantNames.contains(currentToken.getValue()))
            syntaxErrorCapturer.addSyntaxError("Can not overwritte constant: " + currentToken.getValue());

        Node const_name = null;
        if(currentToken.getKey()== VAR_NAME)
            const_name = parseVariable(var_type);
        else if(currentToken.getKey() == VEC_NAME)
            const_name = parseVector(var_type);
        else
            syntaxErrorCapturer.addSyntaxError("Invalid global variable:" + currentToken.getValue());


        return const_name;
    }

    /**Parsing local variables**/

    private void createScope() {
        localVariableNames.put(currentScope, new HashSet<String>());
    }
    private void insertVariableNameInScope(Node var_name) {
        localVariableNames.get(currentScope).add(var_name.getToken().getValue());
    }
    private Node parseLocal() throws Exception {
        Node localVariables = new Node(currentToken);
        eat(LOCAL);
        eat(LBRACE);
        while(currentToken.getKey()!=RBRACE){
            localVariables.addChild(parseLocalVariable());
        }
        eat(RBRACE);
        eat(SEMICOLON);
        return localVariables;
    }

    private Node parseLocalVariableInsideFunction() throws Exception {
        Node localVariables = new Node(currentToken);
        eat(LOCAL);
        while(currentToken.getKey()!=SEMICOLON){
            localVariables.addChild(parseLocalVariable());
        }
        eat(SEMICOLON);
        return localVariables;
    }

    private Node parseLocalVariable() throws Exception {
        Node var_type = parseType();

        if(constantNames.contains(currentToken.getValue()))
            syntaxErrorCapturer.addSyntaxError("Can not overwritte constant: " + currentToken.getValue());

        Node var_name = parseIdentifier(VAR_NAME);

        insertVariableNameInScope(var_name);

        Node var_value = null;
        if(var_name.getToken().getKey() == VAR_NAME){
            if(currentToken!=null && currentToken.getKey()!=EQUALS) {
                //the declaration can be implicit
                var_value = parseConstOrVarImplicitValue(var_type);
            } else {
                eat(EQUALS);
                var_value = parseValue();
            };
        } else if(var_name.getToken().getKey()== VEC_NAME)
            var_value = parseVector(var_type);
        else
            syntaxErrorCapturer.addSyntaxError("Invalid global variable:" + currentToken.getValue());

        var_name.addChild(var_type);
        var_name.addChild(var_value);

        return var_name;
    }
    private Node parseVariable(Node var_type) throws Exception {
        Node var_name = parseIdentifier(VAR_NAME);

        globalVariableNames.add(var_name.getToken().getValue());
        Node var_value = parseVariableValue(var_type);
        var_name.addChild(var_type);
        var_name.addChild(var_value);
        return var_name;
    }

    private Node parseVariableValue(Node var_type) throws Exception {
        Node var_value = null;
        if(currentToken!=null && currentToken.getKey()!=EQUALS) {
            //the declaration can be implicit
            var_value = parseConstOrVarImplicitValue(var_type);
            consumeToken();
        } else {
            eat(EQUALS);
            var_value = parseValue();
        }
        eat(SEMICOLON);
        return var_value;
    }


    private Node parseVector(Node var_type) throws Exception {
        Node vec_name = parseIdentifier(VEC_NAME);

        globalVariableNames.add(vec_name.getToken().getValue());
        eat(LBRACKET);
        Node vec_size = null;
        if(currentToken.getKey()==RBRACKET)
            vec_size = new Node(new Token(NUMBER, "1", NUMERIC));
        else {
            vec_size = parseValue();
            eat(NUMBER);
        }
        eat(RBRACKET);
        Node vec_contents = new Node(new Token(VEC_VALUE, "vec_value", VARIABLE));
        if(currentToken.getKey()!=EQUALS)
            vec_contents.addChild(parseConstOrVarImplicitValue(var_type));
        else {
            eat(EQUALS);
            eat(LBRACE);
            while(currentToken.getKey()!=RBRACE){
                vec_contents.addChild(parseValue());
                eat(NUMBER);
                if(currentToken.getKey()!=RBRACE)
                    eat(COMMA);
            }
            eat(RBRACE);
        }
        eat(SEMICOLON);

        if(vec_contents.getChildren().size()!=Integer.parseInt(vec_size.getToken().getValue()))
            syntaxErrorCapturer.addSyntaxError("Invalid vector size: " + vec_name.getToken().getValue());

        vec_name.addChild(var_type);
        vec_name.addChild(vec_size);
        vec_name.addChild(vec_contents);
        return vec_name;
    }

    private Node parseValue() throws Exception {
        if(!utils.isNumber(currentToken) && !utils.isBoolean(currentToken)){
            syntaxErrorCapturer.addSyntaxError("Invalid value: " + currentToken.getKey());
        }

        return getNode();
    }

    private Node parseIdentifier(BASIC_GRAMMAR key) throws Exception {
        //if the identifier is not a valid name we throw an exception
        if(!utils.validateVariableName(currentToken))
            syntaxErrorCapturer.addSyntaxError("Invalid name " + key + ": " + currentToken.getKey());
        return getNode();
    }

    private Node parseType() throws Exception {
        //If the token does not match the expected types we throw an exception
        if(BASIC_VAR.stream().noneMatch(x->x==currentToken.getKey()) && !structNames.contains(currentToken.getValue())){
            syntaxErrorCapturer.addSyntaxError("Invalid Type: " + currentToken.getKey());
        }

        return getNode();
    }

    /**Parse Write**/
    private Node parseWrite() throws Exception {
        Node writeNode = new Node(currentToken);

        if (currentToken.getKey() == WRITE) {
            eat(WRITE);
        } else if (currentToken.getKey() == WRITE_ALL) {
            eat(WRITE_ALL);
        } else {
            eat(WRITE_STRING);
        }

        eat(LPAREN);

        while (currentToken.getKey() != RPAREN) {
            if (currentToken.getKey() == TEXT) {
                Node stringNode = new Node(currentToken);
                writeNode.addChild(stringNode);
                eat(TEXT);
            } else if (currentToken.getKey() == VAR_NAME || currentToken.getKey() == VEC_NAME || currentToken.getKey() == CON_NAME) {
                Node varNode = parseIdentifier(currentToken.getKey());
                writeNode.addChild(varNode);
            } else if(currentToken.getKey() == FUN_NAME) {
                Node funcNode = parseFunctionCall();
                writeNode.addChild(funcNode);
            } else if (currentToken.getKey() == COMMA) {
                eat(COMMA);
            } else {
                syntaxErrorCapturer.addSyntaxError("Invalid token in write statement: " + currentToken.getKey());
            }
        }

        eat(RPAREN);
        eat(SEMICOLON);

        return writeNode;
    }

    private Node parseFunctionCall() throws Exception {
        Node funcNode = new Node(currentToken);
        eat(FUN_NAME);
        eat(LPAREN);

        // Parsing function arguments
        if (currentToken.getKey() != RPAREN) {
            funcNode.addChild(parseExpression());
            consumeToken();
            while (currentToken.getKey() == COMMA) {
                eat(COMMA);
                funcNode.addChild(parseExpression());
            }
        }

        eat(RPAREN);

        return funcNode;

    }


    /**Parse Assignment **/
    private Node parseAssignment() throws Exception{
        Node assignment = new Node(new Token(ASSIGNMENT, "assignment", VARIABLE));
        Node var_name = parseIdentifier(VAR_NAME);
        
        if(constantNames.contains(var_name))
            syntaxErrorCapturer.addSyntaxError("Can not overwritte constant: " + var_name.getToken().getValue());
        
        if(!globalVariableNames.contains(var_name.getToken().getValue())
            && !localVariableNames.get(currentScope).contains(var_name.getToken().getValue())
                && !currentScope.equals(var_name.getToken().getValue()))
            syntaxErrorCapturer.addSyntaxError("Variable not declared: " + var_name.getToken().getValue());

        //TODO: OTHER like +=, -=, *=, /=, %=
        if(currentToken.getKey()==MULEQUALS){
            Node expression = parseExpression();
            eat(MULEQUALS);

            eat(currentToken.getKey());
            assignment.addChild(var_name);
            assignment.addChild(expression);
            expression.addChild(var_name);
            eat(SEMICOLON);
            return assignment;
        }

        eat(EQUALS);
        
        Node assignmentExpression = null;
        
        if(currentToken.getKey()==READ || currentToken.getKey()==READ_ALL || currentToken.getKey()==READ_STRING)
            assignmentExpression = parseRead();
        else if(currentToken.getKey()==POW || currentToken.getKey()==SQUARE_ROOT || currentToken.getKey()==GEN)
            assignmentExpression = parseInternalFunction();
        else
            assignmentExpression = parseExpression();

        eat(SEMICOLON);

        assignment.addChild(var_name);
        assignment.addChild(assignmentExpression);
        return assignment;
    }

    private Node parseExpression() throws Exception {
        Node expression = parseTerm();

        while (currentToken != null && (currentToken.getKey() == PLUS || currentToken.getKey() == MINUS)) {
            Node operator = new Node(currentToken);
            consumeToken();

            Node term = parseTerm();
            operator.addChild(expression);
            operator.addChild(term);
            expression = operator;
        }

        while (currentToken != null && (currentToken.getKey() == MULEQUALS)) {
            Node operator = new Node(new Token(MUL, "*", OPERATOR));
            consumeToken();

            Node term = parseTerm();

            operator.addChild(term);
            expression = operator;
        }

        return expression;
    }

    private Node parseTerm() throws Exception {
        Node term = parseFactor();

        while (currentToken.getKey() == MUL || currentToken.getKey() == DIV ) {
            Token operator = currentToken;
            eat(operator.getKey());

            Node factor = parseFactor();

            Node termNode = new Node(operator);
            termNode.addChild(term);
            termNode.addChild(factor);

            term = termNode;
        }

        return term;
    }

    private Node parseFactor() throws Exception {
        Node node = new Node(currentToken); // forcing initialization
        if (currentToken.getKey() == LPAREN) {
            eat(LPAREN);
            node = parseExpression();
            eat(RPAREN);
        } else if (currentToken.getKey() == NUMBER) {
            node = new Node(currentToken);
            eat(NUMBER);
        } else if (currentToken.getKey() == TRUE || currentToken.getKey() == FALSE) {
            node = new Node(currentToken);
            eat(currentToken.getKey());
        } else if (currentToken.getKey() == VAR_NAME) {
            node = new Node(currentToken);
            if(!globalVariableNames.contains(node.getToken().getValue())
                    && !localVariableNames.get(currentScope).contains(node.getToken().getValue()))
                syntaxErrorCapturer.addSyntaxError("Variable not declared: " + node.getToken().getValue());

        } else if (currentToken.getKey() == VEC_NAME) {
            node = new Node(currentToken);
            if(!globalVariableNames.contains(node.getToken().getValue())
                    && !localVariableNames.get(currentScope).contains(node.getToken().getValue()))
                syntaxErrorCapturer.addSyntaxError("Vector not declared: " + node.getToken().getValue());
        } else {
            syntaxErrorCapturer.addSyntaxError("Invalid syntax: " + currentToken.getKey());
        }

        return node;
    }


    private Node parseRead() throws Exception {
        Node read = new Node(currentToken);
        if(currentToken.getKey()==READ)
            eat(READ);
        else if(currentToken.getKey()==READ_ALL)
            eat(READ_ALL);
        else
            eat(READ_STRING);
        eat(LPAREN);
        eat(RPAREN);

        return read;
    }

    private Node parseInternalFunction() throws Exception {
        Node internalFunction = new Node(currentToken);
        if(currentToken.getKey()==POW)
            eat(POW);
        else if(currentToken.getKey()==SQUARE_ROOT)
            eat(SQUARE_ROOT);
        else
            eat(GEN);
        eat(LPAREN);
        eat(RPAREN);
        return internalFunction;
    }

    private Node parseMulEquals() throws Exception {
        Node mulEquals = new Node(currentToken);
        eat(MULEQUALS);


        return mulEquals;
    }

    /**Parse other **/
    private Node parseIf() throws Exception {
        Node ifNode = new Node(currentToken);

        eat(IF);
        eat(LPAREN);

        ifNode.addChild(parseBooleanExpression());

        eat(RPAREN);
        eat(LBRACE);

        while(currentToken.getKey()!=RBRACE)
             ifNode.addChild(parseStatement());

        eat(RBRACE);

        if(currentToken.getKey()==ELSE){
            Node elseNode = new Node(currentToken);
            eat(ELSE);
            eat(LBRACE);
            while(currentToken.getKey()!=RBRACE)
                elseNode.addChild(parseStatement());
            eat(RBRACE);
            ifNode.addChild(elseNode);
        }
        return ifNode;
    }

    private Node parseWhile() throws Exception {
        Node whileNode = new Node(currentToken);

        eat(WHILE);
        eat(LPAREN);

        whileNode.addChild(parseBooleanExpression());

        eat(RPAREN);
        eat(LBRACE);

        while(currentToken.getKey()!=RBRACE)
            whileNode.addChild(parseStatement());
        eat(RBRACE);

        return whileNode;
    }

    private Node parseFor() throws Exception {
        Node forNode = new Node(currentToken);

        eat(FOR);
        eat(LPAREN);

        forNode.addChild(parseForVar());


        eat(COMMA);

        forNode.addChild(parseForInit());

        eat(COMMA);
        forNode.addChild(parseForEnd());
        eat(COMMA);

        forNode.addChild(parseForUpdate());

        eat(RPAREN);
        eat(LBRACE);

        while(currentToken.getKey()!=RBRACE)
            forNode.addChild(parseStatement());
        eat(RBRACE);

        return forNode;
    }

    private Node parseForVar() throws Exception {
        Node forVar = new Node(new Token(FOR_VAR, "for_var", INFORMATION_FLOW));
        Node var_name = new Node(currentToken);

        if(!globalVariableNames.contains(var_name.getToken().getValue())
                && !localVariableNames.get(currentScope).contains(var_name.getToken().getValue())
                && !currentScope.equals(var_name.getToken().getValue()))
            syntaxErrorCapturer.addSyntaxError("Variable not declared: " + var_name.getToken().getValue());

        eat(VAR_NAME);
        forVar.addChild(var_name);
        return forVar;
    }

    private Node parseForInit() throws Exception {
        Node forInit = new Node(new Token(FOR_INIT, "for_init", INFORMATION_FLOW));
        Node initial_value = parseExpression();
        forInit.addChild(initial_value);
        return forInit;
    }

    private Node parseForEnd() throws Exception {
        Node forEnd = new Node(new Token(FOR_END, "for_end", INFORMATION_FLOW));
        Node end_value = parseExpression();
        forEnd.addChild(end_value);
        consumeToken();
        return forEnd;
    }

    private Node parseForUpdate() throws Exception {
        Node forUpdate = new Node(new Token(FOR_INCREMENT, "for_increment", INFORMATION_FLOW));
        Node update_value = new Node(currentToken);
        eat(NUMBER);
        forUpdate.addChild(update_value);
        return forUpdate;
    }

    private Node parseBooleanExpression() throws Exception {
        Node booleanExpression = new Node(new Token(BOOLEAN_EXPRESSION, "boolean_expression", BOOL_EXPRESSION));
        while(currentToken.getKey()!=RPAREN){
            booleanExpression.addChild(parseIndividualBooleanExpression());
            if(currentToken.getKey()!=RPAREN)
                eat(OR);
        }
        return booleanExpression;
    }

    private Node parseIndividualBooleanExpression() throws Exception {
        Node expression = new Node(new Token(BOOLEAN_EXPRESSION, "boolean_expression", BOOL_EXPRESSION));
        Node var_name = new Node(currentToken);
        eat(VAR_NAME);

        if (COMPOSED_OPERATORS.stream().noneMatch(x -> x == currentToken.getKey())
                && currentToken.getKey() != GREATER_THAN
                    && currentToken.getKey() != LESS_THAN)
            syntaxErrorCapturer.addSyntaxError("Invalid operator: " + currentToken.getKey());
        Node operator = new Node(currentToken);
        consumeToken();
        Node value = null;
        if(currentToken.getKey() == NUMBER){
            value = parseValue();
        } else {
            if(!globalVariableNames.contains(currentToken.getValue())
                    && !localVariableNames.get(currentScope).contains(currentToken.getValue())
                        && !constantNames.contains(currentToken.getValue()))
                syntaxErrorCapturer.addSyntaxError("Variable not declared: " + var_name.getToken().getValue());
            value = new Node(currentToken);
            consumeToken();
        }

        expression.addChild(var_name);
        expression.addChild(operator);
        expression.addChild(value);

        return expression;

    }

    private Node getNode() throws Exception {
        Node identifier = new Node(currentToken);
        consumeToken();
        return identifier;
    }



    private Token getCurrentToken(){
        if(index >= tokens.size())
            return null;
        return tokens.get(index);
    }

    private void consumeToken() throws Exception {
        index++;
        currentToken = getCurrentToken();
    }

    private void eat(BASIC_GRAMMAR key) throws Exception {
        if (currentToken.getKey() == key) {
            consumeToken();
        } else {
            syntaxErrorCapturer.addSyntaxError("Erro de sintaxe. Token inesperado: " + currentToken.getKey());
        }
    }


    public List<String> getSyntaxErrors() {
        return syntaxErrorCapturer.getSyntaxErrors();
    }
}
