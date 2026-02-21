
package com.example.graph.plot;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Menu {
    private GraphingApp app;
    public Menu(GraphingApp app) { this.app = app; }

    protected Scene createMenuScene() {
        // 1. Root Container using StackPane for background + content layers
        StackPane rootPane = new StackPane();
        rootPane.setStyle("-fx-background-color: lightgreen;");

        // 2. Title Section
        Text title = new Text("CalioGraphy");
        title.setFill(Color.GREEN);

        // Responsive Font: adjust font size based on window width
        title.setFont(Font.font("System", FontWeight.BOLD, 100));

// 2. Responsive Font Listener
// This listens to the width of the root pane and recalculates the Font object
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            double scalingFactor = 0.08; // Adjust this to make text bigger/smaller
            double newSize = newVal.doubleValue() * scalingFactor;

            // Clamp the size so it doesn't get ridiculously small or large
            newSize = Math.max(40, Math.min(newSize, 120));

            title.setFont(Font.font("System", FontWeight.BOLD, newSize));
        });

        Rectangle titleRect = new Rectangle();
        titleRect.setFill(Color.AQUA);
        titleRect.setStroke(Color.RED);
        titleRect.setStrokeWidth(6.0);
        titleRect.setArcWidth(20);
        titleRect.setArcHeight(20);

        // Bind Rectangle size to a percentage of the window
        titleRect.widthProperty().bind(rootPane.widthProperty().multiply(0.75));
        titleRect.heightProperty().bind(rootPane.heightProperty().multiply(0.25));

        StackPane titlePane = new StackPane(titleRect, title);
        titlePane.setPadding(new Insets(20));

        // 3. Button Section (Flexible FlowPane or HBox)
        // FlowPane automatically wraps buttons to the next line if the window is too narrow
        FlowPane buttonLayout = new FlowPane();
        buttonLayout.setHgap(40);
        buttonLayout.setVgap(40);
        buttonLayout.setAlignment(Pos.CENTER);

        Button graphBtn = createResponsiveButton("Graph", Color.BLUE, () -> app.openGraphScene());
        Button registerBtn = createResponsiveButton("Register", Color.BLUE, () -> app.openRegisterScene());
        Button loginBtn = createResponsiveButton("Login", Color.BLUE, () -> app.openLoginScene());

        buttonLayout.getChildren().addAll(graphBtn, registerBtn, loginBtn);

        // 4. Main Vertical Layout
        VBox menuLayout = new VBox(50); // 50px spacing between Title and Buttons
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.getChildren().addAll(titlePane, buttonLayout);

        rootPane.getChildren().add(menuLayout);

        return new Scene(rootPane, 1200, 794);
    }

    private Button createResponsiveButton(String text, Color color, Runnable action) {
        Button button = new Button(text);

        // Use CSS for styling - much cleaner than manual setters
        String baseStyle = "-fx-background-color: orange; " +
                "-fx-border-color: red; " +
                "-fx-border-width: 6px; " +
                "-fx-border-radius: 20px; " +
                "-fx-background-radius: 20px; " +
                "-fx-cursor: hand;";

        button.setStyle(baseStyle);
        button.setTextFill(color);
        button.setFont(Font.font("System Bold", 30));

        // Responsive button sizing:
        // Max size large, but allow it to shrink/grow based on content
        button.setMinWidth(200);
        button.setMinHeight(200);
        button.setPrefSize(300, 300);

        // Hover effects using standard Java logic
        button.setOnMouseEntered(e -> button.setStyle(baseStyle + "-fx-background-color: lightgreen;"));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        button.setOnAction(event -> action.run());

        return button;
    }
}