package ast.nodes.expressions;

import java.util.List;

import ast.nodes.ASTNode;

public class ExpressionNode extends ASTNode {
    public ExpressionNode(List<ASTNode> children) {
        super("Expression", children);
    } 
}
