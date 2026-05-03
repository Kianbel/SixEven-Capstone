package com.example.demo.classes;

import org.matheclipse.core.eval.ExprEvaluator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Function {
    List<Statement> statements = new ArrayList<>();
    List<Declaration> parameters = new ArrayList<>();
    HashSet<Variable> variables = new HashSet<>();
    private String name;
    public Function(String name){
        this.name = name;
    }

    public  void addStatement(Statement statement){
        statements.add(statement);
        statement.setVariables(variables);
//        System.out.println(variables == null);
    }
    public void addParameter(Declaration declaration){
        parameters.add(declaration);
    }
    public String toString() {
        String res = name+"(";
        for(int i = 0; i < parameters.size() - 1; i++)
            res+=parameters.get(i).getVariable().toString()+", ";
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
    public String getRuntime() {
        ExprEvaluator evaluator = new ExprEvaluator(false, (short) 100);
        String res = "0";
        for(Statement s : statements)
            res = evaluator.eval("("+res + ") + (" + s.getRuntime()+")").toString();
        return evaluator.eval("Expand("+ res +")").toString();
    }
}
