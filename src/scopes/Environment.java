package scopes;

import java.util.HashMap;
import java.util.Map;

import common.Symbol;
import common.Type;

public class Environment {
    private final Environment parent;
    private final Map<String, Symbol> symbols;

    public Environment(Environment parent) {
        this.parent = parent;
        this.symbols = new HashMap<>();
    }

    public Symbol get(String varName) {
        if (symbols.containsKey(varName))
            return symbols.get(varName);

        if (parent != null)
            return parent.get(varName, false);
        else
            return null;
    }

    public Symbol get(String varName, boolean sameScope) {
        if (symbols.containsKey(varName))
            return symbols.get(varName);

        if (parent != null && !sameScope)
            return parent.get(varName, false);
        else
            return null;
    }

    public void put(int id, String varName, Type type, boolean isParam) {
        symbols.put(varName, new Symbol(id, varName, type, isParam));
    }
}
