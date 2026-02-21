package com.example.graph.profile;

import com.example.graph.auth.FileDatabase;
import com.example.graph.auth.User;
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
public class Profile {
    GraphingApp app;
    User user;
    private Map<String, TextInputControl> inputMap = new HashMap<>();
    public Profile(GraphingApp app)
    {
        this.app=app;
    }
    public Profile(User user,GraphingApp app) {
        this.app = app;
        this.user = user;
    }

    public Scene createProfileScene()
    {
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
            } else {
                inputField = new TextField();
                if (user != null) {
                    if (labelTexts[i].equals("User Name:")) inputField.setText(user.getUsername());
                    else if (labelTexts[i].equals("Email:")) inputField.setText(user.getEmail());
                    else if (labelTexts[i].equals("Phone:")) inputField.setText(user.getPhone());
                }
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
        Button save = new Button("Save");
        Button logout = new Button("Log Out");

        // Styling Buttons
        String btnBase = "-fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;";
        save.setStyle(btnBase + "-fx-background-color: #2ecc71; -fx-text-fill: white;");
        logout.setStyle(btnBase + "-fx-background-color: #e74c3c; -fx-text-fill: white;");

        save.setMaxWidth(Double.MAX_VALUE);
        logout.setMaxWidth(Double.MAX_VALUE);

        HBox buttonBox = new HBox(15, save, logout);
        buttonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(save, Priority.ALWAYS);
        HBox.setHgrow(logout, Priority.ALWAYS);

        // Assemble
        formCard.getChildren().addAll(heading, grid, buttonBox);
        rootContainer.getChildren().add(formCard);

        // Logic for navigation
        save.setOnAction(e -> {
            // 3. This is how you get the text now!
            String userName = inputMap.get("User Name:").getText();
            List<User> users = FileDatabase.loadUsers();
            boolean updated = false;
            if(!userName.equals(user.getUsername()))
            {
                for (User user : users) {
                    if (user.getUsername().equals(userName)) {
                        System.out.println("Username already exists!");
                        return;
                    }
                }
            }

            String password = inputMap.get("Password:").getText();
            String email = inputMap.get("Email:").getText();
            String phone = inputMap.get("Phone:").getText();
            for (User u : users) {
                if (u.getId() == user.getId()) { // ID দিয়ে বর্তমান ইউজারকে খুঁজে বের করা
                    u.setUsername(userName);
                    u.setEmail(email);
                    u.setPhone(phone);

                    // যদি পাসওয়ার্ড ফিল্ড খালি না থাকে, তবেই আপডেট হবে
                    if (!password.isEmpty()) {
                        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                        u.setPassword(hashedPassword);
                    }
                    updated = true;
                    break;
                }
            }

            if (updated) {
                FileDatabase.saveUsers(users);
                System.out.println("Profile Updated Successfully!");
                app.openGraphScene(); // আপডেট শেষে গ্রাফ পেজে ফিরে যাওয়া
            }

        });
        logout.setOnAction(e -> app.openMenuScene());

        return new Scene(rootContainer, 1200, 794);
    }
}