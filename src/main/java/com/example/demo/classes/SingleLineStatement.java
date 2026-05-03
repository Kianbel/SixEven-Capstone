package com.example.demo.classes;

public class SingleLineStatement extends Statement{
    Expression expression;
    public SingleLineStatement(Expression expression){
        this.expression = expression;
    }

    @Override
    public String toString() {
        return expression.toString();
    }

    @Override
    public String getRuntime() {
        return ((Integer)(expression.getRuntime())).toString();
    }

    public Expression getExpression() {
        return expression;
    }
}
