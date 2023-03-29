package ast;

import ast.nodes.ASTNode;
import ast.nodes.expressions.AdditiveExpressionNode;
import ast.nodes.expressions.FunctionCallExpressionNode;
import ast.nodes.expressions.LiteralNode;
import ast.nodes.expressions.LogicalAndExpressionNode;
import ast.nodes.expressions.LogicalOrExpressionNode;
import ast.nodes.expressions.MultiplicativeExpressionNode;
import ast.nodes.expressions.PrimaryExpressionNode;
import ast.nodes.expressions.RelationalExpressionNode;
import ast.nodes.expressions.UnaryExpressionNode;
import ast.nodes.functions.FunctionArgumentsNode;
import ast.nodes.functions.FunctionDeclNode;
import ast.nodes.functions.FunctionParameterDeclNode;
import ast.nodes.functions.FunctionParametersNode;
import ast.nodes.functions.FunctionSignatureNode;
import ast.nodes.functions.FunctionsNode;
import ast.nodes.general.BlockNode;
import ast.nodes.general.ImportDeclNode;
import ast.nodes.general.ImportsNode;
import ast.nodes.general.PackageClauseNode;
import ast.nodes.general.ProgramNode;
import ast.nodes.general.VariableIdentifierNode;
import ast.nodes.statements.AssignmentStmtNode;
import ast.nodes.statements.DeclarationStmtNode;
import ast.nodes.statements.ExpressionStmtNode;
import ast.nodes.statements.ForStmtNode;
import ast.nodes.statements.IfStmtNode;
import ast.nodes.statements.ReturnStmtNode;

public abstract class ASTBaseVisitor<T> {
    public T visitProgram(ProgramNode programNode) {
        return visitChildren(programNode);
    }

    public T visitChildren(ASTNode node) {
        if (node.getChildren() == null)
            return null;

        for (var child : node.getChildren())
            visit(child);

        return null;
    }

    public T visitFunctionCallExpression(FunctionCallExpressionNode functionCallExpressionNode) {
        return visitChildren(functionCallExpressionNode);
    }

    public T visitPrimaryExpression(PrimaryExpressionNode primaryExpressionNode) {
        return visitChildren(primaryExpressionNode);
    }

    public T visitLogicalOrExpression(LogicalOrExpressionNode logicalOrExpressionNode) {
        return visitChildren(logicalOrExpressionNode);
    }

    public T visitLogicalAndExpression(LogicalAndExpressionNode logicalAndExpressionNode) {
        return visitChildren(logicalAndExpressionNode);
    }

    public T visitRelationalExpression(RelationalExpressionNode relationalExpressionNode) {
        return visitChildren(relationalExpressionNode);
    }

    public T visitUnaryExpression(UnaryExpressionNode unaryExpressionNode) {
        return visitChildren(unaryExpressionNode);
    }

    public T visitMultiplicativeExpression(MultiplicativeExpressionNode multiplicativeExpressionNode) {
        return visitChildren(multiplicativeExpressionNode);
    }

    public T visitAdditiveExpression(AdditiveExpressionNode additiveExpressionNode) {
        return visitChildren(additiveExpressionNode);
    }

    public T visitForStatement(ForStmtNode forStmtNode) {
        return visitChildren(forStmtNode);
    }

    public T visitReturnStatement(ReturnStmtNode returnStmtNode) {
        return visitChildren(returnStmtNode);
    }

    public T visitIfStatement(IfStmtNode ifStmtNode) {
        return visitChildren(ifStmtNode);
    }

    public T visitExpressionStatement(ExpressionStmtNode expressionStmtNode) {
        return visitChildren(expressionStmtNode);
    }

    public T visitAssignmentStatement(AssignmentStmtNode assignmentStmtNode) {
        return visitChildren(assignmentStmtNode);
    }

    public T visitDeclarationStatement(DeclarationStmtNode declarationStmtNode) {
        return visitChildren(declarationStmtNode);
    }

    public T visitFunctionArguments(FunctionArgumentsNode functionArgumentsNode) {
        return visitChildren(functionArgumentsNode);
    }

    public T visitFunctionParameterDecl(FunctionParameterDeclNode functionParameterDeclNode) {
        return visitChildren(functionParameterDeclNode);
    }

    public T visitFunctionParameters(FunctionParametersNode functionParametersNode) {
        return visitChildren(functionParametersNode);
    }

    public T visitFunctionSignature(FunctionSignatureNode functionSignatureNode) {
        return visitChildren(functionSignatureNode);
    }

    public T visitFunctionDecl(FunctionDeclNode functionDeclNode) {
        return visitChildren(functionDeclNode);
    }

    public T visitFunctions(FunctionsNode functionsNode) {
        return visitChildren(functionsNode);
    }

    public T visitBlock(BlockNode blockNode) {
        return visitChildren(blockNode);
    }

    public T visitVariableIdentifier(VariableIdentifierNode variableIdentifierNode) {
        return visitChildren(variableIdentifierNode);
    }

    public T visitImportDecl(ImportDeclNode importDeclNode) {
        return visitChildren(importDeclNode);
    }

    public T visitImports(ImportsNode importsNode) {
        return visitChildren(importsNode);
    }

    public T visitPackageClause(PackageClauseNode packageClauseNode) {
        return visitChildren(packageClauseNode);
    }

    // General dispatch function
    // Sadly very ugly using Java 16 :(
    // Java 17 (Preview) has noice type-based dispatch
    public T visit(ASTNode node) {
        /* General nodes */
        if (node instanceof ProgramNode programNode)
            return visitProgram(programNode);
        if (node instanceof PackageClauseNode packageClauseNode)
            return visitPackageClause(packageClauseNode);
        if (node instanceof ImportsNode importsNode)
            return visitImports(importsNode);
        if (node instanceof ImportDeclNode importDeclNode)
            return visitImportDecl(importDeclNode);
        if (node instanceof BlockNode blockNode)
            return visitBlock(blockNode);
        if (node instanceof VariableIdentifierNode variableIdentifierNode)
            return visitVariableIdentifier(variableIdentifierNode);

        /* Function-related nodes */
        if (node instanceof FunctionsNode functionsNode)
            return visitFunctions(functionsNode);
        if (node instanceof FunctionDeclNode functionDeclNode)
            return visitFunctionDecl(functionDeclNode);
        if (node instanceof FunctionSignatureNode functionSignatureNode)
            return visitFunctionSignature(functionSignatureNode);
        if (node instanceof FunctionParametersNode functionParametersNode)
            return visitFunctionParameters(functionParametersNode);
        if (node instanceof FunctionParameterDeclNode functionParameterDeclNode)
            return visitFunctionParameterDecl(functionParameterDeclNode);
        if (node instanceof FunctionArgumentsNode functionArgumentsNode)
            return visitFunctionArguments(functionArgumentsNode);

        /* Statement-related nodes */
        if (node instanceof DeclarationStmtNode declarationStmtNode)
            return visitDeclarationStatement(declarationStmtNode);
        if (node instanceof AssignmentStmtNode assignmentStmtNode)
            return visitAssignmentStatement(assignmentStmtNode);
        if (node instanceof ExpressionStmtNode expressionStmtNode)
            return visitExpressionStatement(expressionStmtNode);
        if (node instanceof IfStmtNode ifStmtNode)
            return visitIfStatement(ifStmtNode);
        if (node instanceof ReturnStmtNode returnStmtNode)
            return visitReturnStatement(returnStmtNode);
        if (node instanceof ForStmtNode forStmtNode)
            return visitForStatement(forStmtNode);
        
        /* Expression-related nodes */
        if (node instanceof AdditiveExpressionNode additiveExpressionNode)
            return visitAdditiveExpression(additiveExpressionNode);
        if (node instanceof MultiplicativeExpressionNode multiplicativeExpressionNode)
            return visitMultiplicativeExpression(multiplicativeExpressionNode);
        if (node instanceof UnaryExpressionNode unaryExpressionNode)
            return visitUnaryExpression(unaryExpressionNode);
        if (node instanceof RelationalExpressionNode relationalExpressionNode)
            return visitRelationalExpression(relationalExpressionNode);
        if (node instanceof LiteralNode literalNode)
            return visitLiteral(literalNode);
        if (node instanceof LogicalAndExpressionNode logicalAndExpressionNode)
            return visitLogicalAndExpression(logicalAndExpressionNode);
        if (node instanceof LogicalOrExpressionNode logicalOrExpressionNode)
            return visitLogicalOrExpression(logicalOrExpressionNode);
        if (node instanceof PrimaryExpressionNode primaryExpressionNode)
            return visitPrimaryExpression(primaryExpressionNode);
        if (node instanceof FunctionCallExpressionNode functionCallExpressionNode)
            return visitFunctionCallExpression(functionCallExpressionNode);

        return null;
    }

    public T visitLiteral(LiteralNode literalNode) {
        return null;
    }
}