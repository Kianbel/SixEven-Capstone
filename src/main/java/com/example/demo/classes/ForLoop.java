package com.example.demo.classes;

public class ForLoop extends Loop {
    public ForLoop(SingleLineStatement statement, Bound condition, SingleLineStatement increment) {
        super(statement, condition, increment);
    }

    @Override
    public String toString() {
        String res = "";
        res+="for("+statement.toString()+";"+condition.toString()+";"+increment.toString()+")";
        return res+super.toString();
    }

}
