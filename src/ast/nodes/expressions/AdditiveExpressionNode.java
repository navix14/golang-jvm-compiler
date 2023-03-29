package ast.nodes.expressions;

import java.util.List;

import ast.nodes.ASTNode;
import ast.nodes.expressions.operatortypes.AdditiveOperatorType;

public class AdditiveExpressionNode extends ASTNode {
    private final AdditiveOperatorType operatorType;
    private final ASTNode left;
    private final ASTNode right;

    public AdditiveExpressionNode(AdditiveOperatorType operatorType, ASTNode left, ASTNode right) {
        super("AdditiveExpression", List.of(left, right));
        this.operatorType = operatorType;
        this.left = left;
        this.right = right;
    }

    public AdditiveOperatorType operatorType() {
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
}
