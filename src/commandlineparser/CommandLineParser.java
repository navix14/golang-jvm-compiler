package commandlineparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandLineParser {
    private final Map<String, CommandLineOption> options;
    private final List<String> errors;

    private String usage;
    private String help;
    private CommandLineOption requiredOption;

    public CommandLineParser() {
        this.options = new HashMap<>();
        this.errors = new ArrayList<>();
        this.requiredOption = null;
    }

    public void addOption(String name, boolean requiresValue) {
        options.put(name, new CommandLineOption(name, requiresValue));
    }

    public void addRequiredOption(String name) {
        requiredOption = new CommandLineOption(name, false);
        requiredOption.setRequired(true);
        options.put(name, requiredOption);
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    private boolean isHelp(String[] args) {
        for (var arg : args)
            if (arg.contains("-help"))
                return true;

        return false;
    }

    public CommandLineParserResult parse(String[] args) {
        if (isHelp(args)) {
            System.out.println(help);
            return null;
        }

        if (requiredOption != null) {
            if (args.length == 0 || args[args.length - 1].startsWith("-")) {
                errors.add(String.format("Last argument should correspond to the required option '%s'",
                        requiredOption.getName()));
            } else {
                requiredOption.setValue(args[args.length - 1]);
                requiredOption.setActive();
            }
        }

        for (int i = 0; i < args.length; i++) {
            String arg = extractOption(args[i]);

            if (!options.containsKey(arg)) {
                if (i != args.length - 1 || (i == args.length - 1 && requiredOption == null)) {
                    errors.add(String.format("Unknown argument '%s' provided", arg));
                }
            } else {
                var option = options.get(arg);
                option.setActive();

                if (option.requiresValue()) {
                    if (i + 1 >= args.length || args[i + 1].startsWith("-")
                            || (i + 1 == args.length - 1 && requiredOption != null)) {
                        errors.add(String.format("Option '%s' requires argument", option.getName()));
                        continue;
                    }

                    String value = args[i + 1];
                    option.setValue(value);
                    i++;
                }
            }
        }

        if (!errors.isEmpty()) {
            error();
            return null;
        }

        return new CommandLineParserResult(options);
    }

    private String extractOption(String arg) {
        if (arg.startsWith("--"))
            return arg.substring(2);
        else if (arg.startsWith("-"))
            return arg.substring(1);
        else
            return arg;
    }

    private void error() {
        errors.forEach(System.out::println);
        System.out.println("Usage: " + usage);
    }
}