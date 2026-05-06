package com.example.demo.classes;

import org.matheclipse.core.eval.ExprEvaluator;

public class Evaluator {
    private static final ExprEvaluator evaluator = new ExprEvaluator(false, (short) 100);
    static {
        evaluator.eval("1+1");
    }
    public static ExprEvaluator getEvaluator(){
        return evaluator;
    }
}
