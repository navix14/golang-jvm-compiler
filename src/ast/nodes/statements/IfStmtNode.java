package ast.nodes.statements;

import ast.nodes.ASTNode;
import ast.nodes.NodeUtils;

public class IfStmtNode extends ASTNode {
    private final ASTNode conditionalExpr;
    private final ASTNode block;
    private final ASTNode elseIf;
    private final ASTNode blockElse;

    public IfStmtNode(ASTNode conditionalExpr, ASTNode block, ASTNode elseIf, ASTNode blockElse) {
        super("IfStmt", NodeUtils.collectNodes(conditionalExpr, block, elseIf, blockElse));
        this.conditionalExpr = conditionalExpr;
        this.block = block;
        this.elseIf = elseIf;
        this.blockElse = blockElse;
    }

    public ASTNode conditionalExpr() {
        return conditionalExpr;
    }

    public ASTNode block() {
        return block;
    }

    public ASTNode elseIf() {
        return elseIf;
    }

    public ASTNode blockElse() {
        return blockElse;
    }
}
