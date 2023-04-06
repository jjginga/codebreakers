package breakers.code;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        //String string = "int a = 123;";

        String filePath = System.getProperty("user.dir") + "\\src\\breakers\\code\\" + "test.txt";
        String string = new String(Files.readAllBytes(Path.of(filePath)));

        Parser parser = new Parser();
        parser.tokenize(string);
    }
}