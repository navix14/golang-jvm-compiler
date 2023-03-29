package ast.nodes.general;

import ast.nodes.ASTNode;

public class PackageClauseNode extends ASTNode {
    private final String packageName;

    public PackageClauseNode(String packageName) {
        super("PackageClause", null);
        this.packageName = packageName;
    }

    public String packageName() {
        return packageName;
    }

    @Override
    public String attributeDescription() {
        return "  + " + packageName();
    }
}
