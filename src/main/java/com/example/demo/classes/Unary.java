package com.example.demo.classes;

import java.util.HashSet;
import java.util.List;

public abstract class Unary extends Operation implements Incrementable {
    boolean isPrefix;
    public Unary(Operator operator, Variable variable, boolean isPrefix) {
        super(operator, variable);
        this.isPrefix = isPrefix;
    }

    @Override
    public String simplifiedToString() {
        if(isPrefix)
            return expression1.simplifiedToString() + (getOperator().operator.equals("++")? "+" : "-" ) + "1";
        else return expression1.simplifiedToString();
    }

    @Override
    public String toString() {
        if(isPrefix)
            return operator.toString()+expression1.toString();
        else return expression1.toString()+operator.toString();
    }

    @Override
    public String checkedToString() {
        if(((Variable) expression1).isIterator())
            return toString();
        return simplifiedToString();
    }

}
