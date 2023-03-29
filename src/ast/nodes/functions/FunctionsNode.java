package ast.nodes.functions;

import java.util.List;

import ast.nodes.ASTNode;

public class FunctionsNode extends ASTNode {
    public FunctionsNode(List<ASTNode> functionDecls) {
        super("Functions", functionDecls);
    }
}
