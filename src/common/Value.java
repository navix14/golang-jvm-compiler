package common;

public class Value {
    private final Type type;
    private final boolean isSymbol;

    public Value(Type type) {
        this.type = type;
        this.isSymbol = false;
    }

    public Value(Type type, boolean isSymbol) {
        this.type = type;
        this.isSymbol = isSymbol;
    }

    public Type getType() {
        return type;
    }

    public boolean isNumeric() {
        return type == Type.Int || type == Type.Float64;
    }

    public boolean isSymbol() {
        return isSymbol;
    }
}
