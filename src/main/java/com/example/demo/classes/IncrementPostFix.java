package com.example.demo.classes;

public class IncrementPostFix extends Unary{
    public IncrementPostFix(Variable variable) {
        super(new Operator("++"), variable, false);
    }

    @Override
    public int getRuntime() {
        ((Variable)getExpression1()).setExpression(new Binary(new Plus(), ((Variable)getExpression1()).getExpression(), new Value<Integer>(1)));
        return super.getRuntime();
    }
}
