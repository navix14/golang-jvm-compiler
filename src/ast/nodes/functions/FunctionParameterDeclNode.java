package ast.nodes.functions;

import ast.nodes.ASTNode;
import common.Type;

public class FunctionParameterDeclNode extends ASTNode {
    private final String parameterName;
    private final Type type;

    public FunctionParameterDeclNode(String parameterName, Type type) {
        super("ParameterDecl", null);
        this.parameterName = parameterName;
        this.type = type;
    }

    public String parameterName() {
        return parameterName;
    }

    public Type type() {
        return type;
    }

    @Override
    public String attributeDescription() {
        return "  + " + parameterName() + ": " + type();
    }
}
