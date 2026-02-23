package com.example.graph.plot;

import com.example.graph.auth.Login;
import com.example.graph.auth.Register;
import com.example.graph.profile.Profile;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.Objects;

import static com.example.graph.auth.AuthService.authenticated;
public class GraphingApp extends Application {
    private Stage stage;
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("calligraphy");
        try {

            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/graph/sinewave.png")));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Could not load application icon: " + e.getMessage());
        }
        openHomeScene();
        stage.show();

    }

    public void openGraphScene() {
        Graph graph = new Graph(this);
        Graph.UPPER_BOUND=25;
        Graph.LOWER_BOUND=-25;
        Scene graphScene = graph.createGraphScene();
        stage.setScene(graphScene);
    }

    public void openHomeScene() {
        Home home = new Home(this);
        Scene homeScene = home.createHomeScene();
        stage.setScene(homeScene);
    }


    public void openRegisterScene() {
        Register register = new Register(this);
        Scene registerScene = register.createRegisterScene();
        stage.setScene(registerScene);
    }
    public void openLoginScene() {
        Login login = new Login(this);
        Scene loginScene = login.createLoginScene();
        stage.setScene(loginScene);
    }
    public void openLogOutScene() {
        if(authenticated!=null)
        {
            authenticated=null;
            openLoginScene();
        }else
        {
            throw  new Error("You are already Log out");
        }

    }
    public void openProfileScene() {
        Profile profile = new Profile(authenticated,this);
        Scene profileScene = profile.createProfileScene();
        stage.setScene(profileScene);

    }
    public void openCreditsScene() {
        Credits credits = new Credits(this);
        Scene creditsScene = credits.createCreditsScene();
        stage.setScene(creditsScene);

    }

}
