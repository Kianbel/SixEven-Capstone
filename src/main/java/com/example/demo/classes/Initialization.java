package com.example.demo.classes;

public class Initialization extends Assignment {
    public Initialization(Variable variable, Expression expression) {
        super(variable, new AssignmentOperator("="), expression);
        variable.setExpression(expression);
    }
}
