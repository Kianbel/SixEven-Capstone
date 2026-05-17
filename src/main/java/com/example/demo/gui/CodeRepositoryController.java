package com.example.demo.gui;

import com.example.demo.classes.CodeSnippet;
import com.example.demo.database.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class CodeRepositoryController {
    @FXML private TextField textfieldSearch;
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


        // filter the data based on the search bar with dynamic updating
        FilteredList<CodeSnippet> filteredData = new FilteredList<>(rows, p -> true);
        textfieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(snippet -> {
                if(newValue == null || newValue.isEmpty()) return true;

                String lowerCaseFilter = newValue.toLowerCase();

                return snippet.getTitle().toLowerCase().contains(lowerCaseFilter);
            });

            labelTotalSnippets.setText(filteredData.size() + " snippets");
        });

        // wrap in the filteredList around sortedList so users can still sort via columns
        SortedList<CodeSnippet> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableRepository.comparatorProperty());

        // set the filtered rows to the table
        tableRepository.setItems(sortedData);
        labelTotalSnippets.setText(rows.size() + " snippets");
    }

    @FXML
    private void onReturnToEditor() {
        System.out.println("navigated to editor");
        ScreenSwitcher.switchScreen("/com/example/demo/Editor_view.fxml");
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

        // cannot use ScreenSwitcher.switchScreen() because selectedSnippet needs to be passed.
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/CodeDetail_view.fxml"));
            Parent root = loader.load();

            CodeDetailController controller = loader.getController();
            controller.setDetails(selectedSnippet);

            Stage stage = (Stage) tableRepository.getScene().getWindow();
            Scene currentScene = stage.getScene();

            if (currentScene != null) {
                currentScene.setRoot(root);
            } else {
                currentScene = new Scene(root);
                stage.setScene(currentScene);
            }

            stage.setTitle("Code Details");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Redirecting to details for: " + selectedSnippet.getTitle());
    }
}