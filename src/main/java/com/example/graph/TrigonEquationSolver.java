package com.example.graph;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class TrigonEquationSolver {

    private String expression;

    public TrigonEquationSolver(String expression) {
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