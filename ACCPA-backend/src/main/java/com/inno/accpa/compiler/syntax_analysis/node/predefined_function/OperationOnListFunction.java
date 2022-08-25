package com.inno.accpa.compiler.syntax_analysis.node.predefined_function;

import com.inno.accpa.compiler.syntax_analysis.node.ElementInterface;
import com.inno.accpa.compiler.syntax_analysis.node.ListNode;
import com.inno.accpa.compiler.syntax_analysis.node.LiteralNode;

public class OperationOnListFunction implements ElementInterface {

    ElementInterface element;
    ElementInterface list;
    Operation operation;

    public OperationOnListFunction(Operation operation, ElementInterface list) {
        this.list = list;
        this.operation = operation;
    }

    public OperationOnListFunction(Operation operation, ElementInterface element, ElementInterface list) {
        this.element = element;
        this.list = list;
        this.operation = operation;
    }

    @Override
    public ElementInterface evaluate() {
        switch (operation) {
            case HEAD:
                try {
                    ListNode listNode = (ListNode) list.evaluate();
                    if (listNode.elements.isEmpty()) {
                        throw new RuntimeException("Can't call 'head' from empty list");
                    }
                    return listNode.elements.get(0);
                } catch (ClassCastException classCastException) {
                    throw new RuntimeException("The evaluated argument should be a list");
                }
            case TAIL:
                try {
                    ListNode listNode = (ListNode) list.evaluate();
                    if (listNode.elements.isEmpty()) {
                        throw new RuntimeException("Can't call 'tail' from empty list");
                    }
                    return new ListNode(listNode.elements.subList(1, listNode.elements.size()));
                } catch (ClassCastException classCastException) {
                    throw new RuntimeException("The evaluated argument should be a list");
                }
            case CONS:
                try {
                    ElementInterface firstElement = element.evaluate();
                    ListNode listNode = (ListNode) list.evaluate();
                    return new ListNode(firstElement, listNode);
                } catch (ClassCastException classCastException) {
                    throw new RuntimeException("The second evaluated argument should be a list");
                }
            case ISEMPTY:
                try {
                    ListNode listNode = (ListNode) list.evaluate();
                    if (listNode.elements.isEmpty()) {
                        return new LiteralNode(true);
                    }
                    return new LiteralNode(false);
                } catch (ClassCastException classCastException) {
                    throw new RuntimeException("The evaluated argument should be a list");
                }
            case LENGTH:
                try {
                    ListNode listNode = (ListNode) list.evaluate();
                    return new LiteralNode(listNode.elements.size());
                } catch (ClassCastException classCastException) {
                    throw new RuntimeException("The evaluated argument should be a list");
                }
            default:
                throw new RuntimeException("Unknown list operation: " + operation);
        }
    }

    @Override
    public String toString() {
        if (element != null) {
            return "OperationOnLists{" +
                    "element=" + element +
                    ", list=" + list +
                    ", operation=" + operation +
                    '}';
        }
        return "OperationOnLists{" +
                "list=" + list +
                ", operation=" + operation +
                '}';
    }

    public enum Operation {
        HEAD, TAIL, CONS, ISEMPTY, LENGTH
    }
}
