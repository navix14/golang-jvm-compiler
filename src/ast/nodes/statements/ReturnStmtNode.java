package ast.nodes.statements;

import ast.nodes.ASTNode;
import ast.nodes.NodeUtils;

public class ReturnStmtNode extends ASTNode {
    private final ASTNode expression;

    public ReturnStmtNode(ASTNode expression) {
        super("ReturnStmt", NodeUtils.collectNodes(expression));
        this.expression = expression;
    }

    public ASTNode expression() {
        return expression;
    }
}
