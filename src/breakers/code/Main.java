package breakers.code;

public class Main {
    public static void main(String[] args) {
        String string = "int a = 123;";

        Parser parser = new Parser();
        parser.tokenize(string);
    }
}