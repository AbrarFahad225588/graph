package com.example.graph;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Trigonmetry {
     public  GraphingApp app;

    public Trigonmetry(GraphingApp app) {
        this.app=app;
    }

    public Scene createTrigonmetryScene()
    {
        BorderPane borderPane = new BorderPane();

        Scene scene=new Scene(borderPane,800,900);
        return scene;
    }

}
