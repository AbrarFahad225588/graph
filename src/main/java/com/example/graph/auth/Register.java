
package com.example.graph.auth;
import org.mindrot.jbcrypt.BCrypt;
import com.example.graph.plot.GraphingApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register {
    private GraphingApp app;
    private Map<String, TextInputControl> inputMap = new HashMap<>();
    public Register(GraphingApp app) {
        this.app = app;
    }

    public Scene createRegisterScene() {
        // 1. Root Container: StackPane allows centering the form on any screen size
        StackPane rootContainer = new StackPane();
        rootContainer.setStyle("-fx-background-color: #f4f4f4;"); // Light gray background

        // 2. The Form Card: A VBox to hold the header and the grid
        VBox formCard = new VBox(30);
        formCard.setPadding(new Insets(40));
        formCard.setMaxWidth(500); // Prevents the form from getting too wide
        formCard.setMaxHeight(600);
        formCard.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        formCard.setAlignment(Pos.CENTER);

        // Heading
        Label heading = new Label("Create Account");
        heading.setFont(Font.font("System", FontWeight.BOLD, 32));
        heading.setTextFill(Color.web("#2c3e50"));

        // 3. Responsive GridPane
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        // Make columns responsive: Label takes 30%, TextField takes 70%
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(35);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(65);
        grid.getColumnConstraints().addAll(col1, col2);

        // --- Your Original Logic Starts Here ---
        String[] labelTexts = {"User Name:", "Password:", "Email:", "Phone:"};
        for (int i = 0; i < labelTexts.length; i++) {
            Label label = new Label(labelTexts[i]);
            label.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));

            TextInputControl inputField;
            if (labelTexts[i].equals("Password:")) {
                inputField = new PasswordField();
                StackPane passwordContainer = new StackPane();

                PasswordField pf = new PasswordField();
                TextField tfVisible = new TextField();
                tfVisible.setManaged(false);
                tfVisible.setVisible(false);

                Button toggleBtn = new Button("👁"); // এই আইকনটি শো/হাইড করবে
                toggleBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                StackPane.setAlignment(toggleBtn, Pos.CENTER_RIGHT);

                // Toggle logic
                toggleBtn.setOnAction(event -> {
                    if (pf.isVisible()) {
                        tfVisible.setText(pf.getText());
                        tfVisible.setVisible(true);
                        tfVisible.setManaged(true);
                        pf.setVisible(false);
                        pf.setManaged(false);
                        toggleBtn.setText("🙈");
                    } else {
                        pf.setText(tfVisible.getText());
                        pf.setVisible(true);
                        pf.setManaged(true);
                        tfVisible.setVisible(false);
                        tfVisible.setManaged(false);
                        toggleBtn.setText("👁");
                    }
                });

                // স্টাইলিং
                pf.setStyle("-fx-background-radius: 5; -fx-padding: 8 30 8 8; -fx-border-color: #dcdde1; -fx-border-radius: 5;");
                tfVisible.setStyle(pf.getStyle());

                passwordContainer.getChildren().addAll(pf, tfVisible, toggleBtn);
                inputMap.put(labelTexts[i], pf); // মূল Map এ PasswordField রাখলাম

                grid.add(label, 0, i);
                grid.add(passwordContainer, 1, i);
                continue;
            } else {
                inputField = new TextField();
            }

            // Modern styling for inputs
            inputField.setStyle("-fx-background-radius: 5; -fx-padding: 8; -fx-border-color: #dcdde1; -fx-border-radius: 5;");
            inputField.setMaxWidth(Double.MAX_VALUE); // Allows field to grow
            inputMap.put(labelTexts[i], inputField);
            grid.add(label, 0, i);
            grid.add(inputField, 1, i);
        }
        // --- Your Original Logic Ends Here ---

        // 4. Buttons Layout
        Button register = new Button("Register");
        register.setDefaultButton(true);
        Button cancel = new Button("Cancel");
        Label label=new Label();
        // Styling Buttons
        String btnBase = "-fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;";
        register.setStyle(btnBase + "-fx-background-color: #2ecc71; -fx-text-fill: white;");
        cancel.setStyle(btnBase + "-fx-background-color: #e74c3c; -fx-text-fill: white;");

        register.setMaxWidth(Double.MAX_VALUE);
        cancel.setMaxWidth(Double.MAX_VALUE);

        HBox buttonBox = new HBox(15, cancel, register);
        buttonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(register, Priority.ALWAYS);
        HBox.setHgrow(cancel, Priority.ALWAYS);

        // Assemble
        formCard.getChildren().addAll(heading, grid, buttonBox);
        rootContainer.getChildren().add(formCard);

        // Logic for navigation
                register.setOnAction(e -> {
            // 3. This is how you get the text now!
            String userName = inputMap.get("User Name:").getText();
            List<User> users = FileDatabase.loadUsers();

                    for (User user : users) {
                        if (user.getUsername().equals(userName)) {
                            label.setText("Username already exists!");

//                            System.out.println("Username already exists!");
                            return;
                        }
                    }
            String password = inputMap.get("Password:").getText();
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                    int newId = users.size() + 1;

            String email = inputMap.get("Email:").getText();
            String phone = inputMap.get("Phone:").getText();
            User newUser = new User(newId, userName, hashedPassword,email,phone);
            users.add(newUser);
            FileDatabase.saveUsers(users);
            System.out.println("Registration successful!");
            System.out.println("User: " + userName + " Registered!");
            app.openLoginScene();
        });
        cancel.setOnAction(e -> app.openMenuScene());

        return new Scene(rootContainer, 1200, 794);
    }
}
