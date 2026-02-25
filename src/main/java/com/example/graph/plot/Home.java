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
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;


import java.util.Objects;

public class Home {
    GraphingApp app;

    public  Home(GraphingApp app) {
        this.app=app;}
    public Scene createHomeScene(){
        VBox root = new VBox();
        root.setStyle("-fx-background-color: white;");
        HBox navbar = createNavbar();
        StackPane heroSection = createHeroSection();
        VBox toolsSection = createToolsSection();
        HBox footer = createFooter();
        root.getChildren().addAll(navbar, heroSection, toolsSection, footer);
        VBox.setVgrow(toolsSection, Priority.ALWAYS);
        Scene scene = new Scene(root, 1000, 750); // Increased height slightly to accommodate footer
        return scene;
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
        login.setStyle("-fx-background-color: #F44336;-fx-text-fill: white; -fx-font-weight: bold;");
        login.setOnAction(e->{
            app.openLoginScene();
        });
        login.setOnMouseEntered(e -> {
            login.setStyle("-fx-background-color: #F44999;-fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
            login.setTranslateY(-2); 
        });

        login.setOnMouseExited(e -> {
            login.setStyle("-fx-background-color: #F44336;-fx-text-fill: white; -fx-font-weight: bold;");
            login.setTranslateY(0);
        });
        Button register = new Button("Register");
        register.setStyle("-fx-background-color: #2d70b3; -fx-text-fill: white; -fx-font-weight: bold;");
          register.setOnAction(e->{
              app.openRegisterScene();
          });
        register.setOnMouseEntered(e -> {
            register.setStyle("-fx-background-color: #1b9d4b;-fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
            register.setTranslateY(-2); 
        });

        register.setOnMouseExited(e -> {
            register.setStyle("-fx-background-color: #2d70b3; -fx-text-fill: white; -fx-font-weight: bold;");
            register.setTranslateY(0);
        });
        nav.getChildren().addAll(logo, spacer, login, register);
        return nav;
    }

    private StackPane createHeroSection() {
        StackPane hero = new StackPane();
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#1e4a8e")), new Stop(1, Color.web("#3b82f6"))};
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        hero.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        hero.setPrefHeight(400);
        HBox content = new HBox(40);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
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
        openCalc.setOnMouseEntered(e -> {
            openCalc.setStyle("-fx-background-color: white; -fx-text-fill: #333; -fx-font-size: 16px; -fx-padding: 10 20; -fx-cursor: hand;");
            openCalc.setTranslateY(-2); 
        });

        openCalc.setOnMouseExited(e -> {
            openCalc.setStyle("-fx-background-color: white; -fx-text-fill: #333; -fx-font-size: 16px; -fx-padding: 10 20;");
            openCalc.setTranslateY(0);
        });
        leftText.getChildren().addAll(title, subTitle, openCalc);
        VBox graphCard = new VBox();
        graphCard.setPrefSize(300, 300);
        graphCard.setMinSize(300, 300);
        graphCard.setMaxSize(300, 300);
        String imagePath = Objects.requireNonNull(getClass().getResource("/com/example/graph/graphing-calculator.png")).toExternalForm();
        graphCard.setStyle(
                "-fx-background-image: url('" + imagePath + "'); " +
                        "-fx-background-size: cover; " +
                        "-fx-background-position: center; " +
                        "-fx-background-repeat: no-repeat; " +
                        "-fx-background-radius: 20; " + 
                        "-fx-border-radius: 20; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 15, 0, 0, 5);"
        );

        Rectangle clip = new Rectangle(300, 300);
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        graphCard.setClip(clip);
        content.getChildren().addAll(leftText, graphCard);

        SVGPath wave = new SVGPath();
        wave.setContent("M0,100 C150,200 350,0 500,100 C650,200 850,0 1000,100 L1000,200 L0,200 Z");
        wave.setFill(Color.WHITE);
        wave.setManaged(false);
        wave.scaleXProperty().bind(hero.widthProperty().divide(1000.0));
        wave.layoutXProperty().bind(hero.widthProperty().divide(2).subtract(500));
        wave.layoutYProperty().bind(hero.heightProperty().subtract(150));
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
        VBox graphing=createToolIcon("Graphing", "#22c55e");
        grid.add(graphing, 0, 0);
        graphing.setOnMouseClicked(e->{
            app.openGraphScene();
        });
        tools.getChildren().addAll(sectionTitle, grid);
        return tools;
    }
    private VBox createToolIcon(String name, String hexColor) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        StackPane iconRect = new StackPane();
        iconRect.setPrefSize(80, 80);
        String baseStyle = "-fx-background-color: " + hexColor + "; -fx-background-radius: 15;";
        iconRect.setStyle(baseStyle);
        SVGPath wave = new SVGPath();
        wave.setContent("M10,40 Q25,10 40,40 T70,40");
        wave.setStroke(Color.WHITE);
        wave.setStrokeWidth(3);
        wave.setFill(null);
        iconRect.getChildren().add(wave);
        Label label = new Label(name);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        box.setOnMouseEntered(e -> {
            iconRect.setStyle("-fx-background-color: #1b9d4b; -fx-background-radius: 15; -fx-cursor: hand;");
            box.setTranslateY(-2); 
        });
        box.setOnMouseExited(e -> {
            iconRect.setStyle(baseStyle);
            box.setTranslateY(0);
        });
        box.getChildren().addAll(iconRect, label);
        return box;
    }
    private HBox createFooter() {
        HBox footer = new HBox(20);
        footer.setPadding(new Insets(20, 40, 20, 40));
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #e9ecef; -fx-border-width: 1 0 0 0;");

        Label footerText = new Label("© 2026 Calligraphy Studio. Dedicated to mathematical exploration.");
        footerText.setStyle("-fx-font-size: 14px; -fx-text-fill: #6c757d;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button creditsBtn = new Button("Credits");
        creditsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #2d70b3; -fx-border-color: #2d70b3; -fx-border-radius: 5;");

        creditsBtn.setOnMouseEntered(e -> {
            creditsBtn.setStyle("-fx-background-color: #2d70b3; -fx-text-fill: white; -fx-border-radius: 5; -fx-cursor: hand;");
        });
        creditsBtn.setOnMouseExited(e -> {
            creditsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #2d70b3; -fx-border-color: #2d70b3; -fx-border-radius: 5;");
        });

        creditsBtn.setOnAction(e -> {

            app.openCreditsScene();
        });

        footer.getChildren().addAll(footerText, spacer, creditsBtn);
        return footer;
    }

}