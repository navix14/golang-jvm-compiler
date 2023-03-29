package commandlineparser;

import java.util.Map;

public class CommandLineParserResult {
    private final Map<String, CommandLineOption> options;

    public CommandLineParserResult(Map<String, CommandLineOption> options) {
        this.options = options;
    }
    
    public String get(String optionName) {
        return options.get(optionName).getValue();
    }

    public boolean has(String optionName) {
        return options.containsKey(optionName) && options.get(optionName).isActive();
    }

    public boolean isEmpty() {
        return options.isEmpty();
    }
}
