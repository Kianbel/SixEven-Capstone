package com.example.demo.gui.components;

import com.example.demo.classes.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class ExpressionBuilder extends HBox {

    private Expression currentExpression;
    private Runnable onChange;
    private List<ExpressionBuilder> children = new ArrayList<>();

    public ExpressionBuilder() {
        setSpacing(5);
        setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        setStyle("-fx-background-color: #1e1e1e; -fx-border-color: #555; -fx-border-radius: 3; -fx-padding: 5;");

        // Start with a single variable
        rebuildWithSingle();
    }

    private void rebuildWithSingle() {
        getChildren().clear();
        children.clear();

        ComboBox<String> typeSelector = new ComboBox<>();
        typeSelector.getItems().addAll("Variable", "Constant");
        typeSelector.setValue("Variable");

        TextField varField = new TextField("n");
        varField.setPrefWidth(60);

        Button addButton = new Button("+");
        addButton.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> addTerm());

        typeSelector.setOnAction(e -> {
            if ("Constant".equals(typeSelector.getValue())) {
                varField.setText("0");
            } else {
                varField.setText("n");
            }
            updateExpression(typeSelector, varField);
        });

        varField.textProperty().addListener((obs, old, val) -> updateExpression(typeSelector, varField));

        getChildren().addAll(typeSelector, varField, addButton);

        updateExpression(typeSelector, varField);
    }

    private void addTerm() {
        ExpressionBuilder newTerm = new ExpressionBuilder();
        newTerm.setOnExpressionChanged(() -> rebuildExpression());
        children.add(newTerm);

        // Add plus sign and new term
        Label plus = new Label("+");
        plus.setStyle("-fx-text-fill: #4EC9B0; -fx-font-weight: bold;");
        int insertIndex = getChildren().size() - 1; // Before the add button
        getChildren().add(insertIndex, plus);
        getChildren().add(insertIndex, newTerm);

        rebuildExpression();
    }

    private void updateExpression(ComboBox<String> typeSelector, TextField field) {
        if ("Constant".equals(typeSelector.getValue())) {
            try {
                currentExpression = new Value<>(Integer.parseInt(field.getText()));
            } catch (NumberFormatException e) {
                currentExpression = new Value<>(0);
            }
        } else {
            currentExpression = new Variable(field.getText());
        }

        rebuildExpression();
    }

    private void rebuildExpression() {
        Expression total = currentExpression;

        // Combine with all child terms using +
        for (ExpressionBuilder child : children) {
            if (total == null) {
                total = child.getExpression();
            } else if (child.getExpression() != null) {
                total = new Binary(new Plus(), total, child.getExpression());
            }
        }

        currentExpression = total;
        if (onChange != null) onChange.run();
    }

    public Expression getExpression() {
        return currentExpression != null ? currentExpression : new Value<>(0);
    }

    public void setOnExpressionChanged(Runnable onChange) {
        this.onChange = onChange;
    }
}