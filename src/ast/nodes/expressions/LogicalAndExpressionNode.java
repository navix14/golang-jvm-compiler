package ast.nodes.expressions;

import java.util.List;

import ast.nodes.ASTNode;

public class LogicalAndExpressionNode extends ASTNode {
    private final ASTNode left;
    private final ASTNode right;

    public LogicalAndExpressionNode(ASTNode left, ASTNode right) {
        super("LogicalAndExpression", List.of(left, right));
        this.left = left;
        this.right = right;
    }

    public ASTNode left() {
        return left;
    }

    public ASTNode right() {
        return right;
    }
}
