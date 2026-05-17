package com.example.demo.gui;

import com.example.demo.classes.CodeSnippet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class CodeDetailController {
    @FXML private TextArea codeDetailsTextArea;
    @FXML private Label titleLabel;
    @FXML private Label languageLabel;
    @FXML private Label dateLabel;
    @FXML private Label createdByLabel;

    public void setDetails(CodeSnippet code){
        titleLabel.setText(code.getTitle());
        languageLabel.setText(code.getLanguage());
        dateLabel.setText(code.getDateCreated());
        createdByLabel.setText(code.getCreatedBy());
        codeDetailsTextArea.setText(code.getCode());
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
