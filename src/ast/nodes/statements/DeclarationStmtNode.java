package ast.nodes.statements;

import java.util.List;

import ast.nodes.ASTNode;
import common.Type;

public class DeclarationStmtNode extends ASTNode {
    private final String identifier;
    private final Type type;
    private final ASTNode expression;

    public DeclarationStmtNode(String identifier, Type type, ASTNode expression) {
        super("DeclarationStmt", List.of(expression));
        this.identifier = identifier;
        this.type = type;
        this.expression = expression;
    }

    @Override
    public String attributeDescription() {
        return "  + " + identifier + ": " + type;
    }

    public String identifier() {
        return identifier;
    }

    public Type type() {
        return type;
    }

    public ASTNode expression() {
        return expression;
    }
}
