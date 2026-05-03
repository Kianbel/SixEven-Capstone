package com.example.demo.classes;

public class Array extends Variable{
    private Value size;
    public Array(String name, Value value){
        super(name, value);
    }
    public Array(String name){
        this(name, null);
    }
}
