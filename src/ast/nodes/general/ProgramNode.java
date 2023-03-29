package ast.nodes.general;

import java.util.List;

import ast.nodes.ASTNode;

public class ProgramNode extends ASTNode {
    public ProgramNode(ASTNode packageClauseNode, ASTNode importsNode, ASTNode functionsNode) {
        super("Program", List.of(packageClauseNode, importsNode, functionsNode));
    }
}
