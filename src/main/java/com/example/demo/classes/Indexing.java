package com.example.demo.classes;

public class Indexing extends Binary{
    public Indexing(Array array, Expression expression2) {

        super(new IndexOperator(), array, expression2);
    }

    @Override
    public String toString() {
        return expression1+(operator.toString().charAt(0)+"")+expression2.toString()+(operator.toString().charAt(1)+"");
    }
}
