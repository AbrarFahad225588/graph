package com.example.graph.plot;

import com.example.graph.auth.Login;
import com.example.graph.auth.Register;
import com.example.graph.profile.Profile;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import static com.example.graph.auth.AuthService.authenticated;
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

    public void openGraphScene() {
        Graph graph = new Graph(this);
        Graph.UPPER_BOUND=25;
        Graph.LOWER_BOUND=-25;
        Scene graphScene = graph.createGraphScene();
        stage.setScene(graphScene);
    }

    public void openMenuScene() {
        Menu menu = new Menu(this);
        Scene menuScene = menu.createMenuScene();
        stage.setScene(menuScene);
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

}
