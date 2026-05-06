package com.example.demo.classes;

import java.util.HashSet;
import java.util.List;

public class Variable extends Expression{
    private String name;
    private Expression expression;
    private boolean isIterator;
    private int counter;
    public Variable(String name, Expression expression){
        super();
        this.name = name;
        this.expression = expression;
        isIterator = false;
        counter = 0;
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
        counter = 0;
    }

    public void incrementCounter(){
        counter++;
        if(counter > 2)
            throw new RuntimeException("An iterator value cannot be reassigned");
    }
    public boolean isIterator() {
        return isIterator;
    }

    @Override
    public int getRuntime() {
        return 0;
    }
}
