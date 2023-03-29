package ast.nodes.functions;

import java.util.List;

import ast.nodes.ASTNode;
import common.Type;

public class FunctionSignatureNode extends ASTNode {
    private final Type returnType;

    public FunctionSignatureNode(ASTNode parametersNode, Type returnType) {
        super("FunctionSignature", List.of(parametersNode));
        this.returnType = returnType;
    }

    public Type getReturnType() {
        return returnType;
    }

    @Override
    public String attributeDescription() {
        return "  + " + returnType.toString();
    }
}
