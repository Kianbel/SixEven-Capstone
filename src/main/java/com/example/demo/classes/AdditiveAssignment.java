package com.example.demo.classes;

public class AdditiveAssignment extends IncreaseAssignment{
    public AdditiveAssignment(Variable variable, Expression expression) {
        super(variable, new AssignmentOperator("+="), expression);
    }
}
