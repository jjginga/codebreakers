package breakers.code;

import java.util.ArrayList;
import java.util.List;

public class SyntaxErrorCapturer {

    private List<String> syntaxErrors;

    public SyntaxErrorCapturer() {
        this.syntaxErrors = new ArrayList<String>();
    }

    public void addSyntaxError(String error) {
        this.syntaxErrors.add(error);
    }

    public List<String> getSyntaxErrors() {
        return syntaxErrors;
    }
}
