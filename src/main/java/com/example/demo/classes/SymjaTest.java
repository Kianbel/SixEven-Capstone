package com.example.demo.classes;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

public class SymjaTest {
    public static void main(String[] args) {
        try {
            ExprEvaluator evaluator = new ExprEvaluator(false, (short) 100);

            // Test symbolic simplification
            IExpr result = evaluator.evaluate("n + 1 + 2");
            System.out.println(result); // Should print "3 + n"

            // Test with a value
            IExpr withValue = evaluator.evaluate("n + 1 + 2 /. n->5");
            System.out.println(withValue); // Should print "8"

            // Test more complex
            IExpr complex = evaluator.evaluate("a*3 + b + b*2");
            System.out.println(complex); // Should print "3*a + 3*b"

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}