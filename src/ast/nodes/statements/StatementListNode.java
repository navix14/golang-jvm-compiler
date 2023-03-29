package ast.nodes.statements;

import java.util.List;

import ast.nodes.ASTNode;

public class StatementListNode extends ASTNode {
    public StatementListNode(List<ASTNode> statements) {
        super("StatementList", statements);
    }
}
