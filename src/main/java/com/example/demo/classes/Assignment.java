package com.example.demo.classes;

public abstract class Assignment extends SingleLineStatement implements Incrementable{
    private Variable variable;
    private AssignmentOperator operator;

    public Assignment(Variable variable, AssignmentOperator operator, Expression expression){
        super(expression);
        this.variable = variable;
        this.operator = operator;
    }

    @Override
    public String getRuntime() {
        return super.getRuntime() + "+1";
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public String toString() {
        return variable.toString() + operator.toString()+super.toString();
    }
}
