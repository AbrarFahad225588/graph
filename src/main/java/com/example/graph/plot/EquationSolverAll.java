
package com.example.graph.plot;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class EquationSolverAll {

    private final Expression e;

    public EquationSolverAll(String expression) {

        this.e = new ExpressionBuilder(expression)
                .variable("x")
                .build();
    }

    public double evaluate(double xValue) {
        return e.setVariable("x", xValue).evaluate();
    }

}