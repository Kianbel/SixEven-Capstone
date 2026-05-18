package com.example.demo.classes;

import org.matheclipse.core.eval.ExprEvaluator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Function {
    List<Statement> statements = new ArrayList<>();
    List<Declaration> parameters = new ArrayList<>();
    private String name;
    public Function(String name){
        this.name = name;
    }

    public  void addStatement(Statement statement){
        statements.add(statement);
    }
    public void addParameter(Declaration declaration){
        parameters.add(declaration);
    }
    public String toString() {
        String res = name+"(";
        for(int i = 0; i < parameters.size() - 1; i++)
            res+=parameters.get(i).getVariable().toString()+", ";
        if(parameters.size() >= 1)
            res += parameters.get(parameters.size()-1).getVariable().toString();
        res += "){\n";
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
//    public String getRuntime() {
//        String res = "0";
//
//        for(Statement s : statements)
//            res += "+"+s.getRuntime();
//        return Evaluator.getEvaluator().eval("Expand("+ res +")").toString();
//    }
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
}
