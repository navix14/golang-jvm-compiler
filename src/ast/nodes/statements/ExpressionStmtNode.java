package ast.nodes.statements;

import java.util.List;

import ast.nodes.ASTNode;

public class ExpressionStmtNode extends ASTNode {
    public ExpressionStmtNode(ASTNode expression) {
        super("ExpressionStmt", List.of(expression));
    }
}
