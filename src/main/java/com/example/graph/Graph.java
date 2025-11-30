
package com.example.graph;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.util.*;

public class Graph {

    public final static int LOWER_BOUND = -25;
    public final static int UPPER_BOUND = 25;
    private final NumberAxis xAxis = new NumberAxis(LOWER_BOUND, UPPER_BOUND, 1);
    private final NumberAxis yAxis = new NumberAxis(LOWER_BOUND, UPPER_BOUND, 1);
    private final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

    private final GraphingApp app;
    private final ArrayList<Color> alreadyChosen = new ArrayList<>();
    private final Map<TextField, ArrayList<XYChart.Series<Number, Number>>> equationMap = new HashMap<>();
    private final Map<TextField, Color> colorMap = new HashMap<>();
    private final Map<TextField, Label> errorMap = new HashMap<>();

    public Graph(GraphingApp app) {
        this.app = app;
    }

    // ---------------- Zoom ----------------
    private void zoomInOrOut(double x, double y, char op) {
        double xRange = xAxis.getUpperBound() - xAxis.getLowerBound();
        double yRange = yAxis.getUpperBound() - yAxis.getLowerBound();
        double factor = (op == '*') ? 1 : 0.25;

        xAxis.setLowerBound(Math.max(x - xRange * factor, LOWER_BOUND));
        xAxis.setUpperBound(Math.min(x + xRange * factor, UPPER_BOUND));
        yAxis.setLowerBound(Math.max(y - yRange * factor, LOWER_BOUND));
        yAxis.setUpperBound(Math.min(y + yRange * factor, UPPER_BOUND));
    }

    // ---------------- Scene ----------------
    protected Scene createGraphScene() {
        lineChart.setOnMousePressed(e -> {
            double dataX = xAxis.getValueForDisplay(e.getX()).doubleValue();
            double dataY = yAxis.getValueForDisplay(e.getY()).doubleValue();
            if (e.isPrimaryButtonDown() && e.getClickCount() >= 2) zoomInOrOut(dataX, dataY, '/');
            else if (e.isSecondaryButtonDown()) zoomInOrOut(dataX, dataY, '*');
        });

        lineChart.setPrefSize(800, 600);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);

        ScrollPane rootPane = new ScrollPane();
        VBox mainVBox = new VBox();
        mainVBox.setPrefWidth(1180);

        VBox bottomPanel = new VBox();
        bottomPanel.setPadding(new Insets(20));

        Label equationLabel = new Label("Enter Equation(s):");
        equationLabel.setFont(new Font(25));

        VBox finalEquationBox = new VBox();
        HBox initialEquationBox = createEquationBox(bottomPanel);

        Button addEquationBtn = createOptionButton("Add Equation", "limegreen", 60, () -> bottomPanel.getChildren().add(addEquationBox(bottomPanel))); // 336 num line
        Button menuBtn = createOptionButton("Menu", "lightblue", 365, app::openMenuScene);
        Button newGraphBtn = createOptionButton("New Graph", "orange", 670, app::openGraphScene);

        HBox buttonBox = new HBox(addEquationBtn, menuBtn, newGraphBtn);
        finalEquationBox.getChildren().addAll(equationLabel, initialEquationBox);
        bottomPanel.getChildren().add(finalEquationBox);

        mainVBox.getChildren().addAll(lineChart, buttonBox, bottomPanel);
        rootPane.setContent(mainVBox);
        rootPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        rootPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        return new Scene(rootPane, 1200, 794);
    }

    // ---------------- Equation Box ----------------
    private HBox createEquationBox(VBox bottomPanel) {
        HBox equationBox = new HBox();
        equationBox.setSpacing(10);
        equationBox.setPadding(new Insets(20, 0, 20, 0));

        Circle circle = new Circle(20, Color.WHITE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2.0);

        Label yLabel = new Label("y = ");
        yLabel.setFont(new Font(24));
        yLabel.setTranslateX(10);

        TextField tf = new TextField();
        tf.setPrefSize(900, 40);
        tf.setFont(new Font(18));
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                // Remove graph and error message only, keep TextField
                lineChart.getData().removeAll(equationMap.get(tf));
                equationMap.remove(tf);
                Circle c = (Circle) equationBox.getChildren().get(0);
                c.setFill(Color.WHITE);

                // Remove error label if exists
                if (errorMap.get(tf) != null) {
                    VBox parentVBox = (VBox) equationBox.getParent();
                    parentVBox.getChildren().remove(errorMap.get(tf));
                    errorMap.remove(tf);
                }
            } else {
                PloatEquation.plotEquation(equationBox, newValue,equationMap,lineChart,colorMap,errorMap);
            }
        });

        equationBox.getChildren().addAll(circle, yLabel, tf);
        return equationBox;
    }

private VBox addEquationBox(VBox bottomPanel) {
    VBox container = new VBox();
    HBox equationBox = createEquationBox(bottomPanel);

    Circle circle = (Circle) equationBox.getChildren().get(0);
    TextField tf = (TextField) equationBox.getChildren().get(2);

    Button removeBtn = new Button("X");
    removeBtn.setStyle("-fx-background-color: red; -fx-text-fill: black;");
    removeBtn.setFont(new Font(25));
    removeBtn.setPrefSize(40, 40);
    removeBtn.setOnAction(e -> removeEquation(tf, equationBox, bottomPanel, circle));

    equationBox.getChildren().add(removeBtn);
    container.getChildren().add(equationBox);
    return container;
}

    private void removeEquation(TextField tf, HBox equationBox, VBox bottomPanel, Circle circle) {
        lineChart.getData().removeAll(equationMap.getOrDefault(tf, new ArrayList<>()));
        equationMap.remove(tf);
        colorMap.remove(tf);
        circle.setFill(Color.WHITE);
        if (errorMap.get(tf) != null) bottomPanel.getChildren().remove(errorMap.get(tf));
        errorMap.remove(tf);
        bottomPanel.getChildren().remove(equationBox.getParent());
    }








    private Button createOptionButton(String title, String color, int deltaX, Runnable action) {
        Button button = new Button(title);
        button.setTranslateX(deltaX);
        button.setStyle("-fx-background-color: " + color + ";");
        button.setPrefSize(150, 40);
        button.setFont(Font.font("System Bold", 20));
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: lightgreen;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + ";"));
        button.setOnAction(e -> action.run());
        return button;
    }

}

