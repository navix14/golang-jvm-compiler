package ast.nodes.functions;

import java.util.List;

import ast.nodes.ASTNode;

public class FunctionParametersNode extends ASTNode {
    public FunctionParametersNode(List<ASTNode> parameterDecls) {
        super("FunctionParameters", parameterDecls);
    }
}
