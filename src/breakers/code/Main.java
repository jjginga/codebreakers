package breakers.code;

import breakers.code.analysis.syntatic.Node;
import breakers.code.grammar.tokens.Token;
import breakers.code.symboltable.SymbolTable;
import breakers.code.symboltable.SymbolTableFiller;

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
        try {
            Node node = parser.parse();
            System.out.println(node.toString());
            System.out.println(codeGenerator.generateCode(node));

            SymbolTable symbolTable = new SymbolTable(null);

            SymbolTableFiller symbolTableFiller = new SymbolTableFiller(node, symbolTable);
            symbolTableFiller.fillSymbolTable();

            SymbolTable currentRoot = symbolTableFiller.getCurrentSymbolTable();

            currentRoot.printAllSymbolEntries();

            ExceptionsWriter exceptionsWriter = new ExceptionsWriter(parser.getSyntaxErrors());

            exceptionsWriter.writeExceptions();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}