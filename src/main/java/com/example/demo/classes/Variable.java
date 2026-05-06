package com.example.demo.classes;

import java.util.HashSet;
import java.util.List;

public class Variable extends Expression{
    private String name;
    private Expression expression;
    private boolean isIterator;
    public Variable(String name, Expression expression){
        super();
        this.name = name;
        this.expression = expression;
        isIterator = false;
    }
    public Variable(String name){
        super();
        this.name = name;
        this.expression = null;
    }
    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public String toString() {
        return name;
    }
    public String simplifiedToString(){
        if(expression == null)
            return name;
        return expression.toString();
    }

    public Expression getExpression() {
        if(expression == null)
            return this;
        return expression;
    }

    @Override
    public String checkedToString() {
        if(isIterator)
            return toString();
        else return simplifiedToString();
    }

    public void setIterator(boolean iterator) {
        isIterator = iterator;
    }

    public boolean isIterator() {
        return isIterator;
    }

    @Override
    public int getRuntime() {
        return 0;
    }
}
