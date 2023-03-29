package ast.nodes.general;

import ast.nodes.ASTNode;

public class VariableIdentifierNode extends ASTNode {
    private final String variableName;

    public VariableIdentifierNode(String variableName) {
        super("VariableIdentifier", null);
        this.variableName = variableName;
    }

    public String variableName() {
        return variableName;
    }

    @Override
    public String attributeDescription() {
        return "  + " + variableName();
    }
}
