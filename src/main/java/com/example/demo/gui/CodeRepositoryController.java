package com.example.demo.gui;

import com.example.demo.classes.CodeSnippet;
import com.example.demo.database.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class CodeRepositoryController {
    @FXML private TextField textfieldSearch;
    @FXML private Button buttonSearch;
    @FXML private Label labelTotalSnippets;
    @FXML private TableView<CodeSnippet> tableRepository;
    @FXML private TableColumn<CodeSnippet, String> colTitle;
    @FXML private TableColumn<CodeSnippet, String> colLanguage;
    @FXML private TableColumn<CodeSnippet, String> colCreatedBy;
    @FXML private TableColumn<CodeSnippet, String> colDateCreated;

    private final ObservableList<CodeSnippet> rows = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        setupTitleLink(); // make column title a clickable link
        colLanguage.setCellValueFactory(new PropertyValueFactory<>("language"));
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        colDateCreated.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));

        // load all data into the table
        ArrayList<CodeSnippet> snippets = DatabaseHandler.getAllCodeSnippets();
        if (snippets != null) {
            rows.addAll(snippets);
        }

        tableRepository.setItems(rows);
        labelTotalSnippets.setText(rows.size() + " snippets");
    }

    private void setupTitleLink() {
        colTitle.setCellFactory(column -> new TableCell<CodeSnippet, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #1a73e8; -fx-underline: true; -fx-cursor: hand;");
                }
            }

            {
                setOnMouseClicked(event -> {
                    if (!isEmpty()) {
                        CodeSnippet selectedSnippet = getTableView().getItems().get(getIndex());
                        navigateToCodeDetails(selectedSnippet);
                    }
                });
            }
        });
    }

    private void navigateToCodeDetails(CodeSnippet selectedSnippet) {
        // Redirect logic goes here.
        // Example: Pass 'selectedSnippet' to your next controller
        System.out.println("Redirecting to details for: " + selectedSnippet.getTitle());
    }
}