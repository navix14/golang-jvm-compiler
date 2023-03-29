package ast.nodes.general;

import java.util.List;

import ast.nodes.ASTNode;

public class BlockNode extends ASTNode {
    private String parentFunction;

    public BlockNode(List<ASTNode> statements) {
        super("Block", statements);
        this.parentFunction = null;
    }

    public void setParentFunction(String parentFunction) {
        this.parentFunction = parentFunction;
    }

    public String getParentFunction() {
        return parentFunction;
    }
}
