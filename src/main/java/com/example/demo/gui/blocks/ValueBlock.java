package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ValueBlock extends Block {

    private TextField valueField;

    public ValueBlock() {
        super();

        HBox container = new HBox(5);
        valueField = new TextField("0");
        valueField.setPrefWidth(40);
        valueField.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #B5CEA8; -fx-font-family: 'Consolas';");

        javafx.scene.control.Button deleteBtn = new javafx.scene.control.Button("✕");
        deleteBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 8px;");
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) {
                ((VBox) getParent()).getChildren().remove(this);
            }
        });

        container.getChildren().addAll(valueField, deleteBtn);
        getChildren().add(container);
    }

    @Override
    public Expression buildExpression() {
        try {
            return new Value<>(Integer.parseInt(valueField.getText()));
        } catch (NumberFormatException e) {
            return new Value<>(0);
        }
    }

    @Override
    public Statement buildStatement() {
        throw new UnsupportedOperationException("Value alone is not a statement");
    }

    @Override
    public String getBlockDescription() {
        return "Value: " + valueField.getText();
    }

    @Override
    public boolean canDropInto(String slotType) {
        return "expression".equals(slotType);
    }

    @Override
    public Block cloneBlock() {
        ValueBlock clone = new ValueBlock();
        clone.valueField.setText(this.valueField.getText());
        return clone;
    }
}