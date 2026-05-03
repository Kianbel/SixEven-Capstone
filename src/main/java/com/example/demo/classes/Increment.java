package com.example.demo.classes;

public class Increment extends Unary{
    public Increment(Variable variable, boolean isPrefix) {
        super(new Operator("++"), variable, isPrefix);
    }
}
