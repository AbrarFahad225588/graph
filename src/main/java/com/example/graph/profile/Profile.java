package com.example.graph.profile;

import com.example.graph.auth.FileDatabase;
import com.example.graph.auth.User;
import com.example.graph.plot.GraphingApp;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.example.graph.auth.ValidatorUtils.*;
import static com.example.graph.auth.AuthService.authenticated;
public class Profile {
    GraphingApp app;
    User user;
    Label elabel;
    private Map<String, TextInputControl> inputMap = new HashMap<>();
    public Profile(User user,GraphingApp app) {
        this.app = app;
        this.user = user;
    }

    public Scene createProfileScene()
    {
        StackPane rootContainer = new StackPane();
        rootContainer.setStyle("-fx-background-color: #f4f4f4;");

        VBox formCard = new VBox(30);
        formCard.setPadding(new Insets(40));
        formCard.setMaxWidth(500); 
        formCard.setMaxHeight(600);
        formCard.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        formCard.setAlignment(Pos.CENTER);

        Label heading = new Label("Profile");
        heading.setFont(Font.font("System", FontWeight.BOLD, 32));
        heading.setTextFill(Color.web("#2c3e50"));
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(35);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(65);
        grid.getColumnConstraints().addAll(col1, col2);
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
                Button toggleBtn = new Button("👁");
                toggleBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                StackPane.setAlignment(toggleBtn, Pos.CENTER_RIGHT);

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
                pf.setStyle("-fx-background-radius: 5; -fx-padding: 8 30 8 8; -fx-border-color: #dcdde1; -fx-border-radius: 5;");
                tfVisible.setStyle(pf.getStyle());
                passwordContainer.getChildren().addAll(pf, tfVisible, toggleBtn);
                inputMap.put(labelTexts[i], pf);

                grid.add(label, 0, i);
                grid.add(passwordContainer, 1, i);
                continue;
            } else {
                inputField = new TextField();
                if (user != null) {
                    if (labelTexts[i].equals("User Name:")) inputField.setText(user.getUsername());
                    else if (labelTexts[i].equals("Email:")) inputField.setText(user.getEmail());
                    else if (labelTexts[i].equals("Phone:")) inputField.setText(user.getPhone());
                }
            }
            inputField.setStyle("-fx-background-radius: 5; -fx-padding: 8; -fx-border-color: #dcdde1; -fx-border-radius: 5;");
            inputField.setMaxWidth(Double.MAX_VALUE); 
            inputMap.put(labelTexts[i], inputField);
            grid.add(label, 0, i);
            grid.add(inputField, 1, i);
        }
        Button save = new Button("Save");
        save.setDefaultButton(true);
        Button delete = new Button("Delete");
        Button backGraph=new Button("Graph");
        Button backHome=new Button("Home");
       elabel=new Label();
        elabel.setStyle("-fx-text-fill: red;");
        String btnBase = "-fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;";
        save.setStyle(btnBase + "-fx-background-color: #2ecc71; -fx-text-fill: white;");
        delete.setStyle(btnBase + "-fx-background-color: #e74c3c; -fx-text-fill: white;");

        save.setMaxWidth(Double.MAX_VALUE);
        delete.setMaxWidth(Double.MAX_VALUE);

        HBox buttonBox = new HBox(15, save, delete);
        buttonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(save, Priority.ALWAYS);
        HBox.setHgrow(delete, Priority.ALWAYS);

        backGraph.setStyle(btnBase + "-fx-background-color: #000099; -fx-text-fill: white;");
        backHome.setStyle(btnBase + "-fx-background-color: #000099; -fx-text-fill: white;");

        backGraph.setMaxWidth(150);
        backHome.setMaxWidth(150);

        HBox buttonBox2 = new HBox(10, backGraph, backHome);
        buttonBox2.setAlignment(Pos.CENTER);
        HBox.setHgrow(backGraph, Priority.ALWAYS);
        HBox.setHgrow(backHome, Priority.ALWAYS);
        VBox allBtn=new VBox(10,buttonBox,buttonBox2,elabel);
        allBtn.setAlignment(Pos.CENTER);

        formCard.getChildren().addAll(heading, grid, allBtn);
        rootContainer.getChildren().add(formCard);

        save.setOnAction(e -> {
          
            String userName = inputMap.get("User Name:").getText();
            List<User> users = FileDatabase.loadUsers();
            boolean updated = false;
            if(!userName.equals(user.getUsername()))
            {

                for (User user : users) {
                    if (user.getUsername().equals(userName)) {
//                        System.out.println("Username already exists!");
                        elabel.setText("Username already exists!");
                        return;
                    }
                }
            }

            String password = inputMap.get("Password:").getText();
            String email = inputMap.get("Email:").getText();
            String phone = inputMap.get("Phone:").getText();
            for (User u : users) {
                if (u.getId() == user.getId()) {
                    if(userName.isEmpty())
                    {
                        elabel.setText("Username Required !");
                        return;
                    }else
                    {
                        u.setUsername(userName);
                    }
                    if(!email.equals(user.getEmail()) && !isValidEmailFormat(email))
                    {
                        elabel.setText("Not valid Email Format !");
                        return;
                    }else
                    {
                        u.setEmail(email);
                    }
                    if(!phone.equals(user.getEmail()) && !isValidPhoneNumber(phone))
                    {
                        elabel.setText("Not valid Phone Number!");
                        return;
                    }else
                    {
                        u.setPhone(phone);
                    }



                    if (!password.isEmpty() ) {
                        if(isValidPassword(password))
                        {
                            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                            u.setPassword(hashedPassword);
                        }
                        else {
                            elabel.setText("Password must be 8+ chars!");
                            return;
                        }
                    }
                    user=u;
                    authenticated=u;
                    updated = true;
                    break;
                }
            }

            if (updated) {
                FileDatabase.saveUsers(users);
               elabel.setText("Profile Updated Successfully!");
               elabel.setTextFill(Color.GREEN);
               elabel.setStyle("-fx-font-weight: bold;"); // Optional: make it stand out

               PauseTransition delay = new PauseTransition(Duration.seconds(5));
               delay.setOnFinished(event ->
               {
                   elabel.setText("");
                   elabel.setTextFill(Color.RED);
               });
               delay.play();
            }

        });

        delete.setOnAction(e -> {
           
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Account");
            alert.setHeaderText("Are you sure you want to delete your account?");
            alert.setContentText("Delete Your Account Permanently!");
            if (alert.showAndWait().get() == ButtonType.OK) {
             
                List<User> users = FileDatabase.loadUsers();
                boolean removed = users.removeIf(u -> u.getId() == user.getId());
                if (removed) {
                    FileDatabase.saveUsers(users);
                    app.openLoginScene();
                    System.out.println("Account deleted successfully.");
                }
            }
        });
        backHome.setOnAction(e->{app.openHomeScene();});
        backGraph.setOnAction(e->{app.openGraphScene();});
        return new Scene(rootContainer, 1200, 794);
    }
}