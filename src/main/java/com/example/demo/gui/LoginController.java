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
        ScreenSwitcher.switchScreen("/com/example/demo/RegisterView.fxml");
    }

    private void navigateToEditor() {
        System.out.println("navigated to editor");
        ScreenSwitcher.switchScreen("/com/example/demo/EditorView.fxml");
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
