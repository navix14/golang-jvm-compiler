package ast.nodes.functions;

import java.util.List;

import ast.nodes.ASTNode;
import ast.nodes.general.BlockNode;

public class FunctionDeclNode extends ASTNode {
    private final String functionName;
    private final FunctionSignatureNode signature;
    private final BlockNode block;

    public FunctionDeclNode(String functionName, ASTNode signatureNode, ASTNode blockNode) {
        super("FunctionDeclaration", List.of(signatureNode, blockNode));
        this.functionName = functionName;
        this.signature = (FunctionSignatureNode) signatureNode;
        this.block = (BlockNode) blockNode;
    }

    public String functionName() {
        return functionName;
    }

    public FunctionSignatureNode signature() {
        return signature;
    }

    public BlockNode block() {
        return block;
    }

    @Override
    public String attributeDescription() {
        return "  + " + functionName();
    }
}
