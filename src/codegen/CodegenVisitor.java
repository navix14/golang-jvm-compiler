package codegen;

import java.util.Map;

import org.antlr.v4.runtime.tree.RuleNode;

import common.Type;
import functionresolver.FunctionEntry;
import generated.SimpleGoBaseVisitor;
import generated.SimpleGoParser.AdditiveExpressionContext;
import generated.SimpleGoParser.ArgumentsContext;
import generated.SimpleGoParser.AssignmentContext;
import generated.SimpleGoParser.ForStmtContext;
import generated.SimpleGoParser.FunctionDeclContext;
import generated.SimpleGoParser.IfStmtContext;
import generated.SimpleGoParser.LiteralContext;
import generated.SimpleGoParser.LogicalAndExpressionContext;
import generated.SimpleGoParser.LogicalOrExpressionContext;
import generated.SimpleGoParser.MultiplicativeExpressionContext;
import generated.SimpleGoParser.OperandContext;
import generated.SimpleGoParser.OperandNameContext;
import generated.SimpleGoParser.PrimaryExprContext;
import generated.SimpleGoParser.PrimaryExpressionContext;
import generated.SimpleGoParser.ProgramContext;
import generated.SimpleGoParser.RelationalExpressionContext;
import generated.SimpleGoParser.ReturnStmtContext;
import generated.SimpleGoParser.UnaryExpressionContext;
import generated.SimpleGoParser.VarDeclContext;

public class CodegenVisitor extends SimpleGoBaseVisitor<SubtreeCodegenResult> {
    private final Map<String, FunctionEntry> functionTable;
    private final String fileName;

    private FunctionEntry currentFunction;

    private boolean hasReturn;
    private StringBuilder code;
    private int labelCounter;

    public CodegenVisitor(String fileName, Map<String, FunctionEntry> functionTable) {
        this.fileName = fileName;
        this.functionTable = functionTable;
        this.currentFunction = null;
        this.code = new StringBuilder();
        this.labelCounter = 0;
        this.hasReturn = false;
    }

    public String getCode() {
        return code.toString();
    }

    private static String getJasminType(Type type) {
        switch (type) {
            case Int:
                return "I";
            case Float64:
                return "D";
            case Bool:
                return "Z";
            case String:
                return "Ljava/lang/String;";
            case Void:
                return "V";
            default:
                return null;
        }
    }

    private static String getLoadPrefix(Type type) {
        switch (type) {
            case Int:
                return "i";
            case Float64:
                return "d";
            case Bool:
                return "i";
            case String:
                return "a";
            default:
                return "";
        }
    }

    private static String generateTypeConversion(SubtreeCodegenResult left, SubtreeCodegenResult right) {
        String result = "";
        boolean needsCast = left.getType() == Type.Float64 || right.getType() == Type.Float64;

        result += left.getCode();

        if (left.getType() == Type.Int && needsCast) {
            result += "  i2d\n";
        }

        result += right.getCode();

        if (right.getType() == Type.Int && needsCast) {
            result += "  i2d\n";
        }

        return result;
    }

    private String newLabel() {
        return "L" + labelCounter++;
    }

    private String generateBooleanExpression(String comparisonOp, Type type) {
        String result = "";
        String labelTrue = newLabel();
        String labelDone = newLabel();

        switch (type) {
            case Int:
            case Bool:
                result += "  isub\n";
                break;
            case Float64: 
                result += "  dsub\n  d2i\n";
                break;
            case String:
                result += "  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z\n";

                // Invert comparison operator
                if (comparisonOp.equals("ifeq")) comparisonOp = "ifne";
                else if (comparisonOp.equals("ifne")) comparisonOp = "ifeq";
            default: break;
        }

        result += String.format("  %s %s\n", comparisonOp, labelTrue);
        result += "  ldc 0\n";
        result += "  goto " + labelDone + "\n";
        result += labelTrue + ":\n";
        result += "  ldc 1\n";
        result += labelDone + ":\n";

        return result;
    }

    @Override
    public SubtreeCodegenResult visitChildren(RuleNode arg0) {
        SubtreeCodegenResult result = new SubtreeCodegenResult("", Type.Void);

        for (int i = 0; i < arg0.getChildCount(); i++) {
            var child = arg0.getChild(i);
            var subtree = visit(child);

            if (subtree == null)
                continue;

            result.setCode(result.getCode().concat(subtree.getCode()));

            // Return the type of the last subtree
            // Needed for peeking subtree types if type conversions required
            result.setType(subtree.getType());
        }

        return result;
    }

    @Override
    public SubtreeCodegenResult visitProgram(ProgramContext ctx) {
        // Metadata
        code.append(String.format(".source %s.java\n", fileName));
        code.append(String.format(".class public %s\n", fileName));
        code.append(".super java/lang/Object\n\n");

        // Initialize class
        code.append(".method public <init>()V\n");
        code.append("  .limit stack 1\n");
        code.append("  .limit locals 1\n");
        code.append("  aload_0\n");
        code.append("  invokespecial java/lang/Object/<init>()V\n");
        code.append("  return\n");
        code.append(".end method\n\n");

        return visitChildren(ctx);
    }

    @Override
    public SubtreeCodegenResult visitFunctionDecl(FunctionDeclContext ctx) {
        var functionName = ctx.IDENTIFIER().getText();
        currentFunction = functionTable.get(functionName);

        hasReturn = false;

        String jasminParams = currentFunction.getParameters()
                .stream()
                .map(p -> getJasminType(p.getType()))
                .reduce("", (res, p) -> res + p);

        String jasminReturnType = getJasminType(currentFunction.getReturnType());

        int numLocals = currentFunction.getLocalsWidth();

        if (functionName.equals("main")) {
            code.append(".method public static main([Ljava/lang/String;)V\n");
        } else {
            code.append(
                    String.format(".method private static %s(%s)%s\n",
                            functionName, jasminParams, jasminReturnType));
        }

        code.append("  .limit stack 100\n");
        code.append(String.format("  .limit locals %d\n", numLocals));

        code.append(visitChildren(ctx).getCode());

        if (!hasReturn) {
            code.append("  return\n");
        }

        code.append(".end method\n\n");

        return new SubtreeCodegenResult("", Type.Invalid);
    }

    @Override
    public SubtreeCodegenResult visitLiteral(LiteralContext ctx) {
        if (ctx.DECIMAL_LIT() != null) {
            return new SubtreeCodegenResult(String.format("  ldc %s\n", ctx.getText()), Type.Int);
        }

        if (ctx.STRING_LIT() != null) {
            return new SubtreeCodegenResult(String.format("  ldc %s\n", ctx.getText()), Type.String);
        }

        if (ctx.FLOAT_LIT() != null) {
            return new SubtreeCodegenResult(String.format("  ldc2_w %s\n", ctx.getText()), Type.Float64);
        }

        if (ctx.BOOLEAN_LIT() != null) {
            String result = "";

            if (ctx.getText().equals("true"))
                result = "  iconst_1\n";
            else
                result = "  iconst_0\n";

            return new SubtreeCodegenResult(result, Type.Bool);
        }

        return new SubtreeCodegenResult("", Type.Invalid);
    }

    @Override
    public SubtreeCodegenResult visitMultiplicativeExpression(MultiplicativeExpressionContext ctx) {
        String result = "";
        String prefix = "i";

        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));

        result += generateTypeConversion(left, right);

        if (left.getType() == Type.Float64 || right.getType() == Type.Float64) {
            prefix = "d";
        }

        switch (ctx.mul_op.getText()) {
            case "*" -> {
                result += "  " + prefix + "mul\n";
            }
            case "/" -> {
                result += "  " + prefix + "div\n";
            }
            case "%" -> {
                result += "  irem\n";
            }
        }

        return new SubtreeCodegenResult(result, left.getType() == Type.Float64 ? left.getType() : right.getType());
    }

    @Override
    public SubtreeCodegenResult visitAdditiveExpression(AdditiveExpressionContext ctx) {
        String prefix = "i";
        String result = "";

        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));

        result += generateTypeConversion(left, right);

        if (left.getType() == Type.Float64 || right.getType() == Type.Float64) {
            prefix = "d";
        }

        switch (ctx.add_op.getText()) {
            case "+" -> {
                if (left.getType() == Type.String) {
                    result += "  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;\n";
                } else {
                    result += "  " + prefix + "add\n";
                }
            }
            case "-" -> {
                result += "  " + prefix + "sub\n";
            }
        }

        return new SubtreeCodegenResult(result, left.getType() == Type.Float64 ? left.getType() : right.getType());
    }

    @Override
    public SubtreeCodegenResult visitUnaryExpression(UnaryExpressionContext ctx) {
        String result = "";

        var expr = visit(ctx.expression());
        result += expr.getCode();

        switch (ctx.unary_op.getText()) {
            case "-" -> {
                if (expr.getType() == Type.Float64) {
                    result += "  dneg\n";
                } else {
                    result += "  ineg\n";
                }
            }
            case "!" -> {
                result += "  ldc 1\n";
                result += "  iadd\n";
                result += "  ldc 2\n";
                result += "  irem\n";
            }
        }

        return new SubtreeCodegenResult(result, expr.getType());
    }

    @Override
    public SubtreeCodegenResult visitOperand(OperandContext ctx) {
        if (ctx.literal() != null) {
            return visit(ctx.literal());
        }

        if (ctx.operandName() != null) {
            return visit(ctx.operandName());
        }

        if (ctx.L_PAREN() != null) {
            return visit(ctx.expression());
        }

        return new SubtreeCodegenResult("", Type.Invalid);
    }

    private SubtreeCodegenResult handleArguments(ArgumentsContext ctx, FunctionEntry callee) {
        String result = "";

        if (ctx.expressionList() == null) {
            return new SubtreeCodegenResult(result, Type.Invalid);
        }

        var arguments = ctx.expressionList().expression();
        Type lastArgType = Type.Void;

        for (int i = 0; i < arguments.size(); i++) {
            var expr = visit(arguments.get(i));
            var paramType = callee.getParameterType(i);

            result += expr.getCode();

            if (expr.getType() == Type.Int && paramType == Type.Float64) {
                result += "  i2d\n";
            }

            lastArgType = expr.getType();
        }

        return new SubtreeCodegenResult(result, lastArgType);
    }

    @Override
    public SubtreeCodegenResult visitPrimaryExpr(PrimaryExprContext ctx) {
        if (ctx.operand() != null) {
            return visit(ctx.operand());
        }

        if (ctx.arguments() != null) {
            String functionName = ctx.primaryExpr().getText();
            String result = "";
            var function = functionTable.get(functionName);

            boolean isPrint = functionName.equalsIgnoreCase("fmt.Println");

            if (isPrint) {
                result += "  getstatic java/lang/System/out Ljava/io/PrintStream;\n";
            }

            // push arguments on stack
            var params = handleArguments(ctx.arguments(), function);
            result += params.getCode();

            if (isPrint) {
                result += String.format("  invokevirtual java/io/PrintStream/println(%s)V\n", getJasminType(params.getType()));
            } else {
                result += String.format("  invokestatic %s/%s(%s)%s\n",
                fileName, functionName,
                function.getParameters()
                        .stream()
                        .map(p -> getJasminType(p.getType()))
                        .reduce("", (res, p) -> res + p),
                getJasminType(function.getReturnType())); 
            }

            return new SubtreeCodegenResult(result, function.getReturnType());
        }

        return new SubtreeCodegenResult("", Type.Invalid);
    }

    @Override
    public SubtreeCodegenResult visitPrimaryExpression(PrimaryExpressionContext ctx) {
        return visit(ctx.primaryExpr());
    }

    @Override
    public SubtreeCodegenResult visitOperandName(OperandNameContext ctx) {
        var varName = ctx.IDENTIFIER().getText();
        var symbol = currentFunction.getSymbol(varName);
        String result = String.format("  %sload %d\n", getLoadPrefix(symbol.getType()), symbol.getId());

        return new SubtreeCodegenResult(result, symbol.getType());
    }

    @Override
    public SubtreeCodegenResult visitReturnStmt(ReturnStmtContext ctx) {
        SubtreeCodegenResult result = new SubtreeCodegenResult("", Type.Void);

        if (ctx.expression() != null) {
            result = visit(ctx.expression());
        }

        result.setCode(result.getCode().concat(String.format("  %sreturn\n", getLoadPrefix(result.getType()))));

        hasReturn = true;

        return result;
    }

    @Override
    public SubtreeCodegenResult visitVarDecl(VarDeclContext ctx) {
        var varName = ctx.IDENTIFIER().getText();
        var symbol = currentFunction.getSymbol(varName);

        String result = "";

        // Generate code for RHS
        var rhsResult = visit(ctx.expression());

        result += rhsResult.getCode();

        // Convert int to float if needed
        if (symbol.getType() == Type.Float64 && rhsResult.getType() == Type.Int) {
            result += "  i2d\n";
        }

        // Write store command
        result += String.format("  %sstore %d\n", getLoadPrefix(symbol.getType()), symbol.getId());

        // Store result in local variable
        return new SubtreeCodegenResult(result, symbol.getType());
    }

    @Override
    public SubtreeCodegenResult visitRelationalExpression(RelationalExpressionContext ctx) {
        String result = "";

        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));

        boolean needsCast = left.getType() == Type.Float64 || right.getType() == Type.Float64;
        result += generateTypeConversion(left, right);

        switch (ctx.rel_op.getText()) {
            case "==" -> {
                result += generateBooleanExpression("ifeq", needsCast ? Type.Float64 : left.getType());
            }
            case "!=" -> {
                result += generateBooleanExpression("ifne", needsCast ? Type.Float64 : left.getType());
            }
            case ">" -> {
                result += generateBooleanExpression("ifgt", needsCast ? Type.Float64 : left.getType());
            }
            case ">=" -> {
                result += generateBooleanExpression("ifge", needsCast ? Type.Float64 : left.getType());
            }
            case "<" -> {
                result += generateBooleanExpression("iflt", needsCast ? Type.Float64 : left.getType());
            }
            case "<=" -> {
                result += generateBooleanExpression("ifle", needsCast ? Type.Float64 : left.getType());
            }
        }

        return new SubtreeCodegenResult(result, Type.Bool);
    }

    @Override
    public SubtreeCodegenResult visitLogicalAndExpression(LogicalAndExpressionContext ctx) {
        String result = "";

        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));

        result += left.getCode();
        result += right.getCode();

        result += "  iand\n";

        return new SubtreeCodegenResult(result, Type.Bool);
    }

    @Override
    public SubtreeCodegenResult visitLogicalOrExpression(LogicalOrExpressionContext ctx) {
        String result = "";

        var left = visit(ctx.expression(0));
        var right = visit(ctx.expression(1));

        result += left.getCode();
        result += right.getCode();

        result += "  ior\n";

        return new SubtreeCodegenResult(result, Type.Bool);
    }

    @Override
    public SubtreeCodegenResult visitAssignment(AssignmentContext ctx) {
        String result = "";
        var symbol = currentFunction.getSymbol(ctx.expression(0).getText());
        var rhs = visit(ctx.expression(1));

        // Generate RHS
        result += rhs.getCode();
        
        if (symbol.getType() == Type.Float64 && rhs.getType() == Type.Int) {
            result += "  i2d\n";
        }

        result += String.format("  %sstore %d\n", getLoadPrefix(symbol.getType()), symbol.getId());

        return new SubtreeCodegenResult(result, symbol.getType());
    }

    @Override
    public SubtreeCodegenResult visitIfStmt(IfStmtContext ctx) {
        String result = "";

        // Handle normal if-statement
        if (ctx.ELSE() == null) {
            String label = newLabel();

            // Generate code for condition
            result += visit(ctx.expression()).getCode();

            // If condition is false, skip then-block
            result += "  ifeq " + label + "\n";

            // Generate code for then-block
            result += visit(ctx.block(0)).getCode();

            result += label + ":\n";
        } else { // Handle if-else-statement
            String labelElse = newLabel();
            String labelEnd = newLabel();

            result += visit(ctx.expression()).getCode();

            result += "  ifeq " + labelElse + "\n";

            result += visit(ctx.block(0)).getCode();

            result += "  goto " + labelEnd + "\n";

            result += labelElse + ":\n";

            if (ctx.ifStmt() != null) {
                result += visit(ctx.ifStmt()).getCode();
            } else {
                result += visit(ctx.block(1)).getCode();
            }

            result += labelEnd + ":\n";
        }

        return new SubtreeCodegenResult(result, Type.Void);
    }

    @Override
    public SubtreeCodegenResult visitForStmt(ForStmtContext ctx) {
        String result = "";

        String labelLoop = newLabel();
        String labelDone = newLabel();

        var conditionCode = visit(ctx.expression()).getCode();
        var bodyCode = visit(ctx.block()).getCode();

        result += labelLoop + ":\n";
        result += conditionCode;
        result += "  ifeq " + labelDone + "\n";
        result += bodyCode;
        result += "  goto " + labelLoop + "\n";
        result += labelDone + ":\n";

        return new SubtreeCodegenResult(result, Type.Void);
    }
}
