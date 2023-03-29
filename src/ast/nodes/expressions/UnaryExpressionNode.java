package ast.nodes.expressions;

import java.util.List;

import ast.nodes.ASTNode;
import ast.nodes.expressions.operatortypes.UnaryOperatorType;

public class UnaryExpressionNode extends ASTNode {
    private final UnaryOperatorType operatorType;
    private final ASTNode expression;

    public UnaryExpressionNode(UnaryOperatorType operatorType, ASTNode expression) {
        super("UnaryExpression", List.of(expression));
        this.operatorType = operatorType;
        this.expression = expression;
    }

    @Override
    public String attributeDescription() {
        return "  + " + "'" + operatorType.toSymbol() + "'";
    }

    public UnaryOperatorType operatorType() {
        return operatorType;
    }

    public ASTNode expression() {
        return expression;
    }
}
