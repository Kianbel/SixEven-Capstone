package com.example.demo.gui;

import com.example.demo.classes.CodeSnippet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

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
        ScreenSwitcher.switchScreen("/com/example/demo/CodeRepository_view.fxml");
    }
}
