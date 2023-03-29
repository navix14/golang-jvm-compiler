package typechecking;

import common.Error;

public class TypecheckError extends Error {
    private final int line;

    public TypecheckError(String message, int line) {
        super(message);
        this.line = line;
    }

    @Override
    public String getMessage() {
        return String.format("%s (Line %d)", super.getMessage(), line);
    }
}