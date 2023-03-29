package typechecking;

import common.Type;
import common.Value;

public class TypecheckUtils {
    public static boolean compatibleAssignment(Type to, Type from) {
        return (to == Type.Int && from == Type.Int)
            || (to == Type.Float64 && from == Type.Int)
            || (to == Type.Float64 && from == Type.Float64)
            || (to == from);
    }

    public static boolean compatibleMultiplicative(Value v1, Value v2) {
        return v1.isNumeric() && v2.isNumeric();
    }

    public static boolean compatibleMultiplicative(Type t1, Type t2) {
        return t1.isNumeric() && t2.isNumeric();
    }

    public static boolean compatibleAdditive(Value v1, Value v2) {
        return (v1.isNumeric() && v2.isNumeric())
            || (v1.getType() == Type.String && v2.getType() == Type.String);
    }

    public static boolean compatibleAdditive(Type t1, Type t2) {
        return (t1.isNumeric() && t2.isNumeric())
            || (t1 == Type.String && t2 == Type.String);
    }

    public static boolean compatibleRelational(Value v1, Value v2) {
        return (v1.isNumeric() && v2.isNumeric())
            || (v1.getType() == v2.getType());
    }

    public static boolean compatibleRelational(Type t1, Type t2) {
        return (t1.isNumeric() && t2.isNumeric())
            || (t1 == t2);
    }

    public static Value coerce(Value v1, Value v2) {
        if (!v1.isNumeric() || !v2.isNumeric())
            return new Value(Type.Invalid);

        return v1.getType() == Type.Float64 ? v1 : v2;
    }

    public static Type coerce(Type t1, Type t2) {
        if (!t1.isNumeric() || !t2.isNumeric())
            return Type.Invalid;

        return t1 == Type.Float64 ? t1 : t2;
    }
}
