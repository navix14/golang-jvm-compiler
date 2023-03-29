package ast.nodes.expressions.operatortypes;

public enum AdditiveOperatorType {
    ADDITION,
    SUBTRACTION;

    public String toSymbol() {
        return switch (this.name()) {
            case "ADDITION" -> "+";
            case "SUBTRACTION" -> "-";
            default -> "";
        };
    }
}