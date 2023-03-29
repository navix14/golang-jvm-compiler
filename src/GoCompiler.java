import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import common.Error;
import errorlisteners.LexerErrorListener;
import errorlisteners.ParserErrorListener;
import generated.SimpleGoLexer;
import generated.SimpleGoParser;
import passes.CodegenPass;
import passes.TypecheckPass;

public class GoCompiler {
    private final SimpleGoParser.ProgramContext context;
    private final LexerErrorListener lexerErrorListener;
    private final ParserErrorListener parserErrorListener;
    private final List<Error> typecheckErrors;

    public GoCompiler(CharStream charStream) {
        this.typecheckErrors = new ArrayList<>();
        this.lexerErrorListener = new LexerErrorListener();
        this.parserErrorListener = new ParserErrorListener();
        this.context = antlrSetup(charStream);
    }

    private SimpleGoParser.ProgramContext antlrSetup(CharStream charStream) {
        var lexer = new SimpleGoLexer(charStream);
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new SimpleGoParser(tokenStream);

        lexer.addErrorListener(lexerErrorListener);
        parser.addErrorListener(parserErrorListener);

        var ctx = parser.program();
        return ctx;
    }

    private void printParserErrors() {
        for (var error : lexerErrorListener.getLexerErrors())
            System.out.printf("  Lexer error: %s\n", error);

        for (var error : parserErrorListener.getSyntaxErrors())
            System.out.printf("  Parser error: %s\n", error);
    }

    private void printTypecheckErrors() {
        for (var error : typecheckErrors)
            System.out.printf("  Typecheck error: %s\n", error.getMessage());
    }

    public void compile(String fileName) throws IOException {
        if (hasErrors()) {
            System.out.printf("Failed to parse '%s':\n", fileName);
            printParserErrors();
            return;
        }

        var typecheckPass = new TypecheckPass(context);
        typecheckPass.execute();

        typecheckErrors.addAll(typecheckPass.getErrors());

        if (hasErrors()) {
            System.out.printf("Failed to typecheck '%s':\n", fileName);
            printTypecheckErrors();
            return;
        }

        var codegenPass = new CodegenPass(context, fileName, typecheckPass.getFunctionTable());
        codegenPass.execute();

        Files.writeString(Path.of(fileName + ".j"), codegenPass.generatedCode());

        System.out.printf("Successfully compiled '%s.go' to '%s.j'\n", fileName,
                fileName);
    }

    private boolean hasErrors() {
        return !typecheckErrors.isEmpty() || !lexerErrorListener.getLexerErrors().isEmpty()
                || !parserErrorListener.getSyntaxErrors().isEmpty();
    }
}
