package breakers.code;

import breakers.code.grammar.tokens.KeyValueToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        //String string = "int a = 123;";

        String filePath = System.getProperty("user.dir") + "\\src\\breakers\\code\\" + "test.txt";
        String string = new String(Files.readAllBytes(Path.of(filePath)));

        Tokenizer tokenizer = new Tokenizer(string);
        List<List<KeyValueToken>> tokenized = tokenizer.tokenize();

        Parser parser = new Parser();
        parser.parse(tokenized);
        //parser.tokenize(string);
    }
}