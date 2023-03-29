package functionresolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Symbol;
import common.Type;

public class FunctionEntry {
    private final String functionName;
    private final List<Parameter> parameters;
    private final Type returnType;

    private Map<String, Symbol> locals;
    private boolean takesAnyParameters;

    public FunctionEntry(String functionName, List<Parameter> parameters, Type returnType) {
        this.functionName = functionName;
        this.parameters = parameters;
        this.returnType = returnType;
        this.locals = new HashMap<String, Symbol>();
        this.takesAnyParameters = false;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public Type getParameterType(int index) {
        if (parameters != null) {
            return parameters.get(index).getType();
        }

        return Type.Invalid;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void addSymbol(int id, String name, Type type, boolean isParam) {
        locals.put(name, new Symbol(id, name, type, isParam)); 
    }

    public Symbol getSymbol(String name) {
        if (locals.containsKey(name)) {
            return locals.get(name);
        }

        int paramId = 0;

        for (int i = 0; i < parameters.size(); i++) {
            var param = parameters.get(i);

            if (param.getName().equals(name)) {
                return new Symbol(paramId, name, param.getType(), true);
            }

            paramId += param.getType() == Type.Float64 ? 2 : 1;
        }

        return null;
    }

    public int getLocalsWidth() {
        int width = 0;

        for (var local : locals.values()) {
            width += local.getType() == Type.Float64 ? 2 : 1;
        }

        for (var param : parameters) {
            width += param.getType() == Type.Float64 ? 2 : 1;
        }

        return width;
    }

    public int numLocals() {
        return locals.size();
    }

    public void setTakesAnyParameters(boolean value) {
        this.takesAnyParameters = value;
    }

    public boolean takesAnyParameters() {
        return takesAnyParameters;
    }
}
