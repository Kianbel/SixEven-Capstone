package com.example.demo.gui;

import com.example.demo.classes.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Objects;


public class RuntimeAnalyzer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource("/com/example/demo/RuntimeAnalyzer_view.fxml")
        ));
        primaryStage.setScene(new Scene(root, 1400, 700));
        primaryStage.setTitle("Runtime Analyzer");
        primaryStage.show();
    }
}