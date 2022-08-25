package com.inno.accpa.compiler.syntax_analysis.node;

import com.inno.accpa.compiler.interpreter.AtomsTable;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionAtom implements ElementInterface {

    private final List<AtomNode> arguments;
    private final ElementInterface body;
    List<ElementInterface> parameters;


    public FunctionAtom(List<AtomNode> arguments, ElementInterface body) {
        this.arguments = arguments;
        this.body = body;
    }

    public static boolean checkFunctionArguments(ElementInterface argumentsList) {
        try {
            if (argumentsList instanceof ListNode) {
                for (ElementInterface atomArgument : ((ListNode) argumentsList).elements) {
                    try {
                        if (!(atomArgument instanceof AtomNode)) {
                            return false;
                        }
                    } catch (ClassCastException e) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (ClassCastException e) {
            return false;
        }
    }

    public static List<AtomNode> getListFunctionArguments(ElementInterface argumentsList) {
        return ((ListNode) argumentsList).elements.stream().map(a -> (AtomNode) a).collect(Collectors.toList());
    }

    @Override
    public ElementInterface evaluate() {
        AtomsTable.getInstance().introduceLocalContext();
        for (int i = 0; i < parameters.size(); i++) {
            AtomsTable.getInstance().addAtom(new AtomNode(arguments.get(i).name, parameters.get(i).evaluate()));
        }
        ElementInterface result = body.evaluate();
        AtomsTable.getInstance().leaveLocalContext();
        return result;
    }

    public int getArgumentsNumber() {
        return arguments.size();
    }

    public void setParameters(List<ElementInterface> parameters) {
        //In case of currying
        /*
        if (parameters.size() > this.arguments.size()){
            throw new RuntimeException("Too much");
        }
         */

        if (parameters.size() < this.arguments.size()) {
            throw new RuntimeException("Expected number of arguments doesn't match the given number.\n"
                    + "Expected: " + this.arguments.size() + ". Actual: " + parameters.size());
        }
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "_Function";
    }
}
