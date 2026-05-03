package com.example.demo.classes;

import org.matheclipse.core.eval.ExprEvaluator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
public class MultiLineStatement extends Statement{
    List<Statement> statements = new ArrayList<>();
    public String toString() {
        String res = "{\n";
        for(Statement s : statements){
            if(s instanceof MultiLineStatement){
                String[] resarr = s.toString().split("\n");
                for(String str : resarr) {
                    res += "\t";
                    res += str;
                    res += "\n";
                }
            }
            else{
                res += "\t";
                res += s.toString();
                res += "\n";
            }

        }
        res += "}\n";
        return res;
    }

    @Override
    public String getRuntime() {
        ExprEvaluator evaluator = new ExprEvaluator(false, (short) 100);
        String res = "0";
        for(Statement s : statements)
            res = evaluator.eval("("+res + ") + (" + s.getRuntime()+")").toString();
        return evaluator.eval("Expand("+ res +")").toString();
    }
    public  void addStatement(Statement statement){
        statements.add(statement);
        statement.setVariables(variables);
//        System.out.println(variables == null);
    }

    @Override
    public void setVariables(HashSet<Variable> variables) {
        super.setVariables(variables);
        for(Statement s : statements)
            s.setVariables(variables);
    }
}
