package com.example.demo.classes;

public abstract class Bound extends LogicalOperation {
    private boolean isEqual;
    private Variable iterator;
    public Bound(Operator operator, Variable iterator, Expression expression2, boolean isEqual) {
        super(operator, iterator, expression2);
        this.isEqual = isEqual;
        this.iterator = iterator;
    }

    public boolean isEqual() {
        return isEqual;
    }

    public Variable getIterator() {
        return iterator;
    }
}
