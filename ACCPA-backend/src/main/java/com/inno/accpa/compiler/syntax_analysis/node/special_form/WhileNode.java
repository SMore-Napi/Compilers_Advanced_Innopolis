package com.inno.accpa.compiler.syntax_analysis.node.special_form;

import com.inno.accpa.compiler.interpreter.NestedFormBreak;
import com.inno.accpa.compiler.syntax_analysis.node.ElementInterface;
import com.inno.accpa.compiler.syntax_analysis.node.LiteralNode;

public class WhileNode implements ElementInterface {
    ElementInterface condition;
    ElementInterface action;

    public WhileNode(ElementInterface condition, ElementInterface action) {
        this.condition = condition;
        this.action = action;
    }

    @Override
    public ElementInterface evaluate() {
        NestedFormBreak.getInstance().introduceLocalScope();
        while (true) {
            ElementInterface conditionEvaluation = condition.evaluate();
            if ((conditionEvaluation instanceof LiteralNode) && ((LiteralNode) conditionEvaluation).booleanValue != null) {
                if (((LiteralNode) conditionEvaluation).booleanValue) {
                    ElementInterface evaluateResult = this.action.evaluate();
                    if (evaluateResult instanceof BreakNode) {
                        return null;
                    }
                } else {
                    NestedFormBreak.getInstance().leaveLocalScope();
                    return null;
                }
            } else {
                throw new RuntimeException("The condition argument should be boolean, but given " + condition);
            }
        }
    }

    @Override
    public String toString() {
        return "WhileNode{" +
                "condition=" + condition +
                ", action=" + action +
                '}';
    }
}
