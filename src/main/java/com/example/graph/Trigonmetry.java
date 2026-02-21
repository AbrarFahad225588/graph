//package com.example.graph;
//
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.chart.LineChart;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.XYChart;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//import javafx.scene.text.Font;
//import java.util.*;
//
//public class Trigonmetry {
//
//    public static int LOWER_BOUND = -10;
//    public static int UPPER_BOUND = 10;
//
//    private final NumberAxis xAxis = new NumberAxis(LOWER_BOUND, UPPER_BOUND, 1);
//    private final NumberAxis yAxis = new NumberAxis(-5, 5, 1);
//    private final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
//
//    private final GraphingApp app;
//    private final Map<TextField, ArrayList<XYChart.Series<Number, Number>>> equationMap = new HashMap<>();
//    private final Map<TextField, Color> colorMap = new HashMap<>();
//    private final Map<TextField, Label> errorMap = new HashMap<>();
//
//    public Trigonmetry(GraphingApp app) {
//        this.app = app;
//    }
//
//    private double lastMouseX, lastMouseY;
//
//    private void applyAllMouseActions(LineChart<Number, Number> chart) {
//        chart.setOnMouseClicked(e -> {
//            if (e.getClickCount() == 2) {
//                double dataX = xAxis.getValueForDisplay(e.getX()).doubleValue();
//                double dataY = yAxis.getValueForDisplay(e.getY()).doubleValue();
//                if (e.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
//                    applyZoom(dataX, dataY, 0.5);
//                } else {
//                    applyZoom(dataX, dataY, 2.0);
//                }
//            }
//        });
//
//        chart.setOnScroll(e -> {
//            double dataX = xAxis.getValueForDisplay(e.getX()).doubleValue();
//            double dataY = yAxis.getValueForDisplay(e.getY()).doubleValue();
//            double zoomFactor = (e.getDeltaY() > 0) ? 0.8 : 1.2;
//            applyZoom(dataX, dataY, zoomFactor);
//        });
//
//        chart.setOnMousePressed(e -> {
//            lastMouseX = e.getX();
//            lastMouseY = e.getY();
//        });
//
//        chart.setOnMouseDragged(e -> {
//            double deltaX = e.getX() - lastMouseX;
//            double deltaY = e.getY() - lastMouseY;
//            double xShift = (xAxis.getUpperBound() - xAxis.getLowerBound()) * (deltaX / chart.getWidth());
//            double yShift = (yAxis.getUpperBound() - yAxis.getLowerBound()) * (deltaY / chart.getHeight());
//
//            xAxis.setLowerBound(xAxis.getLowerBound() - xShift);
//            xAxis.setUpperBound(xAxis.getUpperBound() - xShift);
//            yAxis.setLowerBound(yAxis.getLowerBound() + yShift);
//            yAxis.setUpperBound(yAxis.getUpperBound() + yShift);
//
//            lastMouseX = e.getX();
//            lastMouseY = e.getY();
//        });
//    }
//
//    private void applyZoom(double x, double y, double factor) {
//        double xLower = xAxis.getLowerBound();
//        double xUpper = xAxis.getUpperBound();
//        double yLower = yAxis.getLowerBound();
//        double yUpper = yAxis.getUpperBound();
//
//        xAxis.setLowerBound(x - (x - xLower) * factor);
//        xAxis.setUpperBound(x + (xUpper - x) * factor);
//        yAxis.setLowerBound(y - (y - yLower) * factor);
//        yAxis.setUpperBound(y + (yUpper - y) * factor);
//
//        LOWER_BOUND = (int) xAxis.getLowerBound();
//        UPPER_BOUND = (int) xAxis.getUpperBound();
//
//        equationMap.forEach((tf, series) -> {
//            if (!tf.getText().trim().isEmpty()) {
//                HBox parentHBox = (HBox) tf.getParent();
//                plotTrigEquation(parentHBox, tf.getText(), tf, (Circle) parentHBox.getChildren().get(0));
//            }
//        });
//    }
//
//    protected Scene createTrigonmetryScene() {
//        lineChart.setPrefSize(800, 500);
//        lineChart.setCreateSymbols(false);
//        lineChart.setLegendVisible(false);
//        lineChart.setAnimated(false);
//        lineChart.setTitle("Trigonometric Graph");
//
//        applyAllMouseActions(lineChart);
//
//        ScrollPane rootPane = new ScrollPane();
//        VBox mainVBox = new VBox();
//        mainVBox.setPrefWidth(1180);
//
//        VBox bottomPanel = new VBox();
//        bottomPanel.setPadding(new Insets(20));
//
//        Label equationLabel = new Label("Enter Trig Equation (e.g., sin(x), cos(x)*x):");
//        equationLabel.setFont(new Font(25));
//
//        VBox equationsContainer = new VBox(); // মাল্টিপল ইকুয়েশন হোল্ডার
//        HBox initialEquationBox = createEquationBox(equationsContainer);
//        equationsContainer.getChildren().add(initialEquationBox);
//
//        Button addEquationBtn = createOptionButton("Add Trig Equation", "limegreen", 60,
//                () -> equationsContainer.getChildren().add(addEquationBox(equationsContainer)));
//        Button menuBtn = createOptionButton("Menu", "lightblue", 365, app::openMenuScene);
//        Button newGraphBtn = createOptionButton("New Graph", "orange", 670, app::openGraphScene);
//
//        HBox buttonBox = new HBox(addEquationBtn, menuBtn, newGraphBtn);
//        bottomPanel.getChildren().addAll(equationLabel, equationsContainer);
//
//        mainVBox.getChildren().addAll(lineChart, buttonBox, bottomPanel);
//        rootPane.setContent(mainVBox);
//
//        return new Scene(rootPane, 1200, 794);
//    }
//
//    private HBox createEquationBox(VBox parentContainer) {
//        HBox row = new HBox();
//        row.setSpacing(10);
//        row.setPadding(new Insets(10, 0, 10, 0));
//
//        Circle c = new Circle(20, Color.WHITE);
//        c.setStroke(Color.BLACK);
//        c.setStrokeWidth(2.0);
//
//        Label yLabel = new Label("y = ");
//        yLabel.setFont(new Font(24));
//
//        TextField textField = new TextField();
//        textField.setPrefSize(800, 40);
//        textField.setFont(new Font(18));
//        textField.setPromptText("sin(x)");
//
//        textField.textProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal == null || newVal.trim().isEmpty()) {
//                lineChart.getData().removeAll(equationMap.getOrDefault(textField, new ArrayList<>()));
//                equationMap.remove(textField);
//                c.setFill(Color.WHITE);
//                removeError(textField, parentContainer); // এখানে Container পাঠানো হয়েছে
//            } else {
//                plotTrigEquation(row, newVal, textField, c);
//            }
//        });
//
//        row.getChildren().addAll(c, yLabel, textField);
//        return row;
//    }
//
//    private void plotTrigEquation(HBox box, String eq, TextField tf, Circle c) {
//        // ফিক্স ১: ইনপুট খালি থাকলে প্লট করবে না
//        if (eq == null || eq.trim().isEmpty()) return;
//
//        try {
//            EquationSolverAll solver = new EquationSolverAll(eq);
//
//            if (equationMap.containsKey(tf)) {
//                lineChart.getData().removeAll(equationMap.get(tf));
//            }
//
//            XYChart.Series<Number, Number> series = new XYChart.Series<>();
//            ArrayList<XYChart.Series<Number, Number>> list = new ArrayList<>();
//
//            for (double x = LOWER_BOUND - 5; x <= UPPER_BOUND + 5; x += 0.05) {
//                double y = solver.evaluate(x);
//                if (Double.isFinite(y)) {
//                    series.getData().add(new XYChart.Data<>(x, y));
//                }
//            }
//
//            list.add(series);
//            equationMap.put(tf, list);
//            lineChart.getData().add(series);
//
//            Color color = ColorPickerRand.assignColor(list, tf, colorMap);
//            c.setFill(color);
//
//            // ফিক্স ২: সাকসেস হলে এরর মুছে দাও
//            removeError(tf, (VBox) box.getParent());
//
//        } catch (Exception e) {
//            c.setFill(Color.WHITE);
//            displayErrorMessage("Not a valid equation.", tf, errorMap);
//        }
//    }
//
//    private void removeError(TextField tf, VBox parent) {
//        if (errorMap.containsKey(tf)) {
//            parent.getChildren().remove(errorMap.get(tf));
//            errorMap.remove(tf);
//        }
//    }
//
//    public void displayErrorMessage(String error, TextField tf, Map<TextField, Label> errorMap) {
//        if (errorMap.containsKey(tf) || tf.getText().trim().isEmpty()) return;
//
//        Label errorLabel = new Label(error);
//        errorLabel.setTextFill(Color.RED);
//        errorLabel.setFont(new Font(15));
//        errorLabel.setTranslateX(100);
//
//        errorMap.put(tf, errorLabel);
//
//        // ফিক্স ৩: প্যারেন্ট হায়ারার্কি নিশ্চিত করা
//        VBox parentVBox = (VBox) tf.getParent().getParent();
//        parentVBox.getChildren().add(errorLabel);
//    }
//
//    private Button createOptionButton(String title, String color, int deltaX, Runnable action) {
//        Button button = new Button(title);
//        button.setTranslateX(deltaX);
//        button.setStyle("-fx-background-color: " + color + "; -fx-font-weight: bold;");
//        button.setPrefSize(180, 40);
//        button.setOnAction(e -> action.run());
//        return button;
//    }
//
//    private HBox addEquationBox(VBox parentContainer) {
////        VBox singleContainer = new VBox(); // প্রতিটি রো এবং এর এরর মেসেজের জন্য আলাদা কন্টেইনার
//        HBox row = createEquationBox(parentContainer);
//        TextField textField = (TextField) row.getChildren().get(2);
//        Circle c = (Circle) row.getChildren().get(0);
//
//        Button removeBtn = new Button("X");
//        removeBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
//        removeBtn.setPrefSize(40, 40);
//
//        removeBtn.setOnAction(e -> {
//            removeEquation(textField, row, parentContainer, c);
//            parentContainer.getChildren().remove(row);
//        });
//
//        row.getChildren().add(removeBtn);
//        return row;
//    }
//
//    private void removeEquation(TextField tf, HBox row, VBox parent, Circle circle) {
//        lineChart.getData().removeAll(equationMap.getOrDefault(tf, new ArrayList<>()));
//        equationMap.remove(tf);
//        colorMap.remove(tf);
//        circle.setFill(Color.WHITE);
//        removeError(tf, parent);
//    }
//}