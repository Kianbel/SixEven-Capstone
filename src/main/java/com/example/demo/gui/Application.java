package com.example.demo.gui;

import com.example.demo.classes.User;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        ScreenSwitcher.initialize(stage);

        User user = deserialize();

        if(user != null) {
            ScreenSwitcher.switchScreen("/com/example/demo/EditorView.fxml");
        }
        else {
            ScreenSwitcher.switchScreen("/com/example/demo/LoginView.fxml");
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
