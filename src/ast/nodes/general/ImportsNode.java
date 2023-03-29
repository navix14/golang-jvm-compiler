package ast.nodes.general;

import java.util.List;

import ast.nodes.ASTNode;

public class ImportsNode extends ASTNode {
    public ImportsNode(List<ASTNode> importDecls) {
        super("Imports", importDecls);
    }
}
