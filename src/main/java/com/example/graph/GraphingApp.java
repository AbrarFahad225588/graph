package com.example.graph;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class GraphingApp extends Application {
    private Stage stage;
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("Graphing Calculator");
        openMenuScene();
        stage.show();
    }

    protected void openGraphScene() {
        Graph graph = new Graph(this);
        Scene graphScene = graph.createGraphScene();
        stage.setScene(graphScene);
    }

    protected void openMenuScene() {
        Menu menu = new Menu(this);
        Scene menuScene = menu.createMenuScene();
        stage.setScene(menuScene);
    }

    protected void openTrigonmetryScene()
    {
        Trigonmetry trigonmetry=new Trigonmetry(this);
        Scene trigonmetryScene=trigonmetry.createTrigonmetryScene();
        stage.setScene(trigonmetryScene);
    }
}
