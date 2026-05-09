package com.example.demo.gui;

import com.example.demo.classes.User;
import com.example.demo.database.DatabaseHandler;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

public class LoginController {
    @FXML private Button buttonLogin;
    @FXML private Hyperlink hyperlinkRegister;
    @FXML private TextField textfieldUsername;
    @FXML private TextField textfieldPassword;
    @FXML private Label error;

    @FXML private void initialize() {
        error.setText("");
    }

    @FXML
    private void onLogInButtonClicked() {
        String username = textfieldUsername.getText();
        String password = textfieldPassword.getText();

        if(username.isBlank() || password.isBlank()) {
            showError("Fields cannot be empty");
            return;
        }

        showSuccess("Logging in user...");

        buttonLogin.setDisable(true);
        hyperlinkRegister.setDisable(true);

        Task<User> loginTask = new Task<>() {
            @Override
            protected User call() throws Exception {
                return DatabaseHandler.authenticateUser(username, password);
            }
        };

        loginTask.setOnSucceeded(e -> {
            User user = loginTask.getValue();
            if(user != null) {
                showSuccess("Welcome back, "+user.getFirstname()+"!");
                saveSession(user);
                navigateToEditor();
            } else {
                showError("Invalid credentials");
                buttonLogin.setDisable(false);
                hyperlinkRegister.setDisable(false);
            }
        });

        new Thread(loginTask).start();
    }

    private void showError(String message){
        error.setTextFill(Color.RED);
        error.setText(message);
    }

    private void showSuccess(String message){
        error.setTextFill(Color.PALEGREEN);
        error.setText(message);
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/modular-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) textfieldUsername.getScene().getWindow();
            stage.setScene(scene);
            stage.sizeToScene();
            stage.setTitle("Runtime Analyzer");

        } catch (IOException e) {
            e.printStackTrace();
        }
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
