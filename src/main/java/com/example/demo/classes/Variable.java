package com.example.demo.classes;

import java.util.HashSet;

public class Variable extends Expression{
    private String name;
    private Expression expression;
    public Variable(String name, Expression expression){
        super();
        this.name = name;
        this.expression = expression;
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
    public String checkedToString(HashSet<Variable> variables) {
        if(variables.contains(this))
            return toString();
        else return simplifiedToString();
    }

    @Override
    public int getRuntime() {
        return 0;
    }
}
