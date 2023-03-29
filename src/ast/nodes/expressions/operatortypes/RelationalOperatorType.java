package ast.nodes.expressions.operatortypes;

public enum RelationalOperatorType {
    EQUALS,
    NOT_EQUALS,
    LESS,
    LESS_OR_EQUALS,
    GREATER,
    GREATER_OR_EQUALS;

    public String toSymbol() {
        return switch (this.name()) {
            case "EQUALS" -> "==";
            case "NOT_EQUALS" -> "!=";
            case "LESS" -> "<";
            case "LESS_OR_EQUALS" -> "<=";
            case "GREATER" -> ">";
            case "GREATER_OR_EQUALS" -> ">=";
            default -> "";
        };
    }
}
