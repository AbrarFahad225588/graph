package com.example.graph;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Calculator {
    private GraphingApp app;
    public Calculator(GraphingApp app) {
        this.app=app;
    }
    protected Scene createCalculatorScene() {
        Text title = new Text("Calculator BY Group Unstable");
        title.setFont(Font.font(100));
        title.setFill(Color.GREEN);
//        Rectangle titleRect = new Rectangle(900, 250);
//        titleRect.setFill(Color.AQUA);
//        titleRect.setStroke(Color.RED);
//        titleRect.setStrokeWidth(6.0);
//        titleRect.setArcWidth(20);
//        titleRect.setArcHeight(20);
        StackPane titlePane = new StackPane(title);
        StackPane.setAlignment(title, Pos.TOP_CENTER);
        titlePane.setTranslateY(30);

        return new Scene(titlePane,900,900);
    }
}
