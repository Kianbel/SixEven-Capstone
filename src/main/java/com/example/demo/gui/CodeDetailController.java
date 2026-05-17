package com.example.demo.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class CodeDetailController {
    @FXML
    private TextArea codeDetailsTextArea;

    public void setText(String code){
        codeDetailsTextArea.setText(code);
    }

    @FXML
    private void onClick(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/CodeRepository_view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) codeDetailsTextArea.getScene().getWindow();
            stage.setScene(scene);
            stage.sizeToScene();
            stage.setTitle("Code Repository");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
