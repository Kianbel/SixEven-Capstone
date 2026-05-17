package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ValueBlock extends Block {

    private TextField valueField;

    public ValueBlock() {
        super();

        setStyle("-fx-background-color: #202a1d; " +
                "-fx-border-color: #B5CEA8; " +
                "-fx-border-width: 1.5; " +
                "-fx-background-radius: 8; " +
                "-fx-border-radius: 8; " +
                "-fx-padding: 8; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");

        HBox container = new HBox(10);
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        valueField = new TextField("0");
        valueField.setPrefWidth(50);
        valueField.setStyle("-fx-background-color: #1e1e1e; " +
                "-fx-text-fill: #B5CEA8; " +
                "-fx-border-color: #3e3e42; " +
                "-fx-border-radius: 4; " +
                "-fx-font-family: 'Consolas'; " +
                "-fx-font-weight: bold;");

        deleteBtn = new javafx.scene.control.Button("✕");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585; -fx-font-size: 10px; -fx-cursor: hand;");

        deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle("-fx-background-color: #e81123; -fx-text-fill: white; -fx-background-radius: 4;"));
        deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585;"));

        deleteBtn.setOnAction(e -> {
            if (getParent() != null) {
                ((VBox) getParent()).getChildren().remove(this);
            }
        });
        setDeleteButtonVisible(false);

        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        container.getChildren().addAll(valueField, spacer, deleteBtn);
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