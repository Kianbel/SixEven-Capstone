package com.example.demo.gui;

import com.example.demo.classes.User;
import com.example.demo.database.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

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
            return;
        }
        User authenticatedUser = DatabaseHandler.authenticateUser(username, password);
        if(authenticatedUser == null) {
            error.setText("Invalid credentials");
            return;
        }

        saveSession(authenticatedUser);
        error.setText("");

        navigateToEditor();
    }

    @FXML private void onRegisterHyperlinkClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/register_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) textfieldUsername.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Register Page");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void navigateToEditor() {
        System.out.println("navigated to editor");
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/editor_view.fxml"));
//            Parent root = loader.load();
//            Stage stage = (Stage) textfieldUsername.getScene().getWindow();
//            stage.getScene().setRoot(root);
//            stage.setTitle("Editor Page");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
