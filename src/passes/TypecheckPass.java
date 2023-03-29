package passes;

import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

import common.Error;
import common.Type;
import functionresolver.FunctionEntry;
import functionresolver.FunctionVisitor;
import typechecking.TypecheckVisitor;

public class TypecheckPass implements IPass {
    private final ParserRuleContext context;
    private TypecheckVisitor typecheckVisitor;
    private Map<String, FunctionEntry> functionTable;

    public TypecheckPass(ParserRuleContext context) {
        this.context = context;
    }

    public Map<String, FunctionEntry> getFunctionTable() {
        return functionTable;
    }

    private void addPredefinedFunctions(Map<String, FunctionEntry> functionTable) {
        var func = new FunctionEntry("fmt.Println", null, Type.Void);
        func.setTakesAnyParameters(true);

        functionTable.put("fmt.Println", func);
    }

    private Map<String, FunctionEntry> resolveFunctions() {
        FunctionVisitor functionVisitor = new FunctionVisitor();
        functionVisitor.visit(context);

        functionTable = functionVisitor.getFunctionTable();
        addPredefinedFunctions(functionTable);

        return functionTable;
    }

    @Override
    public void execute() {
        var functionTable = resolveFunctions();
        typecheckVisitor = new TypecheckVisitor(functionTable);
        typecheckVisitor.visit(context);
    }

    @Override
    public List<Error> getErrors() {
        return typecheckVisitor.getErrors();
    }
}
