package com.example.demo.classes;

public class Decrement extends Unary {
    public Decrement(Variable variable, boolean isPrefix) {
        super(new Operator("--"), variable, isPrefix);
    }
}
