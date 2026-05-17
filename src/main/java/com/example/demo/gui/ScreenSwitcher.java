package com.example.demo.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ScreenSwitcher {
    private static Stage primaryStage;

    public static void initialize(Stage stage) {
        primaryStage = stage;
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(768);
    }

    public static void switchScreen(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenSwitcher.class.getResource(fxmlPath));
            Parent newRoot = loader.load();

            Scene currentScene = primaryStage.getScene();
            if (currentScene == null) {
                currentScene = new Scene(newRoot);
                primaryStage.setScene(currentScene);
            } else {
                currentScene.setRoot(newRoot);
            }
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}