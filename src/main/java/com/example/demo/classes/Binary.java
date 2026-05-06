package com.example.demo.classes;

import java.util.HashSet;
import java.util.List;

public class Binary extends Operation{
    Expression expression2;
    public Binary(Operator operator, Expression expression1, Expression expression2) {
        super(operator, expression1);
        this.expression2 = expression2;
    }

    public Expression getExpression2() {
        return expression2;
    }

    @Override
    public String simplifiedToString() {
        return expression1.simplifiedToString()+operator.toString()+expression2.simplifiedToString();
    }

    @Override
    public String toString() {
        return expression1.toString()+operator.toString()+expression2.toString();
    }

    @Override
    public String checkedToString() {
        return expression1.checkedToString() + operator.toString()+expression2.checkedToString();

    }
    @Override
    public int getRuntime(){
        return expression2.getRuntime() + super.getRuntime();
    }
}
