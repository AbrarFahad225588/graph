package com.example.graph;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.NotNull;

public class Menu {
    private GraphingApp app;
    public Menu(GraphingApp app) { this.app = app; }

    protected Scene createMenuScene() {
        Text title = new Text("CalioGraphy");
        title.setFont(Font.font(100));
        title.setFill(Color.GREEN);
        Rectangle titleRect = new Rectangle(900, 250);
        titleRect.setFill(Color.AQUA);
        titleRect.setStroke(Color.RED);
        titleRect.setStrokeWidth(6.0);
        titleRect.setArcWidth(20);
        titleRect.setArcHeight(20);
        StackPane titlePane = new StackPane(titleRect, title);
        StackPane.setAlignment(title, Pos.CENTER);
        titlePane.setTranslateY(30);

        Button newGraphButton = createSquareButton("Polynomial", Color.BLUE);
        newGraphButton.setStyle("-fx-background-color: orange; -fx-border-color: red; -fx-border-width: 6px; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        newGraphButton.setOnMouseEntered(e -> newGraphButton.setStyle("-fx-background-color: lightgreen; -fx-border-color: red; -fx-border-width: 6px; -fx-border-radius: 20px; -fx-background-radius: 20px;"));
        newGraphButton.setOnMouseExited(e -> newGraphButton.setStyle("-fx-background-color: orange; -fx-border-color: red; -fx-border-width: 6px; -fx-border-radius: 20px; -fx-background-radius: 20px;"));
        newGraphButton.setOnAction(event -> app.openGraphScene());
        newGraphButton.setTranslateX(80);
        newGraphButton.setTranslateY(-30);



        Button trigonmetryButton = createSquareButton("TrigonMetry", Color.BLUE);
        trigonmetryButton.setStyle("-fx-background-color: orange; -fx-border-color: red; -fx-border-width: 6px; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        trigonmetryButton.setOnMouseEntered(e -> trigonmetryButton.setStyle("-fx-background-color: lightgreen; -fx-border-color: red; -fx-border-width: 6px; -fx-border-radius: 20px; -fx-background-radius: 20px;"));
        trigonmetryButton.setOnMouseExited(e -> trigonmetryButton.setStyle("-fx-background-color: orange; -fx-border-color: red; -fx-border-width: 6px; -fx-border-radius: 20px; -fx-background-radius: 20px;"));
        trigonmetryButton.setOnAction(event -> app.openTrigonmetryScene());
        trigonmetryButton.setTranslateX(80);
        trigonmetryButton.setTranslateY(-30);



        Pane backgroundPane = new Pane();
        backgroundPane.setMinSize(1200, 794);
        backgroundPane.setStyle("-fx-background-color: lightgreen");

        HBox functionBox = new HBox(newGraphButton,trigonmetryButton);
        functionBox.setSpacing(200);
        functionBox.setAlignment(Pos.CENTER);

        VBox menuLayout = new VBox(titlePane, functionBox);
        menuLayout.setSpacing(150);
        menuLayout.setAlignment(Pos.CENTER);

        StackPane rootPane = new StackPane(backgroundPane, menuLayout);
        BorderPane root=new BorderPane();
        root.setCenter(rootPane);
        return new Scene(root, 1200, 794);
    }




    private Button createSquareButton(String text, Color color) {
        Button button = new Button(text);
        button.setMinSize(300, 300);
        button.setFont(Font.font("System Bold", 40));
        button.setTextFill(color);
        return button;
    }
}
