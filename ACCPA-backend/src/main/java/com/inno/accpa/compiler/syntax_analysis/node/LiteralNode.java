package com.inno.accpa.compiler.syntax_analysis.node;

import com.inno.accpa.compiler.lexical_analysis.tokens.literal.BooleanLiteralToken;
import com.inno.accpa.compiler.lexical_analysis.tokens.literal.IntegerNumberLiteralToken;
import com.inno.accpa.compiler.lexical_analysis.tokens.literal.LiteralToken;
import com.inno.accpa.compiler.lexical_analysis.tokens.literal.RealNumberLiteralToken;

public class LiteralNode implements ElementInterface {
    public Integer integerValue;
    public Double realValue;
    public Boolean booleanValue;

    public LiteralNode(int value) {
        integerValue = value;
    }

    public LiteralNode() {
    }

    public LiteralNode(double value) {
        realValue = value;
    }

    public LiteralNode(boolean value) {
        booleanValue = value;
    }

    public LiteralNode(LiteralToken token) {
        String content = token.getContent();
        if (token instanceof IntegerNumberLiteralToken) {
            this.integerValue = Integer.valueOf(content);
        } else if (token instanceof RealNumberLiteralToken) {
            this.realValue = Double.valueOf(content);
        } else if (token instanceof BooleanLiteralToken) {
            this.booleanValue = Boolean.valueOf(content);
        }
    }

    public Object getValue() {
        if (integerValue != null) {
            return integerValue;
        }
        if (realValue != null) {
            return realValue;
        }
        if (booleanValue != null) {
            return booleanValue;
        }
        return null;
    }

    @Override
    public String toString() {
        if (integerValue != null) {
            return integerValue.toString();
        }
        if (realValue != null) {
            return realValue.toString();
        }
        if (booleanValue != null) {
            return booleanValue.toString();
        }
        return "null";
    }

    @Override
    public ElementInterface evaluate() {
        return this;
    }
}