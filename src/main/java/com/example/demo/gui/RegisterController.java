package com.example.demo.gui;

import com.example.demo.classes.User;
import com.example.demo.database.DatabaseHandler;
import javafx.concurrent.Task;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.*;

public class RegisterController {
    @FXML private Button buttonSignup;
    @FXML private Hyperlink hyperlinkSignin;
    @FXML private TextField textfieldUsername;
    @FXML private TextField textfieldPassword;
    @FXML private TextField textfieldConfirmPassword;
    @FXML private TextField textfieldFirstName;
    @FXML private TextField textfieldLastName;
    @FXML private Label error;

    @FXML private void initialize() {
        error.setText("");
    }

    @FXML
    private void onSignUpButtonClicked() {
        String username = textfieldUsername.getText();
        String password = textfieldPassword.getText();
        String confirmPassword = textfieldConfirmPassword.getText();
        String firstname = textfieldFirstName.getText();
        String lastname = textfieldLastName.getText();

        if(firstname.isBlank() || lastname.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            showError("Fields cannot be empty");
            return;
        }
        if(!confirmPassword.equals(password)) {
            showError("Passwords do not match");
            return;
        }

        showSuccess("Registering user...");

        buttonSignup.setDisable(true);
        hyperlinkSignin.setDisable(true);

        Task<User> registerTask = new Task<>() {
            @Override
            protected User call() throws Exception {
                boolean success = DatabaseHandler.registerUser(username, password, firstname, lastname);
                if(!success) return null;

                // Auto-login after registration
                return DatabaseHandler.authenticateUser(username, password);
            }
        };

        registerTask.setOnSucceeded(e -> {
            User user = registerTask.getValue();
            if(user != null) {
                showSuccess("Glad to have you, "+user.getFirstname()+"!");
//                saveSession(user);
//                navigateToEditor();
                navigateToLogin();
            } else {
                showError("Registration failed: Username may exist.");
                buttonSignup.setDisable(false);
                hyperlinkSignin.setDisable(false);
            }
        });

        new Thread(registerTask).start();
    }

    private void showError(String message){
        error.setTextFill(Color.RED);
        error.setText(message);
    }

    private void showSuccess(String message){
        error.setTextFill(Color.PALEGREEN);
        error.setText(message);
    }

    @FXML private void onSignInHyperlinkClicked() {
        ScreenSwitcher.switchScreen("/com/example/demo/LoginView.fxml");
    }

    private void navigateToEditor() {
        System.out.println("navigated to editor");
        ScreenSwitcher.switchScreen("/com/example/demo/EditorView.fxml");
    }

    private void navigateToLogin() {
        System.out.println("navigated to login");
        ScreenSwitcher.switchScreen("/com/example/demo/LoginView.fxml");

    }

    private void saveSession(User user) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.ser"))) {
            oos.writeObject(user);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
