import org.antlr.v4.runtime.*;

import ast.ASTPrinter;
import ast.ASTVisitor;
import commandlineparser.CommandLineParser;
import common.StringUtil;
import generated.*;

import java.io.IOException;

public class SimpleGoCompiler {
    private static CharStream createInputStream(String path) {
        CharStream inputStream = null;

        try {
            inputStream = CharStreams.fromFileName(path);
        } catch (IOException e) {
            System.out.printf("ERROR: Failed to open file '%s'\n", path);
            System.exit(-1);
        }

        return inputStream;
    }

    private static void printAST(String filePath, boolean printAttrs) {
        var charStream = createInputStream(filePath);
        var lexer = new SimpleGoLexer(charStream);
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new SimpleGoParser(tokenStream);
        var programNode = parser.program();

        var astVisitor = new ASTVisitor();
        var astRoot = astVisitor.visit(programNode);

        var astPrinter = new ASTPrinter(astRoot);
        astPrinter.printAST(printAttrs);
    }

    private static void compileFile(String filePath) {
        var charStream = createInputStream(filePath);
        var goCompiler = new GoCompiler(charStream);

        try {
            goCompiler.compile(StringUtil.extractFilename(filePath));
        } catch (Exception e) {
            System.out.println("Failed to compile: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        CommandLineParser commandLineParser = new CommandLineParser();
        commandLineParser
                .setUsage("java SimpleGoCompiler [-compile | -liveness | -printast <true | false>] <Filename.go>");
        commandLineParser.setHelp("""
                java SimpleGoCompiler [-compile | -liveness | -printast <true | false>] <Filename.go>

                Options:
                  -compile: Compiles an input .go file into Jasmin bytecode.
                  -liveness: Prints out the required number of registers to run program.
                  -printast: Prints the AST. When given 'true', all node attributes are also printed.
                    """);
        commandLineParser.addOption("compile", false);
        commandLineParser.addOption("liveness", false);
        commandLineParser.addOption("printast", true);
        commandLineParser.addOption("help", false);
        commandLineParser.addRequiredOption("filename");

        var cmdLineArgs = commandLineParser.parse(args);

        if (cmdLineArgs == null)
            return;

        var filePath = cmdLineArgs.get("filename");

        if (cmdLineArgs.has("printast")) {
            boolean printAttrs = Boolean.parseBoolean(cmdLineArgs.get("printast"));
            printAST(filePath, printAttrs);
        }

        if (cmdLineArgs.has("compile")) {
            compileFile(filePath);
        }
    }
}