package ast.nodes.expressions;

import java.util.List;

import ast.nodes.ASTNode;

public class LogicalOrExpressionNode extends ASTNode {
    private final ASTNode left;
    private final ASTNode right;

    public LogicalOrExpressionNode(ASTNode left, ASTNode right) {
        super("LogicalOrExpression", List.of(left, right));
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
