package passes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ParserRuleContext;

import codegen.CodegenVisitor;
import common.Error;
import functionresolver.FunctionEntry;

public class CodegenPass implements IPass {
    private final ParserRuleContext context;
    private final CodegenVisitor codegenVisitor;
    private String generatedCode;

    public CodegenPass(ParserRuleContext context, String fileName, Map<String, FunctionEntry> functionTables) {
        this.context = context;
        this.codegenVisitor = new CodegenVisitor(fileName, functionTables);
        this.generatedCode = null;
    }

    private String removeUnreachableLabels(String code) {
        var lines = new ArrayList<String>(Arrays.asList(code.split("\n")));

        Set<String> removeLabel = new HashSet<>();

        // First pass: Find & remove useless labels
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            Pattern p = Pattern.compile("L(\\d)+:");
            Matcher m = p.matcher(line);

            if (m.find() && i < lines.size() - 1) {
                String nextLine = lines.get(i + 1);
                String labelNum = m.group(1);

                // Unreachable label
                if (nextLine.contains(".end method")) {
                    lines.remove(i);
                    removeLabel.add("L" + labelNum);
                }
            }
        }

        // Second pass: Remove all branch instructions targeting useless labels
        for (int i = 0; i < lines.size(); i++) {
            for (String label : removeLabel) {
                if (lines.get(i).endsWith(label)) {
                    lines.remove(i);
                }
            }
        }

        return String.join("\n", lines);
    }

    public String generatedCode() {
        if (generatedCode == null) {
            execute();
        }
        
        return generatedCode;
    }

    @Override
    public void execute() {
        codegenVisitor.visit(context);

        String result = codegenVisitor.getCode();
        generatedCode = removeUnreachableLabels(result);
    }

    @Override
    public List<Error> getErrors() {
        return null;
    }
}
