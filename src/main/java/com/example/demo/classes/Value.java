package com.example.demo.classes;

import java.util.HashSet;

public class Value<T> extends Expression{
    protected T elem;
    public Value(T elem){
        this.elem = elem;
    }
    public Value(){
        this(null);
    }
    public T getElem() {
        return elem;
    }

    public String toString(){
        return ""+elem;
    }

    @Override
    public String checkedToString(HashSet<Variable> variables) {
        return toString();
    }

    @Override
    public int getRuntime() {
        return 0;
    }

    @Override
    public String simplifiedToString() {
        return toString();
    }

}
