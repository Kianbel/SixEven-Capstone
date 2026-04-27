package com.example.demo.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField textfieldUsername;
    @FXML private TextField textfieldPassword;
    @FXML private Label error;

    @FXML private void initialize() {
        error.setText("");
    }

    @FXML private void onLogInButtonClicked() {
        String username = textfieldUsername.getText();
        String password = textfieldPassword.getText();

        if(username.isBlank() || password.isBlank()) {
            error.setText("Fields cannot be empty");
        }
        else {
            // TODO: save session (serialize)
            // TODO: authenticate user using database
            // TODO: if authenticated, navigate to editor page
        }
    }

    @FXML private void onRegisterHyperlinkClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/Register_view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) textfieldUsername.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Register Page");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
