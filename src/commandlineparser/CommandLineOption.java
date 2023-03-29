package commandlineparser;

public class CommandLineOption {
    private final String name;
    private final boolean requiresValue;
    
    private boolean isSet;
    private boolean isRequired;

    public String value;

    public CommandLineOption(String name, boolean requiresValue) {
        this.name = name;
        this.requiresValue = requiresValue;
        this.isSet = false;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean requiresValue() {
        return requiresValue;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public boolean isActive() {
        return isSet;
    }

    public void setActive() {
        this.isSet = true;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }
}
