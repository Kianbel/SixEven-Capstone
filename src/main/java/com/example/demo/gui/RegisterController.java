package com.example.demo.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML private TextField textfieldUsername;
    @FXML private TextField textfieldEmail;
    @FXML private TextField textfieldPassword;
    @FXML private TextField textfieldConfirmPassword;
    @FXML private Label error;

    @FXML private void initialize() {
        error.setText("");
    }

    @FXML private void onSignUpButtonClicked() {
        String username = textfieldUsername.getText();
        String email = textfieldEmail.getText();
        String password = textfieldPassword.getText();
        String confirmPassword = textfieldConfirmPassword.getText();

        if(username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            error.setText("Fields cannot be empty");
        }
        else {
            if(!password.equals(confirmPassword)) error.setText("Invalid credentials");
            else {
                // TODO: serialize user
                // TODO: save user to database
                // TODO: navigate to editor page
            }
        }
    }

    @FXML private void onSignInHyperlinkClicked() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/Login_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) textfieldUsername.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Login Page");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
