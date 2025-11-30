
package com.example.graph;

import java.util.ArrayList;
import java.util.Stack;

public class EquationSolver {

    private ArrayList<String> tokens;

    // --- Stacks for Shunting Yard ---
    private Stack<String> operators = new Stack<>();
    private Stack<Double> numbers = new Stack<>();

    public double parseEquation(String equation, double x) throws Exception {
        // 1️⃣ Tokenize the input equation
        tokens = tokenize(equation, x);

        // 2️⃣ Handle negative numbers & decimals in token list
        processNegativesAndDecimals();

        // 3️⃣ Convert tokens to Reverse Polish Notation (RPN) using Shunting Yard
        ArrayList<String> rpn = convertToRPN(tokens);

        // 4️⃣ Evaluate RPN expression
        return evaluateRPN(rpn);
    }

    // ---------------- Tokenization ----------------
    private ArrayList<String> tokenize(String expression, double x) throws Exception {
        ArrayList<String> tokenList = new ArrayList<>();
        StringBuilder numberBuffer = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch) || ch == '.') {
                numberBuffer.append(ch); // Collect digits and decimal
            } else {
                // Flush number buffer if any
                if (numberBuffer.length() > 0) {
                    tokenList.add(numberBuffer.toString());
                    numberBuffer.setLength(0);
                }

                if (ch == 'x') {
                    tokenList.add(String.valueOf(x));
                } else if (isOperator(ch)) {
                    tokenList.add(String.valueOf(ch));
                } else if (ch == '(' || ch == ')') {
                    tokenList.add(String.valueOf(ch));
                } else if (ch != ' ') {
                    throw new Exception("Invalid character: " + ch);
                }
            }
        }

        if (numberBuffer.length() > 0) tokenList.add(numberBuffer.toString());

        // 5️⃣ Handle implicit multiplication
        tokenList = handleImplicitMultiplication(tokenList);

        return tokenList;
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^';
    }

    private ArrayList<String> handleImplicitMultiplication(ArrayList<String> list) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String curr = list.get(i);
            result.add(curr);

            if (i < list.size() - 1) {
                String next = list.get(i + 1);
                // Insert "*" between number or ')' and '(' or number
                if ((isNumeric(curr) || curr.equals(")")) && (next.equals("(") || isNumeric(next))) {
                    result.add("*");
                }
            }
        }
        return result;
    }

    private void processNegativesAndDecimals() throws Exception {
        // Merge negative signs with numbers if needed
        for (int i = 0; i < tokens.size(); i++) {
            String curr = tokens.get(i);
            if (curr.equals("-")) {
                if (i == 0 || isOperator(tokens.get(i - 1).charAt(0)) || tokens.get(i - 1).equals("(")) {
                    // negative number
                    if (i + 1 < tokens.size() && isNumeric(tokens.get(i + 1))) {
                        String negNumber = "-" + tokens.get(i + 1);
                        tokens.remove(i); // remove "-"
                        tokens.set(i, negNumber); // replace number
                    }
                }
            }
        }
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    // ---------------- Shunting Yard Algorithm ----------------
    private ArrayList<String> convertToRPN(ArrayList<String> tokens) throws Exception {
        ArrayList<String> output = new ArrayList<>();
        Stack<String> opStack = new Stack<>();

        for (String token : tokens) {
            if (isNumeric(token)) {
                output.add(token);
            } else if (isOperator(token.charAt(0))) {
                while (!opStack.isEmpty() && isOperator(opStack.peek().charAt(0))
                        && (getPriority(opStack.peek()) >= getPriority(token))) {
                    output.add(opStack.pop());
                }
                opStack.push(token);
            } else if (token.equals("(")) {
                opStack.push(token);
            } else if (token.equals(")")) {
                while (!opStack.isEmpty() && !opStack.peek().equals("(")) {
                    output.add(opStack.pop());
                }
                if (opStack.isEmpty()) throw new Exception("Mismatched parentheses");
                opStack.pop(); // remove "("
            }
        }

        while (!opStack.isEmpty()) {
            if (opStack.peek().equals("(") || opStack.peek().equals(")"))
                throw new Exception("Mismatched parentheses");
            output.add(opStack.pop());
        }

        return output;
    }

    private int getPriority(String op) {
        switch (op) {
            case "^": return 3;
            case "*":
            case "/": return 2;
            case "+":
            case "-": return 1;
            default: return 0;
        }
    }

    // ---------------- Evaluate RPN ----------------
    private double evaluateRPN(ArrayList<String> rpn) throws Exception {
        Stack<Double> stack = new Stack<>();

        for (String token : rpn) {
            if (isNumeric(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
                if (stack.size() < 2) throw new Exception("Invalid expression");
                double b = stack.pop();
                double a = stack.pop();
                double res = applyOperator(a, b, token);
                stack.push(res);
            }
        }

        if (stack.size() != 1) throw new Exception("Invalid expression");

        return stack.pop();
    }

    private double applyOperator(double a, double b, String op) throws Exception {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (b == 0) throw new Exception("Division by zero");
                return a / b;
            case "^": return Math.pow(a, b);
            default: throw new Exception("Invalid operator: " + op);
        }
    }

}
