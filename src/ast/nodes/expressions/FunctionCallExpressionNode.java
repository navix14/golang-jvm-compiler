package ast.nodes.expressions;

import java.util.List;

import ast.nodes.ASTNode;

public class FunctionCallExpressionNode extends ASTNode {
    private final String functionName;
    private final List<ASTNode> arguments;

    public FunctionCallExpressionNode(String functionName, List<ASTNode> functionArguments) {
        super("FunctionCallExpression", functionArguments);
        this.functionName = functionName;
        this.arguments = functionArguments;
    }

    public String functionName() {
        return functionName;
    }

    @Override
    public String attributeDescription() {
        return "  + " + functionName();
    }

    public List<ASTNode> arguments() {
        return arguments;
    }
}
