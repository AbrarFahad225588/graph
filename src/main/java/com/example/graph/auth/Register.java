
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
import static com.example.graph.auth.ValidatorUtils.*;
public class Register {
    private GraphingApp app;
    Label elabel=new Label();

    private Map<String, TextInputControl> inputMap = new HashMap<>();
    public Register(GraphingApp app) {
        this.app = app;
    }


    public Scene createRegisterScene() {

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

        Label heading = new Label("Create Account");
        heading.setFont(Font.font("System", FontWeight.BOLD, 32));
        heading.setTextFill(Color.web("#2c3e50"));
        elabel.setStyle("-fx-text-fill: red;");
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
//                inputField = new PasswordField();
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
            }
            inputField.setStyle("-fx-background-radius: 5; -fx-padding: 8; -fx-border-color: #dcdde1; -fx-border-radius: 5;");
            inputField.setMaxWidth(Double.MAX_VALUE); // Allows field to grow
            inputMap.put(labelTexts[i], inputField);
            grid.add(label, 0, i);
            grid.add(inputField, 1, i);
        }
        Button register = new Button("Register");
        register.setDefaultButton(true);
        Button cancel = new Button("Cancel");
        String btnBase = "-fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;";
        register.setStyle(btnBase + "-fx-background-color: #2ecc71; -fx-text-fill: white;");
        cancel.setStyle(btnBase + "-fx-background-color: #e74c3c; -fx-text-fill: white;");

        register.setMaxWidth(Double.MAX_VALUE);
        cancel.setMaxWidth(Double.MAX_VALUE);

        HBox buttonBox = new HBox(15, cancel, register);
        buttonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(register, Priority.ALWAYS);
        HBox.setHgrow(cancel, Priority.ALWAYS);


        formCard.getChildren().addAll(heading, grid, buttonBox,elabel);
        rootContainer.getChildren().add(formCard);
                register.setOnAction(e -> {

            String userName = inputMap.get("User Name:").getText();
            List<User> users = FileDatabase.loadUsers();
                   if(!userName.isEmpty())
                   {
                       for (User user : users) {
                           if (user.getUsername().equals(userName)) {
                               elabel.setText("Username already exists!");
                               return;
                           }
                       }
                   }else {
                       elabel.setText("Required Fill all Field");
                       return;
                   }


                   String password = inputMap.get("Password:").getText();
                   if(!password.isEmpty())
                   {
                       if (!isValidPassword(password)) {
                           elabel.setText("Password must be 4+ chars !");
                           return;
                       }
                   }else
                   {
                       elabel.setText("Required Fill all Field");
                       return;
                   }
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                    int newId = users.size() + 1;

            String email = inputMap.get("Email:").getText();
            if (!email.isEmpty())
            {
               if(!isValidEmailFormat(email))
               {
                   elabel.setText("Invalid email format.");
                   return;
               }
            }else
            {
                elabel.setText("Required Fill all Field");
                return;
            }
            String phone = inputMap.get("Phone:").getText();
                    if (!phone.isEmpty())
                    {
                      if(!isValidPhoneNumber(phone))
                      {
                          elabel.setText("Invalid Phone Number.");
                          return;
                      }
                    }else {
                        elabel.setText("Required Fill all Field");
                    }
            User newUser = new User(newId, userName, hashedPassword,email,phone);
            users.add(newUser);
            FileDatabase.saveUsers(users);
            app.openLoginScene();
        });
        cancel.setOnAction(e -> app.openHomeScene());
        return new Scene(rootContainer, 1200, 794);
    }
}
