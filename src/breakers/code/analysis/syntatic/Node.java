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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringHelper(this, sb, 0);
        return sb.toString();
    }

    private void toStringHelper(Node node, StringBuilder sb, int level) {
        for (int i = 0; i < level; i++) {
            sb.append("-");
        }

        sb.append(node.getToken().getValue());
        sb.append("\n");

        for (Node child : node.getChildren()) {
            toStringHelper(child, sb, level + 1);
        }
    }
}