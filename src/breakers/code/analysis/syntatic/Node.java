package breakers.code.analysis.syntatic;
import breakers.code.grammar.tokens.Token;
import java.util.*;

public class Node {
    private Token token;
    private List<Node> children;

    public Node(Token token) {
        this.token = token;
        this.children = new ArrayList<Node>();
    }

    public void addChild(Node node) {
        children.add(node);
    }

    public Token getToken() {
        return token;
    }

    public List<Node> getChildren() {
        return children;
    }
}