
package com.example.graph.plot;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.graph.plot.PlotEquation.plotEquation;
import static  com.example.graph.auth.AuthService.authenticated;
public class Graph {

    public static int LOWER_BOUND = -25;
    public static int UPPER_BOUND = 25;
    private final NumberAxis xAxis = new NumberAxis(LOWER_BOUND, UPPER_BOUND, 1);
    private final NumberAxis yAxis = new NumberAxis(LOWER_BOUND, UPPER_BOUND, 1);
    private final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

    private final GraphingApp app;
    private final Map<TextField, ArrayList<XYChart.Series<Number, Number>>> equationMap = new HashMap<>();
    private final Map<TextField, Color> colorMap = new HashMap<>();
    private final Map<TextField, Label> errorMap = new HashMap<>();
    public Graph(GraphingApp app) { this.app = app; }
        private double lastMouseX, lastMouseY;

    private void applyAllMouseActions(LineChart<Number, Number> chart) {
        chart.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                double dataX = xAxis.getValueForDisplay(e.getX()).doubleValue();
                double dataY = yAxis.getValueForDisplay(e.getY()).doubleValue();
                if (e.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                    applyZoom(dataX, dataY, 0.5);
                } else {
                    applyZoom(dataX, dataY, 2.0);
                }
            }
        });

        chart.setOnScroll(e -> {
            double dataX = xAxis.getValueForDisplay(e.getX()).doubleValue();
            double dataY = yAxis.getValueForDisplay(e.getY()).doubleValue();
            double zoomFactor = (e.getDeltaY() > 0) ? 0.8 : 1.2;
            applyZoom(dataX, dataY, zoomFactor);
        });

        chart.setOnMousePressed(e -> {
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        });

        chart.setOnMouseDragged(e -> {
            double deltaX = e.getX() - lastMouseX;
            double deltaY = e.getY() - lastMouseY;
            double xShift = (xAxis.getUpperBound() - xAxis.getLowerBound()) * (deltaX / chart.getWidth());
            double yShift = (yAxis.getUpperBound() - yAxis.getLowerBound()) * (deltaY / chart.getHeight());

            xAxis.setLowerBound(xAxis.getLowerBound() - xShift);
            xAxis.setUpperBound(xAxis.getUpperBound() - xShift);
            yAxis.setLowerBound(yAxis.getLowerBound() + yShift);
            yAxis.setUpperBound(yAxis.getUpperBound() + yShift);

            lastMouseX = e.getX();
            lastMouseY = e.getY();
            LOWER_BOUND = (int) xAxis.getLowerBound();
            UPPER_BOUND = (int) xAxis.getUpperBound();
            equationMap.keySet().forEach(tf -> {
                if (!tf.getText().trim().isEmpty()) {
                    plotEquation((HBox) tf.getParent(), tf.getText(), equationMap, lineChart, colorMap, errorMap);
                }
            });
        });
    }

    private void applyZoom(double x, double y, double factor) {
        double xLower = xAxis.getLowerBound();
        double xUpper = xAxis.getUpperBound();
        double yLower = yAxis.getLowerBound();
        double yUpper = yAxis.getUpperBound();

        xAxis.setLowerBound(x - (x - xLower) * factor);
        xAxis.setUpperBound(x + (xUpper - x) * factor);
        yAxis.setLowerBound(y - (y - yLower) * factor);
        yAxis.setUpperBound(y + (yUpper - y) * factor);

        LOWER_BOUND = (int) xAxis.getLowerBound();
        UPPER_BOUND = (int) xAxis.getUpperBound();

        equationMap.keySet().forEach(tf -> {
            if (!tf.getText().trim().isEmpty()) {
                plotEquation((HBox) tf.getParent(), tf.getText(), equationMap, lineChart, colorMap, errorMap);
            }
        });
    }

    protected Scene createGraphScene() {
        applyAllMouseActions(lineChart);

        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(true);
        lineChart.setAnimated(true);
        lineChart.setTitle("CalliGraphy Graph");
        lineChart.setStyle("-fx-background-color: white; -fx-border-color: #dcdde1; -fx-border-width: 1;");
        lineChart.setMinHeight(500);
        VBox.setVgrow(lineChart, Priority.ALWAYS);// akhne change anlam
        VBox mainVBox = new VBox(10);
        mainVBox.setPadding(new Insets(15));
        mainVBox.setStyle("-fx-background-color: #f5f6fa;");

        VBox bottomPanel = new VBox(10);
        bottomPanel.setPadding(new Insets(20));
        bottomPanel.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Label equationLabel = new Label("Equations");
        equationLabel.setFont(Font.font("System", FontWeight.BOLD, 20));

        VBox equationListContainer = new VBox(5);
        equationListContainer.getChildren().add(createEquationBox(bottomPanel));
        Button addEquationBtn = createOptionButton("Add", "#2ecc71", () -> {
            equationListContainer.getChildren().add(addEquationBox(bottomPanel));
        });
        Button menuBtn = createOptionButton("Home", "#3498db", app::openHomeScene);
        Button newGraphBtn = createOptionButton("Reset", "#f39c12", app::openGraphScene);

        HBox buttonBox = new HBox(15, addEquationBtn, menuBtn, newGraphBtn);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        bottomPanel.getChildren().addAll(equationLabel, equationListContainer, buttonBox);
        HBox nav = new HBox();
        nav.setPadding(new Insets(0, 0, 10, 0));
        nav.setAlignment(Pos.CENTER);
        nav.setStyle("-fx-background-color: white; -fx-padding: 15;");

        Button rlabel = createOptionButton("Register", "#50C878", app::openRegisterScene);
        Button llabel = createOptionButton("Login", "#c0392b", app::openLoginScene);
        Button plabel = createOptionButton("Profile", "#50C878", app::openProfileScene);
        Button logoutlabel = createOptionButton("Log out", "#c0392b", app::openLogOutScene);
        Button savePng = createOptionButton("Save Graph", "#50C878", this::savePNG);

        HBox rightButtons = new HBox(5);
        if (authenticated == null) {
            rightButtons.getChildren().addAll(rlabel, llabel);
        } else {
            rightButtons.getChildren().addAll(plabel, logoutlabel);
        }

        HBox animatedTitle = createAnimatedTitle();

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();

        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        nav.getChildren().addAll(
                savePng,
                leftSpacer,
                animatedTitle,
                rightSpacer,
                rightButtons
        );

        mainVBox.getChildren().addAll(nav,lineChart, bottomPanel);
        ScrollPane rootPane = new ScrollPane(mainVBox);
        rootPane.setFitToWidth(true);
        rootPane.setFitToHeight(false);
        rootPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        rootPane.setPannable(true);
        return new Scene(rootPane, 1200, 794);
    }
    private HBox createAnimatedTitle() {

        HBox titleBox = new HBox(2);
        titleBox.setAlignment(Pos.CENTER);

        String text = "CalliGraphy Graph";

        for (char c : text.toCharArray()) {
            Label letter = new Label(String.valueOf(c));
            letter.setFont(Font.font("System", 20));
            titleBox.getChildren().add(letter);
        }

        AnimationTimer timer = new AnimationTimer() {

            private double time = 0;

            @Override
            public void handle(long now) {

                time += 0.08;

                for (int i = 0; i < titleBox.getChildren().size(); i++) {

                    Label letter = (Label) titleBox.getChildren().get(i);

                    double y = Math.sin(time + i * 0.5) * 15;
                    letter.setTranslateY(y);
                    double hue = (time * 60 + i * 20) % 360;
                    letter.setTextFill(Color.hsb(hue, 1.0, 1.0));
                }
            }
        };
        timer.start();
        return titleBox;
    }
    private HBox createEquationBox(VBox bottomPanel) {
        HBox equationBox = new HBox(15);
        equationBox.setAlignment(Pos.CENTER_LEFT);
        equationBox.setPadding(new Insets(5, 0, 5, 0));

        Circle circle = new Circle(12, Color.WHITE);
        circle.setStroke(Color.web("#bdc3c7"));
        circle.setStrokeWidth(2);

        Label yLabel = new Label("y =");
        yLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 18));

        TextField tf = new TextField();
        tf.setPromptText("e.g. x^2 + 5x + 2");
        tf.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #dcdde1; -fx-padding: 8;");
        tf.setFont(new Font(16));
        HBox.setHgrow(tf, Priority.ALWAYS);

        tf.textProperty().addListener((obs, old, newValue) -> {
            if (newValue.isEmpty()) {
                lineChart.getData().removeAll(equationMap.getOrDefault(tf, new ArrayList<>()));
                equationMap.remove(tf);
                circle.setFill(Color.WHITE);
                removeError(tf, equationBox);
            } else {
                plotEquation(equationBox, newValue, equationMap, lineChart, colorMap, errorMap);
            }
        });

        equationBox.getChildren().addAll(circle, yLabel, tf);
        return equationBox;
    }

    private VBox addEquationBox(VBox bottomPanel) {
        VBox container = new VBox();
        HBox equationBox = createEquationBox(bottomPanel);

        Button removeBtn = new Button("✕");
        removeBtn.setStyle("-fx-background-color: #ff7675; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        removeBtn.setPrefSize(35, 35);

        TextField tf = (TextField) equationBox.getChildren().get(2);
        Circle circle = (Circle) equationBox.getChildren().get(0);

        removeBtn.setOnAction(e -> {
            lineChart.getData().removeAll(equationMap.getOrDefault(tf, new ArrayList<>()));
            equationMap.remove(tf);
            colorMap.remove(tf);
            removeError(tf, equationBox);
            ((VBox)container.getParent()).getChildren().remove(container);
        });

        equationBox.getChildren().add(removeBtn);
        container.getChildren().add(equationBox);
        return container;
    }

    private void removeError(TextField tf, HBox equationBox) {
        if (errorMap.containsKey(tf)) {
            VBox container = (VBox) equationBox.getParent();
            container.getChildren().remove(errorMap.get(tf));
            errorMap.remove(tf);
        }
    }

    private Button createOptionButton(String title, String color, Runnable action) {
        Button button = new Button(title);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        button.setPrefSize(120, 40);
        button.setFont(new Font(14));
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;"));
        button.setOnAction(e -> action.run());
        return button;
    }

    public void savePNG() {
         if(authenticated!=null)
         {
             FileChooser fileChooser = new FileChooser();
             fileChooser.getExtensionFilters()
                     .add(new FileChooser.ExtensionFilter("PNG", "*.png"));

             LocalDateTime now = LocalDateTime.now();
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
             String timestamp = now.format(formatter);
             fileChooser.setInitialFileName("y_" + timestamp + ".png");

             File file = fileChooser.showSaveDialog(null);

             if (file != null) {
                 try {
                     ImageIO.write(
                             SwingFXUtils.fromFXImage(
                                     lineChart.snapshot(new SnapshotParameters(), null),
                                     null),
                             "png", file
                     );
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }else{
             app.openLoginScene();
         }
         }

}