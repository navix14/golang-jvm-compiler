package functionresolver;

import common.Type;

public class Parameter {
    private final String name;
    private final Type type;

    public Parameter(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
}
