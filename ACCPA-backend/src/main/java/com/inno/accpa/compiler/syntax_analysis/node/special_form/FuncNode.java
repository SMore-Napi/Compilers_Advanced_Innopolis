package com.inno.accpa.compiler.syntax_analysis.node.special_form;

import com.inno.accpa.compiler.interpreter.AtomsTable;
import com.inno.accpa.compiler.interpreter.FunctionsTable;
import com.inno.accpa.compiler.syntax_analysis.node.AtomNode;
import com.inno.accpa.compiler.syntax_analysis.node.ElementInterface;
import com.inno.accpa.compiler.syntax_analysis.node.FunctionAtom;

public class FuncNode implements ElementInterface {
    AtomNode functionName;
    ElementInterface argumentsList;
    ElementInterface functionBody;

    public FuncNode(AtomNode functionName, ElementInterface argumentsList, ElementInterface functionBody) {
        this.functionName = functionName;
        this.argumentsList = argumentsList;
        this.functionBody = functionBody;
    }

    @Override
    public ElementInterface evaluate() {
        if (!FunctionAtom.checkFunctionArguments(argumentsList)) {
            throw new RuntimeException("The second argument should contain a number of atoms that represent " +
                    "the function parameters: " + argumentsList);
        }
        if (FunctionsTable.getInstance().contains(functionName.name)) {
            throw new RuntimeException("The function is already defined: " + functionName.name);
        }
        if (AtomsTable.getInstance().contains(functionName.name)) {
            throw new RuntimeException("Can't name a function with already defined identifier name: " + functionName.name);
        }
        FunctionAtom function = new FunctionAtom(FunctionAtom.getListFunctionArguments(argumentsList), functionBody);
        functionName.value = function;
        FunctionsTable.getInstance().addFunction(functionName.name, function);
        return null;
    }

    @Override
    public String toString() {
        return "FuncNode{" +
                "atom=" + functionName +
                ", list=" + argumentsList +
                ", element=" + functionBody +
                '}';
    }
}
