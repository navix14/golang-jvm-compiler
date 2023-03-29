package ast.nodes.functions;

import java.util.List;

import ast.nodes.ASTNode;

public class FunctionArgumentsNode extends ASTNode {
    public FunctionArgumentsNode(List<ASTNode> arguments) {
        super("FunctionArguments", arguments);
    }
}
