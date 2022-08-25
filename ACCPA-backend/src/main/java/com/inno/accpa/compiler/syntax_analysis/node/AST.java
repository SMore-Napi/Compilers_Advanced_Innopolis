package com.inno.accpa.compiler.syntax_analysis.node;

public class AST {

    private final ListNode root;

    public AST(ListNode lst) {
        this.root = lst;
    }

    public void printAST() {
        System.out.println(root);
    }

    public String evaluate() {
        StringBuilder result = new StringBuilder();
        ListNode lines = (ListNode) root.evaluate();
        for (ElementInterface i : lines.elements) {
            if (i != null) {
                if ((i instanceof LiteralNode) || (i instanceof AtomNode) || (i instanceof ListNode)) {
                    result.append(i).append("\n");
                }
            }
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
