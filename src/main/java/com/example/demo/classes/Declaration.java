package com.example.demo.classes;

public class Declaration extends SingleLineStatement{
    public Declaration(String name) {
        super(new Variable(name));
    }
    public Variable getVariable(){
        return (Variable) expression;
    }
}
