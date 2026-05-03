package com.example.demo.classes;

import java.util.HashSet;

public abstract class Statement {
    HashSet<Variable> variables;
    public abstract String getRuntime();

    public void setVariables(HashSet<Variable> variables) {
        this.variables = variables;
//        System.out.println(getClass()+" "+(variables == null));
    }
}
