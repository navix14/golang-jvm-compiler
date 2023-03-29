package typechecking;

import common.Error;
import common.Type;
import common.Value;
import functionresolver.FunctionEntry;
import functionresolver.Parameter;
import generated.*;
import scopes.Environment;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TypecheckVisitor extends SimpleGoBaseVisitor<Value> {
    private final Map<String, FunctionEntry> functionTable;
    private FunctionEntry currentFunction;
    private Environment currentScope;

    private final List<Error> errors;
    private int localCounter;

    public TypecheckVisitor(Map<String, FunctionEntry> functionTable) {
        this.functionTable = functionTable;
        this.currentScope = new Environment(null);
        this.currentFunction = null;
        this.errors = new ArrayList<>();
        this.localCounter = 0;
    }

    public List<Error> getErrors() {
        return errors;
    }

    private void addError(String message, ParserRuleContext ctx, Object... args) {
        errors.add(new TypecheckError(String.format(message, args), ctx.start.getLine()));
    }

    @Override
    public Value visitPrimaryExpression(SimpleGoParser.PrimaryExpressionContext ctx) {
        return visit(ctx.primaryExpr());
    }

    @Override
    public Value visitAssignment(SimpleGoParser.AssignmentContext ctx) {
        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));

        if (!left.isSymbol())
            addError("Left side of assignment has to be a symbol (lvalue)", ctx);

        if (!TypecheckUtils.compatibleAssignment(left.getType(), right.getType()))
            addError("Cannot assign '%s' to '%s'", ctx, right.getType(), left.getType());

        return null;
    }

    @Override
    public Value visitUnaryExpression(SimpleGoParser.UnaryExpressionContext ctx) {
        var value = visit(ctx.expression());

        if (value == null) {
            addError("Unary operator not applicable to invalid type", ctx);
            return new Value(Type.Invalid);
        }

        if (value.getType() == Type.String) {
            addError("Unary operators are not applicable to type 'string'", ctx);
            return new Value(Type.Invalid);
        }

        switch (ctx.unary_op.getType()) {
            case SimpleGoLexer.PLUS, SimpleGoLexer.MINUS -> {
                if (!value.isNumeric()) {
                    addError("Unary plus cannot be applied to type '%s'", ctx, value.getType());
                    return new Value(Type.Invalid);
                }

                return value;
            }
            case SimpleGoLexer.EXCLAMATION -> {
                if (value.getType() != Type.Bool) {
                    addError("Logical not cannot be applied to type '%s'", ctx, value.getType());
                    return new Value(Type.Invalid);
                }

                return value;
            }
            default -> {
                addError("Unexpected error in unary expression evaluation", ctx);
                return new Value(Type.Invalid);
            }
        }
    }

    @Override
    public Value visitMultiplicativeExpression(SimpleGoParser.MultiplicativeExpressionContext ctx) {
        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));

        if (!TypecheckUtils.compatibleMultiplicative(left, right)) {
            addError("Cannot multiply or divide values of type '%s' and '%s'", ctx, left.getType(), right.getType());
            return new Value(Type.Invalid);
        }

        switch (ctx.mul_op.getType()) {
            case SimpleGoLexer.STAR, SimpleGoLexer.DIV -> {
                return TypecheckUtils.coerce(left, right);
            }
            case SimpleGoLexer.MOD -> {
                if (left.getType() != Type.Int || right.getType() != Type.Int) {
                    addError("Cannot apply modulo to types '%s' and '%s'", ctx, left.getType(), right.getType());
                    return new Value(Type.Invalid);
                }

                return left;
            }
            default -> {
                return new Value(Type.Invalid);
            }
        }
    }

    @Override
    public Value visitAdditiveExpression(SimpleGoParser.AdditiveExpressionContext ctx) {
        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));

        if (!TypecheckUtils.compatibleAdditive(left, right)) {
            addError("Cannot add values of types '%s' and '%s'", ctx, left.getType(), right.getType());
            return new Value(Type.Invalid);
        }

        switch (ctx.add_op.getType()) {
            case SimpleGoLexer.PLUS -> {
                if (left.getType() == Type.String && right.getType() == Type.String)
                    return new Value(Type.String);

                return TypecheckUtils.coerce(left, right);
            }
            case SimpleGoLexer.MINUS -> {
                if (left.getType() == Type.String && right.getType() == Type.String) {
                    addError("Cannot subtract values of type 'String'", ctx);
                    return new Value(Type.Invalid);
                }

                return TypecheckUtils.coerce(left, right);
            }
            default -> {
                return new Value(Type.Invalid);
            }
        }
    }

    @Override
    public Value visitRelationalExpression(SimpleGoParser.RelationalExpressionContext ctx) {
        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));

        if (!TypecheckUtils.compatibleRelational(left, right)) {
            addError("Cannot compare values of types '%s' and '%s'", ctx, left.getType(), right.getType());
            return new Value(Type.Invalid);
        }

        switch (ctx.rel_op.getType()) {
            case SimpleGoLexer.EQUALS, SimpleGoLexer.NOT_EQUALS -> {
                return new Value(Type.Bool);
            }
            case SimpleGoLexer.LESS, SimpleGoLexer.LESS_OR_EQUALS,
                    SimpleGoLexer.GREATER, SimpleGoLexer.GREATER_OR_EQUALS -> {
                if (!left.isNumeric()) {
                    addError("Relational operator not available for types '%s' and '%s'", ctx, left.getType(),
                            right.getType());
                    return new Value(Type.Invalid);
                }

                return new Value(Type.Bool);
            }
            default -> {
                return new Value(Type.Invalid);
            }
        }
    }

    @Override
    public Value visitLogicalAndExpression(SimpleGoParser.LogicalAndExpressionContext ctx) {
        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));

        if (left.getType() != Type.Bool || right.getType() != Type.Bool) {
            addError("Cannot apply logical AND to non-boolean types '%s' and '%s'", ctx, left.getType(),
                    right.getType());
            return new Value(Type.Invalid);
        }

        return left;
    }

    @Override
    public Value visitLogicalOrExpression(SimpleGoParser.LogicalOrExpressionContext ctx) {
        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));

        if (left.getType() != Type.Bool || right.getType() != Type.Bool) {
            addError("Cannot apply logical OR to non-boolean types '%s' and '%s'", ctx, left.getType(),
                    right.getType());
            return new Value(Type.Invalid);
        }

        return left;
    }

    @Override
    public Value visitPrimaryExpr(SimpleGoParser.PrimaryExprContext ctx) {
        if (ctx.operand() != null)
            return visit(ctx.operand());

        if (ctx.arguments() != null) {
            var functionName = ctx.primaryExpr().getText();
            var argumentsCtx = ctx.arguments();

            if (!functionTable.containsKey(functionName)) {
                addError("Calling undefined function '%s'", ctx, functionName);
                return new Value(Type.Invalid);
            }

            List<Value> callerParams = new ArrayList<>();

            if (argumentsCtx.expressionList() != null) {
                callerParams = argumentsCtx.expressionList().expression()
                        .stream()
                        .map(this::visit)
                        .collect(Collectors.toList());
            }

            var functionEntry = functionTable.get(functionName);
            List<Parameter> formalParams;

            if (functionEntry.takesAnyParameters())
                formalParams = fillFakeParams(functionEntry.getParameters(), callerParams);
            else
                formalParams = functionEntry.getParameters();

            if (typecheckParameters(ctx, formalParams, callerParams))
                return new Value(functionEntry.getReturnType());
            else
                return new Value(Type.Invalid);
        }

        return new Value(Type.Invalid);
    }

    @Override
    public Value visitOperand(SimpleGoParser.OperandContext ctx) {
        if (ctx.literal() != null)
            return visit(ctx.literal());

        if (ctx.operandName() != null) {
            var value = visit(ctx.operandName());
            return new Value(value.getType(), true);
        }

        if (ctx.L_PAREN() != null)
            return visit(ctx.expression());

        return new Value(Type.Invalid);
    }

    @Override
    public Value visitOperandName(SimpleGoParser.OperandNameContext ctx) {
        var varName = ctx.IDENTIFIER().getText();
        var symbol = currentScope.get(varName);

        if (symbol == null) {
            addError("Reference to undefined symbol '%s'", ctx, varName);
            return new Value(Type.Invalid);
        }

        return new Value(symbol.getType());
    }

    @Override
    public Value visitLiteral(SimpleGoParser.LiteralContext ctx) {
        if (ctx.DECIMAL_LIT() != null)
            return new Value(Type.Int);
        if (ctx.STRING_LIT() != null)
            return new Value(Type.String);
        if (ctx.FLOAT_LIT() != null)
            return new Value(Type.Float64);
        if (ctx.BOOLEAN_LIT() != null)
            return new Value(Type.Bool);

        return new Value(Type.Invalid);
    }

    @Override
    public Value visitVarDecl(SimpleGoParser.VarDeclContext ctx) {
        var varName = ctx.IDENTIFIER().getText();
        var type = Type.from(ctx.typeName().getText());

        if (currentScope.get(varName, true) != null) {
            addError("Redeclared variable '%s' in same block", ctx, varName);
            return new Value(Type.Invalid);
        }

        if (ctx.expression() == null) {
            addError("Variable '%s' declared but not defined", ctx, varName);
            return new Value(Type.Invalid);
        }

        var value = visit(ctx.expression());

        if (value == null) {
            addError("Cannot assign invalid type to variable '%s'", ctx, varName);
            return new Value(Type.Invalid);
        }

        if (type != value.getType() && !(type == Type.Float64 && value.getType() == Type.Int)) {
            addError("Cannot assign type '%s' to type '%s'", ctx, value.getType(), type);
            return new Value(Type.Invalid);
        }

        currentScope.put(localCounter, varName, type, false);
        currentFunction.addSymbol(localCounter, varName, type, false);

        if (type == Type.Float64) {
            localCounter += 2;
        } else {
            localCounter += 1;
        }

        return null;
    }

    @Override
    public Value visitBlock(SimpleGoParser.BlockContext ctx) {
        var oldScope = currentScope;
        currentScope = new Environment(currentScope);

        if (ctx.getParent() instanceof SimpleGoParser.FunctionDeclContext) {
            var functionDeclCtx = (SimpleGoParser.FunctionDeclContext) ctx.getParent();
            var funcEntry = functionTable.get(functionDeclCtx.IDENTIFIER().getText());

            currentFunction = funcEntry;
            localCounter = funcEntry.getParameters().size();
            populateFunctionScope(currentScope);
        }

        visitChildren(ctx);

        currentScope = oldScope;

        return null;
    }

    @Override
    public Value visitReturnStmt(SimpleGoParser.ReturnStmtContext ctx) {
        var enclosingFunction = getEnclosingFunction(ctx);

        if (enclosingFunction == null) {
            addError("Could not find enclosing function", ctx);
            return new Value(Type.Invalid);
        }

        Type returnType = enclosingFunction.getReturnType();

        if (returnType != Type.Void) {
            if (ctx.expression() == null) {
                addError("Expected return type '%s' but got 'Void'", ctx, returnType);
                return new Value(Type.Invalid);
            }

            var value = visit(ctx.expression());

            if (!TypecheckUtils.compatibleAssignment(returnType, value.getType())) {
                addError("Expected return type '%s' but got '%s'", ctx, returnType, value.getType());
                return new Value(Type.Invalid);
            }
        } else {
            if (ctx.expression() != null) {
                var value = visit(ctx.expression());
                addError("Expected return type 'Void' but got '%s'", ctx, value.getType());
            }
        }

        return new Value(Type.Invalid);
    }

    @Override
    public Value visitForStmt(SimpleGoParser.ForStmtContext ctx) {
        var conditional = visit(ctx.expression());

        if (conditional.getType() != Type.Bool) {
            addError("Condition in for loop has to be a boolean expression, got '%s' instead", ctx,
                    conditional.getType());
            return new Value(Type.Invalid);
        }

        visit(ctx.block());

        return new Value(Type.Invalid);
    }

    @Override
    public Value visitIfStmt(SimpleGoParser.IfStmtContext ctx) {
        var conditional = visit(ctx.expression());

        if (conditional == null)
            return new Value(Type.Invalid);

        if (conditional.getType() != Type.Bool) {
            addError("Condition in if statement has to be a boolean expression, got '%s' instead", ctx,
                    conditional.getType());
            return new Value(Type.Invalid);
        }

        visitChildren(ctx);

        return new Value(Type.Invalid);
    }

    private boolean typecheckParameters(ParserRuleContext ctx, List<Parameter> formalParams, List<Value> callerParams) {
        var numFormalParams = formalParams.size();
        var numCallerParams = callerParams.size();

        if (numFormalParams != numCallerParams) {
            addError("Function expects %d arguments, but given %d", ctx, formalParams.size(), callerParams.size());
            return false;
        }

        for (int i = 0; i < numCallerParams; i++) {
            var formalParam = formalParams.get(i);
            var callerParam = callerParams.get(i);

            if (!TypecheckUtils.compatibleAssignment(formalParam.getType(), callerParam.getType())
                    && formalParam.getType() != Type.Any) {
                addError("Parameter '%s' expects type '%s', but given '%s'", ctx, formalParam.getName(),
                        formalParam.getType(), callerParam.getType());
                return false;
            }
        }

        return true;
    }

    private List<Parameter> fillFakeParams(List<Parameter> parameters, List<Value> callerParams) {
        var filledParams = new ArrayList<Parameter>();

        for (var callerParam : callerParams) {
            if (callerParam != null)
                filledParams.add(new Parameter("", callerParam.getType()));
        }

        return filledParams;
    }

    private FunctionEntry getEnclosingFunction(SimpleGoParser.ReturnStmtContext ctx) {
        ParserRuleContext current = ctx;
        while (current != null && !(current instanceof SimpleGoParser.FunctionDeclContext))
            current = current.getParent();

        var funcCtx = (SimpleGoParser.FunctionDeclContext) current;

        if (funcCtx != null) {
            return functionTable.get(funcCtx.IDENTIFIER().getText());
        } else {
            addError("Could not find function corresponding to return statement", ctx);
            return null;
        }
    }

    private void populateFunctionScope(Environment currentScope) {
        for (var param : currentFunction.getParameters()) {
            currentScope.put(localCounter, param.getName(), param.getType(), true);
        }
    }
}
