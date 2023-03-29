package functionresolver;

import generated.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import common.Type;

public class FunctionVisitor extends SimpleGoBaseVisitor<Object> {
    private final Map<String, FunctionEntry> functionTable;

    public FunctionVisitor() {
        this.functionTable = new HashMap<>();
    }

    public Map<String, FunctionEntry> getFunctionTable() {
        return functionTable;
    }

    @Override
    public Object visitFunctionDecl(SimpleGoParser.FunctionDeclContext ctx) {
        var functionName = ctx.IDENTIFIER().getText();
        var signatureCtx = ctx.signature();
        var paramsCtx = signatureCtx.parameters();

        var parameters = new ArrayList<Parameter>();

        // Process function parameters
        for (var paramDeclaration : paramsCtx.parameterDecl()) {
            var paramName = paramDeclaration.IDENTIFIER().getText();
            var paramType = Type.from(paramDeclaration.typeName().getText());
            parameters.add(new Parameter(paramName, paramType));
        }

        if (functionName.equals("main")) {
            parameters.add(new Parameter("args", Type.Int));
        }

        // Process function return type
        Type returnType = Type.Void;
        if (signatureCtx.typeName() != null)
            returnType = Type.from(signatureCtx.typeName().getText());

        // Add function to table
        functionTable.put(functionName, new FunctionEntry(functionName, parameters, returnType));

        // Don't visit any further
        return null;
    }
}
