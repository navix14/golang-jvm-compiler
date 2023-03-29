package ast;

import java.util.List;
import java.util.stream.Collectors;

import common.Type;

import ast.nodes.*;
import ast.nodes.functions.*;
import ast.nodes.general.*;
import ast.nodes.expressions.*;
import ast.nodes.expressions.operatortypes.*;
import ast.nodes.statements.*;

import generated.SimpleGoBaseVisitor;
import generated.SimpleGoParser.*;

public class ASTVisitor extends SimpleGoBaseVisitor<ASTNode> {

    @Override
    public ASTNode visitProgram(ProgramContext ctx) {
        var packageClauseNode = visit(ctx.packageClause());
        var importsNode = visit(ctx.imports());
        var functionsNode = visit(ctx.functions());

        return new ProgramNode(packageClauseNode, importsNode, functionsNode);
    }

    @Override
    public ASTNode visitPackageClause(PackageClauseContext ctx) {
        var packageName = ctx.IDENTIFIER().getText();
        return new PackageClauseNode(packageName);
    }

    @Override
    public ASTNode visitImports(ImportsContext ctx) {
        var importDeclNodes = ctx.importDecl()
                .stream()
                .map(importDecl -> visit(importDecl))
                .collect(Collectors.toList());

        return new ImportsNode(importDeclNodes);
    }

    @Override
    public ASTNode visitImportDecl(ImportDeclContext ctx) {
        var importName = ctx.STRING_LIT().getText();
        return new ImportDeclNode(importName);
    }

    @Override
    public ASTNode visitFunctions(FunctionsContext ctx) {
        var functionDeclNodes = ctx.functionDecl()
                .stream()
                .map(functionDecl -> visit(functionDecl))
                .collect(Collectors.toList());

        return new FunctionsNode(functionDeclNodes);
    }

    @Override
    public ASTNode visitFunctionDecl(FunctionDeclContext ctx) {
        var functionName = ctx.IDENTIFIER().getText();
        var signatureNode = visit(ctx.signature());
        var blockNode = visit(ctx.block());

        if (blockNode instanceof BlockNode block)
            block.setParentFunction(ctx.IDENTIFIER().getText());

        return new FunctionDeclNode(functionName, signatureNode, blockNode);
    }

    @Override
    public ASTNode visitSignature(SignatureContext ctx) {
        var parametersNode = visit(ctx.parameters());
        var returnType = Type.Void;

        if (ctx.typeName() != null)
            returnType = Type.from(ctx.typeName().getText());
            
        return new FunctionSignatureNode(parametersNode, returnType);
    }

    @Override
    public ASTNode visitParameters(ParametersContext ctx) {
        var parameterDeclNodes = ctx.parameterDecl()
                .stream()
                .map(decl -> visit(decl))
                .collect(Collectors.toList());

        return new FunctionParametersNode(parameterDeclNodes);
    }

    @Override
    public ASTNode visitParameterDecl(ParameterDeclContext ctx) {
        var parameterName = ctx.IDENTIFIER().getText();
        var type = Type.from(ctx.typeName().getText());

        return new FunctionParameterDeclNode(parameterName, type);
    }

    @Override
    public ASTNode visitBlock(BlockContext ctx) {
        if (ctx.statementList() != null) {
            var statements = ctx.statementList().statement()
                .stream()
                .map(statement -> visit(statement))
                .collect(Collectors.toList());

            return new BlockNode(statements);
        }

        return new BlockNode(null);
    }

    @Override
    public ASTNode visitStatement(StatementContext ctx) {
        if (ctx.declaration() != null)
            return visit(ctx.declaration());

        if (ctx.returnStmt() != null)
            return visit(ctx.returnStmt());

        if (ctx.block() != null)
            return visit(ctx.block());

        if (ctx.forStmt() != null)
            return visit(ctx.forStmt());

        if (ctx.simpleStmt() != null) {
            var simpleStmt = ctx.simpleStmt();

            if (simpleStmt.assignment() != null)
                return visit(simpleStmt.assignment());

            if (simpleStmt.expressionStmt() != null)
                return visit(simpleStmt.expressionStmt());

            if (simpleStmt.ifStmt() != null)
                return visit(simpleStmt.ifStmt());

            return null;
        }

        return null;
    }

    @Override
    public ASTNode visitForStmt(ForStmtContext ctx) {
        var expressionNode = visit(ctx.expression());
        var blockNode = visit(ctx.block());

        return new ForStmtNode(expressionNode, blockNode);
    }

    @Override
    public ASTNode visitAssignment(AssignmentContext ctx) {
        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));
        return new AssignmentStmtNode(left, right);
    }

    @Override
    public ASTNode visitExpressionStmt(ExpressionStmtContext ctx) {
        var expression = visit(ctx.expression());
        return new ExpressionStmtNode(expression);
    }

    @Override
    public ASTNode visitIfStmt(IfStmtContext ctx) {
        var conditional = visit(ctx.expression());
        var block = visit(ctx.block(0));

        ASTNode elseIf = null;
        ASTNode blockElse = null;

        if (ctx.ELSE() != null) {
            if (ctx.ifStmt() != null)
                elseIf = visit(ctx.ifStmt());
            else if (ctx.block(1) != null)
                blockElse = visit(ctx.block(1));
            else
                return null;
        }

        return new IfStmtNode(conditional, block, elseIf, blockElse);
    }

    @Override
    public ASTNode visitReturnStmt(ReturnStmtContext ctx) {
        ASTNode returnValue = null;

        if (ctx.expression() != null)
            returnValue = visit(ctx.expression());

        return new ReturnStmtNode(returnValue);
    }

    @Override
    public ASTNode visitVarDecl(VarDeclContext ctx) {
        var identifier = ctx.IDENTIFIER().getText();
        var type = Type.from(ctx.typeName().getText());
        ASTNode expression = null;

        if (ctx.expression() != null)
            expression = visit(ctx.expression());

        return new DeclarationStmtNode(identifier, type, expression);
    }

    @Override
    public ASTNode visitExpressionList(ExpressionListContext ctx) {
        var expressionNodes = ctx.expression()
                .stream()
                .map(expr -> visit(expr))
                .collect(Collectors.toList());

        return new ExpressionListNode(expressionNodes);
    }

    @Override
    public ASTNode visitPrimaryExpression(PrimaryExpressionContext ctx) {
        return visit(ctx.primaryExpr());
    }

    @Override
    public ASTNode visitUnaryExpression(UnaryExpressionContext ctx) {
        var expression = visit(ctx.expression());
        UnaryOperatorType operatorType = null;

        if (ctx.PLUS() != null)
            operatorType = UnaryOperatorType.PLUS;
        else if (ctx.PLUS() != null)
            operatorType = UnaryOperatorType.MINUS;
        else if (ctx.EXCLAMATION() != null)
            operatorType = UnaryOperatorType.LOGICAL_NOT;

        return new UnaryExpressionNode(operatorType, expression);
    }

    @Override
    public ASTNode visitMultiplicativeExpression(MultiplicativeExpressionContext ctx) {
        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));
        MultiplicativeOperatorType operatorType = null;

        if (ctx.STAR() != null)
            operatorType = MultiplicativeOperatorType.MULTIPLICATION;
        else if (ctx.DIV() != null)
            operatorType = MultiplicativeOperatorType.DIVISION;
        else if (ctx.MOD() != null)
            operatorType = MultiplicativeOperatorType.MODULO;

        return new MultiplicativeExpressionNode(operatorType, left, right);
    }

    @Override
    public ASTNode visitAdditiveExpression(AdditiveExpressionContext ctx) {
        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));
        AdditiveOperatorType operatorType = null;

        if (ctx.PLUS() != null)
            operatorType = AdditiveOperatorType.ADDITION;
        else if (ctx.MINUS() != null)
            operatorType = AdditiveOperatorType.SUBTRACTION;

        return new AdditiveExpressionNode(operatorType, left, right);
    }

    @Override
    public ASTNode visitRelationalExpression(RelationalExpressionContext ctx) {
        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));
        RelationalOperatorType operatorType = null;

        if (ctx.EQUALS() != null)
            operatorType = RelationalOperatorType.EQUALS;
        else if (ctx.NOT_EQUALS() != null)
            operatorType = RelationalOperatorType.NOT_EQUALS;
        else if (ctx.LESS() != null)
            operatorType = RelationalOperatorType.LESS;
        else if (ctx.LESS_OR_EQUALS() != null)
            operatorType = RelationalOperatorType.LESS_OR_EQUALS;
        else if (ctx.GREATER() != null)
            operatorType = RelationalOperatorType.GREATER;
        else if (ctx.GREATER_OR_EQUALS() != null)
            operatorType = RelationalOperatorType.GREATER_OR_EQUALS;

        return new RelationalExpressionNode(operatorType, left, right);
    }

    @Override
    public ASTNode visitLogicalAndExpression(LogicalAndExpressionContext ctx) {
        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));
        return new LogicalAndExpressionNode(left, right);
    }

    @Override
    public ASTNode visitLogicalOrExpression(LogicalOrExpressionContext ctx) {
        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));
        return new LogicalOrExpressionNode(left, right);
    }

    @Override
    public ASTNode visitPrimaryExpr(PrimaryExprContext ctx) {
        if (ctx.operand() != null)
            return visit(ctx.operand());

        List<ASTNode> arguments = null;
        var functionName = ctx.primaryExpr().getText();

        if (ctx.arguments() != null) {
            var argsCtx = ctx.arguments();

            if (argsCtx.expressionList() != null) {
                arguments = argsCtx.expressionList().expression()
                    .stream()
                    .map(expr -> visit(expr))
                    .collect(Collectors.toList());
            }
        }

        return new FunctionCallExpressionNode(functionName, arguments);
    }

    @Override
    public ASTNode visitOperand(OperandContext ctx) {
        if (ctx.literal() != null) {
            var literal = ctx.literal();

            if (literal.DECIMAL_LIT() != null)
                return new LiteralNode(Type.Int, Integer.parseInt(literal.DECIMAL_LIT().getText()));
            else if (literal.STRING_LIT() != null)
                return new LiteralNode(Type.String, literal.STRING_LIT().getText());
            else if (literal.FLOAT_LIT() != null)
                return new LiteralNode(Type.Float64, Float.parseFloat(literal.FLOAT_LIT().getText()));
            else if (literal.BOOLEAN_LIT() != null)
                return new LiteralNode(Type.Bool, Boolean.parseBoolean(literal.BOOLEAN_LIT().getText()));

            return null;
        }

        if (ctx.operandName() != null) {
            return new VariableIdentifierNode(ctx.operandName().getText());
        }

        if (ctx.expression() != null) {
            return visit(ctx.expression());
        }

        return null;
    }

    @Override
    public ASTNode visitArguments(ArgumentsContext ctx) {
        if (ctx.expressionList() != null) {
            var args = ctx.expressionList().expression()
                .stream()
                .map(arg -> visit(arg))
                .collect(Collectors.toList());

            return new FunctionArgumentsNode(args);
        }

        return new FunctionArgumentsNode(null);
    }
}
