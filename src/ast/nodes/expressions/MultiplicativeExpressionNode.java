package ast.nodes.expressions;

import java.util.List;

import ast.nodes.ASTNode;
import ast.nodes.expressions.operatortypes.MultiplicativeOperatorType;

public class MultiplicativeExpressionNode extends ASTNode {
    private final ASTNode left;
    private final ASTNode right;
    private final MultiplicativeOperatorType operatorType;

    public MultiplicativeExpressionNode(MultiplicativeOperatorType operatorType, ASTNode left, ASTNode right) {
        super("MultiplicativeExpression", List.of(left, right));
        this.operatorType = operatorType;
        this.left = left;
        this.right = right;
    }

    public MultiplicativeOperatorType operatorType() {
        return operatorType;
    }

    @Override
    public String attributeDescription() {
        return "  + " + "'" + operatorType.toSymbol() + "'";
    }

    public ASTNode left() {
        return left;
    }

    public ASTNode right() {
        return right;
    }

    public MultiplicativeOperatorType getOperatorType() {
        return operatorType;
    }
}
