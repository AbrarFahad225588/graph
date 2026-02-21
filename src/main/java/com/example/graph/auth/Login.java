package com.example.graph.auth;

import com.example.graph.plot.GraphingApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.graph.auth.AuthService.authenticated;

public class Login {

    private GraphingApp app;
    private Map<String, TextInputControl> inputMap = new HashMap<>();

    public Login(GraphingApp app) {
        this.app = app;
    }

    public Scene createLoginScene() {
        // 1. Root Container (Same as Register)
        StackPane rootContainer = new StackPane();
        rootContainer.setStyle("-fx-background-color: #f4f4f4;");

        // 2. The Form Card (Same as Register)
        VBox formCard = new VBox(30);
        formCard.setPadding(new Insets(40));
        formCard.setMaxWidth(500);
        formCard.setMaxHeight(500); // Slightly shorter since fewer fields
        formCard.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        formCard.setAlignment(Pos.CENTER);

        // Heading
        Label heading = new Label("Welcome Back");
        heading.setFont(Font.font("System", FontWeight.BOLD, 32));
        heading.setTextFill(Color.web("#2c3e50"));

        // 3. Responsive GridPane
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(35);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(65);
        grid.getColumnConstraints().addAll(col1, col2);

        // --- Fields Logic (Username and Password only) ---
        String[] labelTexts = {"User Name:", "Password:"};
        for (int i = 0; i < labelTexts.length; i++) {
            Label label = new Label(labelTexts[i]);
            label.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));

            TextInputControl inputField;
            if (labelTexts[i].equals("Password:")) {
                inputField = new PasswordField();
                inputField.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
            } else {
                inputField = new TextField();
                inputField.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
            }

            inputField.setStyle("-fx-background-radius: 5; -fx-padding: 8; -fx-border-color: #dcdde1; -fx-border-radius: 5;");
            inputField.setMaxWidth(Double.MAX_VALUE);

            inputMap.put(labelTexts[i], inputField);
            grid.add(label, 0, i);
            grid.add(inputField, 1, i);
        }

        // 4. Buttons Layout
        Button loginBtn = new Button("Login");
        Button backBtn = new Button("Back");

        // Styling Buttons (Matching your Register design)
        String btnBase = "-fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;";
        loginBtn.setStyle(btnBase + "-fx-background-color: #3498db; -fx-text-fill: white;"); // Blue for login
        backBtn.setStyle(btnBase + "-fx-background-color: #95a5a6; -fx-text-fill: white;"); // Gray for back

        loginBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setMaxWidth(Double.MAX_VALUE);

        HBox buttonBox = new HBox(15, backBtn, loginBtn);
        buttonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(loginBtn, Priority.ALWAYS);
        HBox.setHgrow(backBtn, Priority.ALWAYS);

        // Assemble
        formCard.getChildren().addAll(heading, grid, buttonBox);
        rootContainer.getChildren().add(formCard);

        // --- Login Logic ---
        loginBtn.setOnAction(e -> {
            String userName = inputMap.get("User Name:").getText();
            String password = inputMap.get("Password:").getText();

            List<User> users = FileDatabase.loadUsers();


            for (User user : users) {
                if (user.getUsername().equals(userName)) {
                    // Use BCrypt to check the entered password against the hashed one
                    if (BCrypt.checkpw(password, user.getPassword())) {
                        authenticated =user;
                        break;
                    }
                }
            }

            if (authenticated!=null) {
                System.out.println("Login successful!");
                app.openGraphScene();
            } else {
                System.out.println("Invalid Username or Password!");

            }
        });

        backBtn.setOnAction(e -> app.openMenuScene());

        return new Scene(rootContainer, 1200, 794);
    }
}
