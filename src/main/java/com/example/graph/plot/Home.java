package com.example.graph.plot;

import javafx.application.Application;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Home {
    GraphingApp app;

    public  Home(GraphingApp app) {
        this.app=app;}
    public Scene createHomeScene(){
        VBox root = new VBox();
        root.setStyle("-fx-background-color: white;");


        HBox navbar = createNavbar();


        StackPane heroSection = createHeroSection();

        // 3. Tools Section (Bottom)
        VBox toolsSection = createToolsSection();

        root.getChildren().addAll(navbar, heroSection, toolsSection);

        Scene scene = new Scene(root, 1000, 700);
        return  scene;
    }

    private HBox createNavbar() {
        HBox nav = new HBox(20);
        nav.setPadding(new Insets(10, 40, 10, 40));
        nav.setAlignment(Pos.CENTER_LEFT);
        nav.setStyle("-fx-background-color: white;");

        Label logo = new Label("Calligraphy");
        logo.setStyle("-fx-font-size: 24px; -fx-text-fill: #808080; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button login = new Button("Log In");
        login.setStyle("-fx-background-color: transparent; -fx-border-color: #ccc; -fx-border-radius: 4;");
        login.setOnAction(e->{
            app.openLoginScene();
        });
        Button register = new Button("Register");
        register.setStyle("-fx-background-color: #2d70b3; -fx-text-fill: white; -fx-font-weight: bold;");
          register.setOnAction(e->{
              app.openRegisterScene();
          });
        nav.getChildren().addAll(logo, spacer, login, register);
        return nav;
    }

    private StackPane createHeroSection() {
        StackPane hero = new StackPane();

        // Gradient Background
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#1e4a8e")), new Stop(1, Color.web("#3b82f6"))};
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        hero.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        hero.setPrefHeight(400);

        // Content Container
        HBox content = new HBox(40);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));

        // Left Text
        VBox leftText = new VBox(10);
        leftText.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Beautiful free math.");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 42px; -fx-font-weight: bold;");
        Label subTitle = new Label("At calligraphy Studio, we want to help everyone learn\nmath, love math, and grow with math.");
        subTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
        Button openCalc = new Button("Open Graphing Calculator");
        openCalc.setStyle("-fx-background-color: white; -fx-text-fill: #333; -fx-font-size: 16px; -fx-padding: 10 20;");
         openCalc.setOnAction(e->{
               app.openGraphScene();
         });
        leftText.getChildren().addAll(title, subTitle, openCalc);


        VBox graphCard = new VBox();
        graphCard.setPrefSize(300, 300);
        graphCard.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");


        content.getChildren().addAll(leftText, graphCard);


        SVGPath wave = new SVGPath();
        wave.setContent("M0,100 C150,200 350,0 500,100 C650,200 850,0 1000,100 L1000,200 L0,200 Z");
        wave.setFill(Color.WHITE);
        StackPane.setAlignment(wave, Pos.BOTTOM_CENTER);

        hero.getChildren().addAll(content, wave);
        return hero;
    }

    private VBox createToolsSection() {
        VBox tools = new VBox(30);
        tools.setAlignment(Pos.CENTER);
        tools.setPadding(new Insets(40));

        Label sectionTitle = new Label("Explore all of our math tools!");
        sectionTitle.setStyle("-fx-font-size: 24px; -fx-text-fill: #333;");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setAlignment(Pos.CENTER);

        // Tool Item Helper
        VBox graphing=createToolIcon("Graphing", "#22c55e");
        grid.add(graphing, 0, 0);
        graphing.setOnMouseClicked(e->{
            app.openGraphScene();
        });
        graphing.setOnMouseEntered(e->{

        });

        tools.getChildren().addAll(sectionTitle, grid);
        return tools;
    }

    private VBox createToolIcon(String name, String hexColor) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        StackPane iconRect = new StackPane();
        iconRect.setPrefSize(80, 80);
        iconRect.setStyle("-fx-background-color: " + hexColor + "; -fx-background-radius: 15;");

        Label label = new Label(name);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        box.setOnMouseEntered(e->{
            iconRect.setStyle("-fx-background-color: " + "#5ca742" + "; -fx-background-radius: 15;");
        });
        box.getChildren().addAll(iconRect, label);
        return box;
    }

}