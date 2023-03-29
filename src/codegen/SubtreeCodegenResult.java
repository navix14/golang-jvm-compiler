package codegen;

import common.Type;

public class SubtreeCodegenResult {
    private String code;
    private Type type;

    public SubtreeCodegenResult(String code, Type type) {
        this.code = code;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public Type getType() {
        return type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
