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

        StringBuilder builder = new StringBuilder();
        builder.append("0");
        List<RuntimeRunnable> runnables = new ArrayList<>();
        for(Statement s : statements){
            if(statements instanceof MultiLineStatement m){
                RuntimeRunnable runnable = new RuntimeRunnable(m);
                runnables.add(runnable);
                Thread thread = new Thread(runnable);
                thread.start();
            }
            else
                builder.append("+"+s.getRuntime());
        }
        for (RuntimeRunnable r: runnables)
            builder.append("+"+r.getRuntime());
        return Evaluator.getEvaluator().eval("Expand("+ builder.toString() +")").toString();
    }
//    public String getRuntime() {
//        String res = "0";
//        for(Statement s : statements)
//                res += "+"+s.getRuntime();
//        return Evaluator.getEvaluator().eval(res).toString();
//    }
    public  void addStatement(Statement statement){
        statements.add(statement);
//        System.out.println(variables == null);
    }
}
