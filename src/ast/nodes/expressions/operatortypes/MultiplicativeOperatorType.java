package ast.nodes.expressions.operatortypes;

public enum MultiplicativeOperatorType {
    MULTIPLICATION,
    DIVISION,
    MODULO;

    public String toSymbol() {
        return switch (this.name()) {
            case "MULTIPLICATION" -> "*";
            case "DIVISION" -> "/";
            case "MODULO" -> "%";
            default -> "";
        };
    }
}
