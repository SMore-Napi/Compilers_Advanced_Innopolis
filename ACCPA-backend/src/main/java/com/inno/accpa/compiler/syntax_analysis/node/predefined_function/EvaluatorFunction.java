package com.inno.accpa.compiler.syntax_analysis.node.predefined_function;

import com.inno.accpa.compiler.interpreter.DefinedFunction;
import com.inno.accpa.compiler.interpreter.PredefinedFunction;
import com.inno.accpa.compiler.syntax_analysis.node.ElementInterface;
import com.inno.accpa.compiler.syntax_analysis.node.FunctionAtom;
import com.inno.accpa.compiler.syntax_analysis.node.ListNode;

import java.util.ArrayList;
import java.util.List;

public class EvaluatorFunction implements ElementInterface {
    ElementInterface argument;

    public EvaluatorFunction(ElementInterface argument) {
        this.argument = argument;
    }

    @Override
    public ElementInterface evaluate() {
        ElementInterface evaluatedArgument = argument.evaluate();
        try {
            if (evaluatedArgument instanceof ListNode) {
                List<ElementInterface> elementsList = ((ListNode) evaluatedArgument).elements;
                PredefinedFunction predefinedFunction = new PredefinedFunction(elementsList);
                if (predefinedFunction.isPredefinedFunction()) {
                    return predefinedFunction.performFunctionAction();
                }
                DefinedFunction definedFunction = new DefinedFunction(elementsList);
                if (definedFunction.isDefinedFunction()) {
                    return definedFunction.performFunctionAction();
                }
                if (!(elementsList.isEmpty())) {
                    if (elementsList.get(0) instanceof FunctionAtom) {
                        FunctionAtom functionAtom = (FunctionAtom) elementsList.get(0);
                        functionAtom.setParameters(elementsList.subList(1,
                                1 + Math.min(functionAtom.getArgumentsNumber(), elementsList.size())));
                        ElementInterface functionResult = functionAtom.evaluate();
                        if (functionAtom.getArgumentsNumber() == elementsList.size()) {
                            return functionResult;
                        } else if (functionAtom.getArgumentsNumber() < elementsList.size()) {
                            List<ElementInterface> evaluatedListNode = new ArrayList<>();
                            evaluatedListNode.add(functionResult);
                            evaluatedListNode.addAll(elementsList
                                    .subList(1 + functionAtom.getArgumentsNumber(), elementsList.size()));
                            return new ListNode(evaluatedListNode);
                        }
                    }
                }
                throw new RuntimeException("The list can't be treated as a function: " + evaluatedArgument);
            } else {
                return evaluatedArgument;
            }
        } catch (ClassCastException e) {
            return evaluatedArgument;
        }
    }

    @Override
    public String toString() {
        return "eval{" + argument + '}';
    }
}
