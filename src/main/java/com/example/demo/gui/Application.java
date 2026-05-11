package com.example.demo.gui;

import com.example.demo.classes.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        User user = deserialize();
        if(user != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/example/demo/Editor_view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Runtime Analyzer");
            stage.setScene(scene);
            stage.show();
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/example/demo/Login_view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Login page");
            stage.setScene(scene);
            stage.show();
        }
    }

    public User deserialize() {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user.ser"))) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
