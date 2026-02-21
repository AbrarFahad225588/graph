package com.example.graph.plot;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class EquationSolverAll {

    private final String expression;

    public EquationSolverAll(String expression) {
        this.expression = expression;
    }

    public double evaluate(double xValue) {
        Expression e = new ExpressionBuilder(expression)
                .variable("x")
                .build()
                .setVariable("x", xValue);

        return e.evaluate();
    }

    public double derivative(double xValue) {
        double h = 1e-5;
        return (evaluate(xValue + h) - evaluate(xValue - h)) / (2 * h);
    }
}