package com.example.demo.classes;

public abstract class UpperBound extends Bound {
    Expression upperBound;

    public UpperBound(Operator operator, Variable iterator, Expression upperBound, boolean isEqual) {
        super(operator, iterator, upperBound, isEqual);
        this.upperBound = upperBound;
    }
    public Expression getUpperBound() {
        return upperBound;
    }
}
