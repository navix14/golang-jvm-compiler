package ast;

import ast.nodes.ASTNode;
import common.StringUtil;

public class ASTPrinter {
    private final ASTNode root;
    private boolean printAttrs;

    public ASTPrinter(ASTNode node) {
        this.root = node;
    }

    public void printAST(boolean printAttrs) {
        this.printAttrs = printAttrs;
        printAST(root, 0);
    }

    private void printAST(ASTNode node, int indentLevel) {
        var indent = StringUtil.repeat("  ", indentLevel);

        System.out.println(indent + node.getNodeName());

        if (printAttrs && node.attributeDescription() != null)
            System.out.println(indent + node.attributeDescription());
            
        if (node.getChildren() != null) {
            for (var child : node.getChildren())
                printAST(child, indentLevel + 1);
        }
    }
}
