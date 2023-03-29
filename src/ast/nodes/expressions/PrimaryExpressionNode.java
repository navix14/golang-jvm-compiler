package ast.nodes.expressions;

import java.util.List;

import ast.nodes.ASTNode;

public class PrimaryExpressionNode extends ASTNode {
    public PrimaryExpressionNode(List<ASTNode> children) {
        super("PrimaryExpression", children);
    }
}
