package ast.nodes;

import java.util.List;

public abstract class ASTNode {
    private final String nodeName;
    private List<ASTNode> children;

    public ASTNode(String nodeName, List<ASTNode> children) {
        this.nodeName = nodeName;
        this.children = children;
    }

    public String getNodeName() {
        return nodeName;
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    public String attributeDescription() {
        return null;
    }
}
