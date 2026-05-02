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

public class RegisterController {
    @FXML private TextField textfieldUsername;
    @FXML private TextField textfieldPassword;
    @FXML private TextField textfieldConfirmPassword;
    @FXML private TextField textfieldFirstName;
    @FXML private TextField textfieldLastName;
    @FXML private Label error;

    @FXML private void initialize() {
        error.setText("");
    }

    @FXML private void onSignUpButtonClicked() {
        String username = textfieldUsername.getText();
        String password = textfieldPassword.getText();
        String confirmPassword = textfieldConfirmPassword.getText();
        String firstname = textfieldFirstName.getText();
        String lastname = textfieldLastName.getText();

        if(firstname.isBlank() || lastname.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            error.setText("Fields cannot be empty");
        }
        else {
            if(!confirmPassword.equals(password)) {
                error.setText("Password and confirm password are not equal");
                return;
            }

            boolean success = DatabaseHandler.registerUser(username, password, firstname, lastname);
            if(!success) {
                error.setText("Username already exists");
                return;
            }

            User authenticatedUser = DatabaseHandler.authenticateUser(username, password);
            if(authenticatedUser == null) {
                error.setText("Unable to register user");
                return;
            }

            saveSession(authenticatedUser);
            error.setText("");
            navigateToEditor();
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
