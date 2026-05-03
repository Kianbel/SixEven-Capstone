package com.example.demo.classes;

public class RightUpperBound extends UpperBound{
    public RightUpperBound(Variable iterator, Expression upperBound, boolean isEqual) {
        super(isEqual ? new Operator("<=") : new Operator("<"), iterator, upperBound, isEqual);
    }
}
