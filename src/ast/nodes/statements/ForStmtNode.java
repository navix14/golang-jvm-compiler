package ast.nodes.statements;

import java.util.List;

import ast.nodes.ASTNode;

public class ForStmtNode extends ASTNode {
    private final ASTNode conditionalExpr;
    private final ASTNode block;

    public ForStmtNode(ASTNode expression, ASTNode block) {
        super("ForStmt", List.of(expression, block));
        this.conditionalExpr = expression;
        this.block = block;
    }

    public ASTNode conditionalExpr() {
        return conditionalExpr;
    }

    public ASTNode block() {
        return block;
    }
}
