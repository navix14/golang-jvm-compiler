package ast.nodes.expressions;

import java.util.List;

import ast.nodes.ASTNode;

public class ExpressionListNode extends ASTNode {
    public ExpressionListNode(List<ASTNode> expressions) {
        super("ExpressionList", expressions);
    }
}
