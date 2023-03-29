package ast.nodes.statements;

import java.util.List;

import ast.nodes.ASTNode;

public class AssignmentStmtNode extends ASTNode {
    private final ASTNode left;
    private final ASTNode right;

    public AssignmentStmtNode(ASTNode left, ASTNode right) {
        super("AssignmentStmt", List.of(left, right));
        this.left = left;
        this.right = right;
    }

    public ASTNode left() {
        return left;
    }

    public ASTNode right() {
        return right;
    }
}
