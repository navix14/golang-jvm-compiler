package ast.nodes.expressions;

import java.util.List;

import ast.nodes.ASTNode;
import ast.nodes.expressions.operatortypes.RelationalOperatorType;

public class RelationalExpressionNode extends ASTNode {
    private final ASTNode left;
    private final ASTNode right;
    private final RelationalOperatorType operatorType;

    public RelationalExpressionNode(RelationalOperatorType operatorType, ASTNode left, ASTNode right) {
        super("RelationalExpression", List.of(left, right));
        this.operatorType = operatorType;
        this.left = left;
        this.right = right;
    }

    public RelationalOperatorType operatorType() {
        return operatorType;
    }

    @Override
    public String attributeDescription() {
        return "  + " + "'" + operatorType().toSymbol() + "'";
    }

    public ASTNode left() {
        return left;
    }

    public ASTNode right() {
        return right;
    }
}
