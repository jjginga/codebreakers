package breakers.code;

public class SyntaxErrorException extends RuntimeException {
    private String message;

    public SyntaxErrorException(String message){
        this.message = message;
    }
}
