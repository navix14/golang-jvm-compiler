package common;

public class Symbol {
    private final int id;
    private final String name;
    private final Type type;
    private final boolean isParam;

    public Symbol(int id, String name, Type type, boolean isParam) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isParam = isParam;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public boolean isParam() {
        return isParam;
    }
}
