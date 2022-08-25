package com.inno.accpa.compiler.interpreter;

import com.inno.accpa.compiler.syntax_analysis.node.AST;

public class Interpreter {
    private final AST ast;
    private final boolean logging;

    public Interpreter(final AST ast) {
        this(ast, false);
    }

    public Interpreter(final AST ast, final boolean logging) {
        this.ast = ast;
        this.logging = logging;
    }

    public String interpret() {
        AtomsTable.getInstance().resetContext();
        FunctionsTable.getInstance().resetTable();
        NestedFormBreak.getInstance().resetNestedForm();
        return ast.evaluate();
    }
}
