package ast.nodes.expressions;

import ast.nodes.ASTNode;
import common.Type;

public class LiteralNode extends ASTNode {
    private final Type type;
    private final Object value;

    public LiteralNode(Type type, Object value) {
        super("Literal", null);
        this.type = type;
        this.value = value;
    }

    public Type type() {
        return type;
    }

    public Object value() {
        return value;
    }

    @Override
    public String attributeDescription() {
        return "  + " + type() + ": " + value();
    }
}
