

package com.example.graph.plot;

import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.Map;

import static com.example.graph.plot.Graph.LOWER_BOUND;
import static com.example.graph.plot.Graph.UPPER_BOUND;

public class PlotEquation {
    public static void plotEquation(HBox equationBox, String equation, Map<TextField, ArrayList<XYChart.Series<Number, Number>>> equationMap, LineChart<Number, Number> lineChart, Map<TextField, Color> colorMap, Map<TextField, Label> errorMap) {
        Circle c = (Circle) equationBox.getChildren().get(0);
        TextField tf = (TextField) equationBox.getChildren().get(2);

        try {
            EquationSolverAll EQ = new EquationSolverAll(equation);
            ArrayList<XYChart.Series<Number, Number>> seriesArr = equationMap.get(tf);

            if (seriesArr != null) {
                lineChart.getData().removeAll(seriesArr);
                seriesArr.clear();
            } else {
                seriesArr = new ArrayList<>();
                equationMap.put(tf, seriesArr);
            }

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("y = " + equation);

//            for (int i = 0; i <= (UPPER_BOUND - LOWER_BOUND) * 25; i++) {
//                double x = LOWER_BOUND + i * 0.04;
//                double y = EQ.evaluate(x);
//
//                if (!Double.isNaN(y) && y != Double.POSITIVE_INFINITY && y != Double.NEGATIVE_INFINITY) {
//                    series.getData().add(new XYChart.Data<>(x, y));
//                } else {
//                    if (!series.getData().isEmpty()) seriesArr.add(series);
//                    series = new XYChart.Series<>();
//                }
//            }
            for (int i = 0; i <= (UPPER_BOUND - LOWER_BOUND) * 25; i++) {
                double x = LOWER_BOUND + i * 0.04;
                try {
                    double y = EQ.evaluate(x);

                    // Check for NaN, Infinities, and ensure Y isn't a massive "spike"
                    if (Double.isFinite(y)) {
                        series.getData().add(new XYChart.Data<>(x, y));
                    } else {
                        // If y is NaN (like 0/0), break the line segment
                        if (!series.getData().isEmpty()) seriesArr.add(series);
                        series = new XYChart.Series<>();
                    }
                } catch (Exception e) {
                    // This catches the "Division by zero" error from exp4j
                    if (!series.getData().isEmpty()) seriesArr.add(series);
                    series = new XYChart.Series<>();
                }
            }

            if (!series.getData().isEmpty()) seriesArr.add(series);
            lineChart.getData().addAll(seriesArr);
            lineChart.applyCss();
            lineChart.layout();

            if (!seriesArr.isEmpty()) {
                XYChart.Series<Number, Number> firstSeries = seriesArr.get(0);

                // 2. Get the node of the series (the line itself)
                javafx.scene.Node seriesNode = firstSeries.getNode();

                if (seriesNode != null) {
                    // 3. Extract the stroke color assigned by the chart's default CSS
                    // Most JavaFX themes use the "-fx-stroke" property for LineChart lines
                    Paint linePaint = ((javafx.scene.shape.Shape) seriesNode).getStroke();

                    if (linePaint instanceof Color defaultChartColor) {
                        // 4. Update your UI circle to match the chart's default color
                        c.setFill(defaultChartColor);

                        // Optional: Store it in your map if you need it elsewhere
                        colorMap.put(tf, defaultChartColor);
                    }
                }
            }

            VBox container = (VBox) equationBox.getParent();
            if (errorMap.get(tf) != null) {
                container.getChildren().remove(errorMap.get(tf));
                errorMap.remove(tf);
            }

        } catch (Exception e) {
            c.setFill(Color.WHITE);
            displayErrorMessage("Invalid Equation Format", tf, errorMap);
        }
    }

    public static void displayErrorMessage(String error, TextField tf, Map<TextField, Label> errorMap) {
        if (errorMap.get(tf) != null) return;

        Label errorLabel = new Label(error);
        errorLabel.setTextFill(Color.web("#e74c3c"));
        errorLabel.setFont(Font.font("System", 13));
        errorLabel.setPadding(new Insets(0, 0, 5, 45)); // Aligns with the TextField

        errorMap.put(tf, errorLabel);
        VBox container = (VBox) tf.getParent().getParent();
        container.getChildren().add(errorLabel);
    }
}