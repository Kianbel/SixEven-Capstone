package com.example.demo.classes;

import org.matheclipse.core.eval.ExprEvaluator;

public abstract class Loop extends MultiLineStatement{
    Initialization statement;
    Bound condition;
    SingleLineStatement increment;

    public Loop(Initialization statement, Bound condition, SingleLineStatement increment) {
        this.statement = statement;
        this.condition = condition;
        this.increment = increment;
    }


    public String toString() {
        return super.toString();
    }

    public String getRuntime(){
        String length = null;
        if(increment instanceof Assignment a){
            if(statement.getVariable() != a.getVariable())
                throw new RuntimeException("Same variable must be used for the parameters");
            if(condition.getIterator() != a.getVariable())
                throw new RuntimeException("Iterator must be incremented");
            if(condition.getExpression2().toString().contains("++"))
                throw new RuntimeException("UpperBound cannot be increasing");
            if(condition.getIterator().getExpression() == null)
                throw new RuntimeException("Iterator cannot be NULL");
            condition.getIterator().setIterator(true);
            if(condition instanceof UpperBound u && increment instanceof IncreaseAssignment){
                String res = statement.getRuntime() +
                        "+Sum("+ condition.getRuntime() +",{" + a.getVariable().toString() + "," + a.getVariable().simplifiedToString()+",("+ u.getUpperBound().checkedToString() +")/"+ a.getExpression().toString() + (u.isEqual() ? "+0" : "-1") + "})+1"+
                        "+Sum("+ increment.getRuntime() +",{" + a.getVariable().toString() + "," + a.getVariable().simplifiedToString()+",("+ u.getUpperBound().checkedToString() + ")/"+ a.getExpression().toString() + (u.isEqual() ? "+0" : "-1") + "})";

                String temp = super.getRuntime();
                String query = "Sum("+ temp +",{" + a.getVariable().toString() + "," + a.getVariable().simplifiedToString()+",("+ u.getUpperBound().checkedToString() +")/"+a.getExpression().toString() +(u.isEqual() ? "+0" : "-1") + "})";
                res = Evaluator.getEvaluator().eval(res + "+" + query).toString();
                condition.getIterator().setIterator(false);
                return res;
            }
            else
                throw new RuntimeException("Not yet supported");
        }
        else
            throw new RuntimeException("Not yet supported");
//        return "0";
    }
}
