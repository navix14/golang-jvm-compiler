package ast.nodes.general;

import ast.nodes.ASTNode;

public class ImportDeclNode extends ASTNode {
    private final String importName;

    public ImportDeclNode(String importName) {
        super("ImportDecl", null);
        this.importName = importName;
    }

    public String importName() {
        return importName;
    }

    @Override
    public String attributeDescription() {
        return "  + " + importName();
    }
}
