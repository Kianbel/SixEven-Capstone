package com.example.demo.classes;

public class AdditiveAssignment extends IncreaseAssignment{
    public AdditiveAssignment(Variable variable, Expression expression) {
        super(variable, new AssignmentOperator("+="), expression);
    }
    @Override
    public String getRuntime(){
        if(!getVariable().isIterator())
            getVariable().setExpression(new Binary(new Plus(), getVariable().getExpression(), expression));
        return super.getRuntime();
    }
}
