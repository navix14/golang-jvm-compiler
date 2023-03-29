package typechecking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ast.ASTBaseVisitor;
import ast.nodes.expressions.*;
import ast.nodes.functions.*;
import ast.nodes.general.*;
import ast.nodes.statements.*;
import common.Type;
import functionresolver.FunctionEntry;
import functionresolver.Parameter;
import scopes.Environment;

public class TypecheckVisitorAST extends ASTBaseVisitor<Type> {
    private final Map<String, FunctionEntry> functionTable;
    private FunctionEntry currentFunction;
    private Environment currentScope;

    public TypecheckVisitorAST(Map<String, FunctionEntry> functionTable) {
        this.functionTable = functionTable;
        this.currentFunction = null;
        this.currentScope = new Environment(null);
    }

    @Override
    public Type visitAdditiveExpression(AdditiveExpressionNode additiveExpressionNode) {
        var left = visit(additiveExpressionNode.left());
        var right = visit(additiveExpressionNode.right());

        if (!TypecheckUtils.compatibleAdditive(left, right)) {
            System.out.printf("Cannot add values of types '%s' and '%s'\n", left, right);
            return Type.Invalid;
        }

        switch (additiveExpressionNode.operatorType()) {
            case ADDITION -> {
                if (left == Type.String && right == Type.String)
                    return Type.String;

                return TypecheckUtils.coerce(left, right);
            }
            case SUBTRACTION -> {
                if (left == Type.String && right == Type.String)
                    return Type.Invalid;

                return TypecheckUtils.coerce(left, right);
            }
            default -> {
                return Type.Invalid;
            }
        }
    }

    @Override
    public Type visitMultiplicativeExpression(MultiplicativeExpressionNode multiplicativeExpressionNode) {
        var left = visit(multiplicativeExpressionNode.left());
        var right = visit(multiplicativeExpressionNode.right());

        if (!TypecheckUtils.compatibleMultiplicative(left, right))
            System.out.printf("Cannot multiply or divide values of types '%s' and '%s'\n", left, right);

        switch (multiplicativeExpressionNode.operatorType()) {
            case MULTIPLICATION, DIVISION -> {
                return TypecheckUtils.coerce(left, right);
            }
            case MODULO -> {
                if (left != Type.Int || right != Type.Int)
                    System.out.printf("Cannot apply modulo to types '%s' and '%s'\n", left, right);

                return Type.Int;
            }
            default -> {
                return Type.Invalid;
            }
        }
    }

    @Override
    public Type visitUnaryExpression(UnaryExpressionNode unaryExpressionNode) {
        var type = visit(unaryExpressionNode.expression());

        if (type == Type.String)
            System.out.printf("Unary operators are not applicable to type 'string'\n");

        switch (unaryExpressionNode.operatorType()) {
            case PLUS, MINUS -> {
                if (!type.isNumeric()) {
                    System.out.printf("Unary plus cannot be applied to type '%s'\n", type);
                    return Type.Invalid;
                }

                return type;
            }
            case LOGICAL_NOT -> {
                if (type != Type.Bool) {
                    System.out.printf("Logical not cannot be applied to type '%s'\n", type);
                    return Type.Invalid;
                }

                return type;
            }
            default -> {
                return Type.Invalid;
            }
        }
    }

    @Override
    public Type visitRelationalExpression(RelationalExpressionNode relationalExpressionNode) {
        var left = visit(relationalExpressionNode.left());
        var right = visit(relationalExpressionNode.right());

        if (!TypecheckUtils.compatibleRelational(left, right)) {
            System.out.printf("Cannot compare values of types '%s' and '%s'\n", left, right);
            return Type.Invalid;
        }

        switch (relationalExpressionNode.operatorType()) {
            case EQUALS, NOT_EQUALS -> {
                return Type.Bool;
            }
            case LESS, LESS_OR_EQUALS, GREATER, GREATER_OR_EQUALS -> {
                if (!left.isNumeric() || !right.isNumeric()) {
                    System.out.printf("Relational operator '%s' not available for types '%s' and '%s'\n",
                            relationalExpressionNode.operatorType().toSymbol(), left, right);
                    return Type.Invalid;
                }

                return Type.Bool;
            }
            default -> {
                return Type.Invalid;
            }
        }
    }

    @Override
    public Type visitLogicalAndExpression(LogicalAndExpressionNode logicalAndExpressionNode) {
        var left = visit(logicalAndExpressionNode.left());
        var right = visit(logicalAndExpressionNode.right());

        if (left != Type.Bool || right != Type.Bool) {
            System.out.printf("Cannot apply '&&' to non-boolean types '%s' and '%s'\n", left, right);
            return Type.Invalid;
        }

        return Type.Bool;
    }

    @Override
    public Type visitLogicalOrExpression(LogicalOrExpressionNode logicalOrExpressionNode) {
        var left = visit(logicalOrExpressionNode.left());
        var right = visit(logicalOrExpressionNode.right());

        if (left != Type.Bool || right != Type.Bool) {
            System.out.printf("Cannot apply '&&' to non-boolean types '%s' and '%s'\n", left, right);
            return Type.Invalid;
        }

        return Type.Bool;
    }

    @Override
    public Type visitFunctionCallExpression(FunctionCallExpressionNode functionCallExpressionNode) {
        var functionName = functionCallExpressionNode.functionName();
        var args = functionCallExpressionNode.arguments();

        if (!functionTable.containsKey(functionName)) {
            System.out.printf("Calling undefined function '%s'\n", functionName);
            return Type.Invalid;
        }

        List<Type> callerParams = new ArrayList<>();

        if (args != null) {
            callerParams = args
                    .stream()
                    .map(arg -> visit(arg))
                    .collect(Collectors.toList());
        }

        var functionEntry = functionTable.get(functionName);

        List<Parameter> formalParams = functionEntry.getParameters();

        if (functionEntry.takesAnyParameters())
            formalParams = fillFakeParams(functionEntry.getParameters(), callerParams);

        if (typecheckParameters(functionName, formalParams, callerParams))
            return functionEntry.getReturnType();
        else
            return Type.Invalid;
    }

    @Override
    public Type visitLiteral(LiteralNode literalNode) {
        return literalNode.type();
    }

    @Override
    public Type visitVariableIdentifier(VariableIdentifierNode variableIdentifierNode) {
        var symbolName = variableIdentifierNode.variableName();
        var symbol = currentScope.get(symbolName);

        if (symbol == null) {
            System.out.printf("Reference to undefined symbol '%s'\n", symbolName);
            return Type.Invalid;
        }

        return symbol.getType();
    }

    @Override
    public Type visitDeclarationStatement(DeclarationStmtNode declarationStmtNode) {
        var variableName = declarationStmtNode.identifier();
        var variableType = declarationStmtNode.type();
        var expression = declarationStmtNode.expression();

        if (currentScope.get(variableName, true) != null) {
            System.out.printf("Redeclared variable '%s' in same block\n", variableName);
            return Type.Invalid;
        }

        if (expression == null) {
            System.out.printf("Variable '%s' declared but not defined\n", variableName);
            return Type.Invalid;
        }

        var expressionType = visit(expression);

        if (!TypecheckUtils.compatibleAssignment(variableType, expressionType)) {
            System.out.printf("Cannot assign type '%s' to type '%s'\n", expressionType, variableType);
            return Type.Invalid;
        }

        currentScope.put(variableName, variableType);

        return Type.Invalid;
    }

    @Override
    public Type visitBlock(BlockNode blockNode) {
        var oldScope = currentScope;

        currentScope = new Environment(currentScope);

        if (blockNode.getParentFunction() != null)
            populateFunctionScope(currentScope, blockNode.getParentFunction());

        visitChildren(blockNode);

        currentScope = oldScope;

        return Type.Invalid;
    }

    @Override
    public Type visitAssignmentStatement(AssignmentStmtNode assignmentStmtNode) {
        var left = visit(assignmentStmtNode.left());
        var right = visit(assignmentStmtNode.right());

        if (!TypecheckUtils.compatibleAssignment(left, right)) {
            System.out.printf("Cannot assign type '%s' to type '%s\n", right, left);
        }

        return Type.Invalid;
    }

    @Override
    public Type visitFunctionDecl(FunctionDeclNode functionDeclNode) {
        currentFunction = functionTable.get(functionDeclNode.functionName());
        return visitChildren(functionDeclNode);
    }

    @Override
    public Type visitReturnStatement(ReturnStmtNode returnStmtNode) {
        var functionName = currentFunction.getFunctionName();
        var returnType = currentFunction.getReturnType();
        var expression = returnStmtNode.expression();

        if (returnType != Type.Void) {
            if (expression == null) {
                System.out.printf("Function '%s' expects return type '%s', but got 'void'\n", functionName, returnType);
                return Type.Invalid;
            }

            var exprType = visit(expression);

            if (!TypecheckUtils.compatibleAssignment(returnType, exprType)) {
                System.out.printf("Function '%s' expects return type '%s', but got '%s'\n", functionName, returnType,
                        exprType);
                return Type.Invalid;
            }
        } else {
            if (expression != null) {
                var exprType = visit(expression);

                System.out.printf("Function '%s' expects return type 'void', but got '%s'\n", functionName, exprType);

                return Type.Invalid;
            }
        }

        return Type.Invalid;
    }

    @Override
    public Type visitIfStatement(IfStmtNode ifStmtNode) {
        var conditionalType = visit(ifStmtNode.conditionalExpr());

        if (conditionalType != Type.Bool) {
            System.out.printf("Condition in if-statement has to be a boolean expression, got '%s' instead\n",
                    conditionalType);
            return Type.Invalid;
        }

        visitChildren(ifStmtNode);

        return Type.Invalid;
    }

    @Override
    public Type visitForStatement(ForStmtNode forStmtNode) {
        var conditionalExpr = visit(forStmtNode.conditionalExpr());

        if (conditionalExpr != Type.Bool) {
            System.out.printf("Condition in for-loop has to be a boolean expression, got '%s' instead\n",
                    conditionalExpr);
            return Type.Invalid;
        }

        visitChildren(forStmtNode);

        return Type.Invalid;
    }

    private void populateFunctionScope(Environment scope, String parentFunction) {
        var funcEntry = functionTable.get(parentFunction);

        for (var param : funcEntry.getParameters())
            scope.put(param.getName(), param.getType());
    }

    private boolean typecheckParameters(String functionName, List<Parameter> formalParams, List<Type> callerParams) {
        var numFormalParams = formalParams.size();
        var numCallerParams = callerParams.size();

        if (numFormalParams != numCallerParams) {
            System.out.printf("Function '%s' expects '%d' arguments, but given '%d'\n", functionName, numFormalParams,
                    numCallerParams);
            return false;
        }

        for (int i = 0; i < numCallerParams; i++) {
            var formalParam = formalParams.get(i);
            var callerParam = callerParams.get(i);

            if (!TypecheckUtils.compatibleAssignment(formalParam.getType(), callerParam)
                    && formalParam.getType() != Type.Any) {
                return false;
            }
        }

        return true;
    }

    private List<Parameter> fillFakeParams(List<Parameter> parameters, List<Type> callerParams) {
        var filledParams = new ArrayList<Parameter>();

        for (var callerParam : callerParams)
            if (callerParam != null)
                filledParams.add(new Parameter("", callerParam));

        return filledParams;
    }

}
