package breakers.code;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExceptionsWriter {
    private List<String> capturedExceptions;

    public ExceptionsWriter(List<String> capturedExceptions) {
        this.capturedExceptions = capturedExceptions;
    }

    public void writeExceptions() {
        try {
            FileWriter myWriter = new FileWriter("exceptions.txt");

            for (String exception : capturedExceptions) {
                myWriter.write(exception + "\n");
            }

            myWriter.close();
        } catch (IOException e) {
            System.out.println("Falhou ao escrever exceções no ficheiro.");
        }
    }
}
