

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
import javafx.scene.Node;
import javafx.scene.shape.Shape;
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

            for (int i = 0; i <= (UPPER_BOUND - LOWER_BOUND) * 25; i++) {
                double x = LOWER_BOUND + i * 0.04;
                try {
                    double y = EQ.evaluate(x);
                    if (Double.isFinite(y)) {
                        series.getData().add(new XYChart.Data<>(x, y));
                    } else {
                        if (!series.getData().isEmpty()) seriesArr.add(series);
                        series = new XYChart.Series<>();
                    }
                } catch (Exception e) {
                   
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

                Node seriesNode = firstSeries.getNode();

                if (seriesNode != null) {
                    
                    Paint linePaint = ((Shape) seriesNode).getStroke();
                    if (linePaint instanceof Color defaultChartColor) {
                        c.setFill(defaultChartColor);
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