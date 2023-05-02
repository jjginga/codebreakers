package breakers.code;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class ExceptionsWriter {
    private List<String> capturedExceptions;

    public ExceptionsWriter(List<String> capturedExceptions) {
        this.capturedExceptions = capturedExceptions;
    }

    public void writeExceptions() {
        try {
            String directory = "/breakers/code/outbox";
            String fileName = "exceptions.txt";

            FileWriter myWriter = new FileWriter(new File(".").getAbsolutePath() + "/outbound/exceptions.txt", false);

            for (String exception : capturedExceptions) {
                myWriter.write(exception + "\n");
            }

            myWriter.close();
        } catch (IOException e) {
            System.out.println("Falhou ao escrever exceções no ficheiro." + e.getMessage());
        }
    }
}
