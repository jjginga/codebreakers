package breakers.code.analysis.syntatic;

import breakers.code.grammar.tokens.Token;
import java.util.*;

public class ASTNode {
    private Token token;
    private List<ASTNode> children;

    public ASTNode(Token token) {
        this.token = token;
        this.children = new ArrayList<ASTNode>();
    }

    public void addChild(ASTNode node) {
        children.add(node);
    }

    public Token getToken() {
        return token;
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    public String toString() {
        return token.getValue();
    }
}
