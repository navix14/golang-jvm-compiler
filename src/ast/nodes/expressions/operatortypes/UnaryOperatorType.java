package ast.nodes.expressions.operatortypes;

public enum UnaryOperatorType {
    PLUS,
    MINUS,
    LOGICAL_NOT;

    public String toSymbol() {
        return switch (this.name()) {
            case "PLUS" -> "+";
            case "MINUS" -> "-";
            case "LOGICAL_NOT" -> "!";
            default -> "";
        };
    }
}