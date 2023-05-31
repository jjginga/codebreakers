package breakers.code;

import breakers.code.analysis.syntatic.Node;
import breakers.code.grammar.tokens.Token;
import breakers.code.symboltable.SymbolTable;
import breakers.code.symboltable.SymbolTableFiller;
import breakers.code.tac.ThreeAddressCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String filePath = System.getProperty("user.dir") + "/inbound/yailfile.yail";
        String string = new String(Files.readAllBytes(Path.of(filePath)));

        Tokenizer tokenizer = new Tokenizer(string);
        List<List<Token>> tokenized = tokenizer.tokenize();

        Parser parser = new Parser(tokenized);

        CodeGenerator codeGenerator = new CodeGenerator();

        Optimizer optimizer = new Optimizer();
        try {
            System.out.println("*****Parse Tree*****\n");
            Node node = parser.parse();
            System.out.println(node.toString());
            System.out.println("\n\n\n*****Intermediate Code*****\n");
            List<ThreeAddressCode> code = codeGenerator.generateCode(node);
            code.forEach(System.out::println);
            System.out.println("\n\n\n*****Optimized Code*****\n");
            optimizer.setTacList(code);
            optimizer.optimize().forEach(System.out::println);


            /**SymbolTable symbolTable = new SymbolTable(null);

            SymbolTableFiller symbolTableFiller = new SymbolTableFiller(node, symbolTable);
            symbolTableFiller.fillSymbolTable();

            SymbolTable currentRoot = symbolTableFiller.getCurrentSymbolTable();

            currentRoot.printAllSymbolEntries();**/

            ExceptionsWriter exceptionsWriter = new ExceptionsWriter(parser.getSyntaxErrors());

            exceptionsWriter.writeExceptions();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}