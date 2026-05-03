package com.example.demo.classes;

import org.matheclipse.core.eval.ExprEvaluator;

public abstract class Loop extends MultiLineStatement{
    SingleLineStatement statement;
    Bound condition;
    SingleLineStatement increment;

    public Loop(SingleLineStatement statement, Bound condition, SingleLineStatement increment) {
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
            if(condition.getIterator() != a.getVariable())
                throw new RuntimeException("Iterator must be incremented");
            variables.add(condition.getIterator());
            if(condition instanceof UpperBound u && increment instanceof IncreaseAssignment){
                if(u.getIterator().getExpression() == null)
                    throw new RuntimeException("Iterator cannot be NULL");
                ExprEvaluator evaluator = new ExprEvaluator(false, (short) 100);
                String res = super.getRuntime();
//                System.out.println(u.getUpperBound().checkedToString(variables));
                String query = "Sum("+ res +",{" + a.getVariable().toString() + "," + a.getVariable().simplifiedToString()+",("+ u.getUpperBound().checkedToString(variables) + (u.isEqual() ? "+0" : "-1") + ")/" + a.getExpression().toString()+"})";
//                System.out.println(query);
                res = evaluator.eval(query).toString();
//                System.out.println(res);
                res = evaluator.eval(res + " + " + statement.getRuntime()).toString();
                res = evaluator.eval(res + " + " + "Sum("+ condition.getRuntime() +",{" + a.getVariable().toString() + "," + a.getVariable().simplifiedToString()+",("+ u.getUpperBound().checkedToString(variables) + (u.isEqual() ? "+0" : "-1") + ")/" + a.getExpression().toString()+"+1})").toString();
                res = evaluator.eval(res + " + " + "Sum("+ increment.getRuntime() +",{" + a.getVariable().toString() + "," + a.getVariable().simplifiedToString()+",("+ u.getUpperBound().checkedToString(variables) + (u.isEqual() ? "+0" : "-1") + ")/" + a.getExpression().toString()+"})").toString();
                variables.remove(condition.getIterator());
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
