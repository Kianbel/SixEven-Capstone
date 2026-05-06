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
                String res = "0";
                ExprEvaluator evaluator = new ExprEvaluator(false, (short) 100);
                res = evaluator.eval(res + " + " + statement.getRuntime()).toString();
                res = evaluator.eval(res + " + " + "Sum("+ condition.getRuntime() +",{" + a.getVariable().toString() + "," + a.getVariable().simplifiedToString()+",("+ u.getUpperBound().checkedToString() + (u.isEqual() ? "+0" : "-1") + ")/" + a.getExpression().toString()+"+1})").toString();
                res = evaluator.eval(res + " + " + "Sum("+ increment.getRuntime() +",{" + a.getVariable().toString() + "," + a.getVariable().simplifiedToString()+",("+ u.getUpperBound().checkedToString() + (u.isEqual() ? "+0" : "-1") + ")/" + a.getExpression().toString()+"})").toString();
                String temp = super.getRuntime();
//                System.out.println(u.getUpperBound().checkedToString(variables));
                String query = "Sum("+ temp +",{" + a.getVariable().toString() + "," + a.getVariable().simplifiedToString()+",("+ u.getUpperBound().checkedToString() + (u.isEqual() ? "+0" : "-1") + ")/" + a.getExpression().toString()+"})";
//                System.out.println(query);
                temp = evaluator.eval(query).toString();
//                System.out.println(temp);
                res = evaluator.eval(res + "+" + temp).toString();

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
