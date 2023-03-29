package common;

public enum Type {
    Int("int"),
    Float64("float64"),
    String("string"),
    Bool("bool"),
    Void("void"),
    Invalid("invalid"),
    Any("any");

    private final String typeName;

    Type(java.lang.String typeName) {
        this.typeName = typeName;
    }

    public boolean isNumeric() {
        return this == Type.Int || this == Type.Float64;
    }

    public static Type from(String typeName) {
        for (var type : values()) {
            if (type.typeName.equals(typeName))
                return type;
        }

        return Type.Invalid;
    }
}