package com.example.graph.plot;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.SVGPath;
public class Credits {
    GraphingApp app;
    public Credits(GraphingApp app) {
        this.app = app;
    }
    public Scene createCreditsScene() {
        StackPane root = new StackPane();
        Stop[] stops = new Stop[] {
                new Stop(0, Color.web("#1e4a8e")),
                new Stop(1, Color.web("#3b82f6"))
        };
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        root.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        SVGPath wave = new SVGPath();
        wave.setContent("M0,100 C150,200 350,0 500,100 C650,200 850,0 1000,100 L1000,200 L0,200 Z");
        wave.setFill(Color.WHITE);
        wave.setScaleX(1.2); // Ensure it covers the width
        StackPane.setAlignment(wave, Pos.BOTTOM_CENTER);

        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(50));
        Label mainTitle = new Label("CREDITS");
        mainTitle.setStyle("-fx-font-size: 60px; -fx-font-weight: 900; -fx-text-fill: white; -fx-underline: true;");
        VBox supervisorBox = new VBox(5);
        supervisorBox.setAlignment(Pos.CENTER);
        Label supervisorLabel = new Label("SUPERVISOR");
        supervisorLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-letter-spacing: 2px;");
        Label supervisorName = new Label("Md. Fazle Rabbi");
        supervisorName.setStyle("-fx-font-size: 32px; -fx-text-fill: white; -fx-font-family: 'serif';");
        Label supervisorTitle = new Label("Professor, Dept. of Computer Science and Engineering");
        supervisorTitle.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-style: italic;");
        supervisorBox.getChildren().addAll(supervisorLabel, supervisorName, supervisorTitle);
        HBox teamBox = new HBox(60);
        teamBox.setAlignment(Pos.CENTER);
        teamBox.setPadding(new Insets(40, 0, 40, 0));
        teamBox.getChildren().addAll(
                createMemberNode("Md. Motairul Hoque Fahad", "2302024"),
                createMemberNode("Sobuj Chandra Roy", "2302016")
        );
        Button backBtn = new Button("Back to Menu");
        backBtn.setStyle("-fx-background-color: #33e1ed; -fx-text-fill: #1e4a8e; -fx-font-weight: bold; " +
                "-fx-padding: 10 30; -fx-background-radius: 5; -fx-font-size: 16px;");
        backBtn.setOnAction(e -> app.openHomeScene());
        backBtn.setOnMouseEntered(e -> {
            backBtn.setStyle("-fx-background-color: white; -fx-text-fill: #1e4a8e; -fx-font-weight: bold; " +
                    "-fx-padding: 10 30; -fx-background-radius: 5; -fx-font-size: 16px; -fx-cursor: hand;");
        });
        backBtn.setOnMouseExited(e -> {
            backBtn.setStyle("-fx-background-color: #33e1ed; -fx-text-fill: #1e4a8e; -fx-font-weight: bold; " +
                    "-fx-padding: 10 30; -fx-background-radius: 5; -fx-font-size: 16px;");
        });
        content.getChildren().addAll(mainTitle, supervisorBox, teamBox, backBtn);
        root.getChildren().addAll(wave, content);
        return new Scene(root, 1000, 700);
    }
    private VBox createMemberNode(String name, String id) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");
        Label idLabel = new Label(id);
        idLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        box.getChildren().addAll(nameLabel, idLabel);
        return box;
    }
}