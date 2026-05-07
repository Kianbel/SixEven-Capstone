package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

public class ValueBlock extends Block {

    private TextField valueField;

    public ValueBlock() {
        super();

        HBox container = new HBox(5);
        valueField = new TextField("0");
        valueField.setPrefWidth(40);
        valueField.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #B5CEA8; -fx-font-family: 'Consolas';");

        Button deleteBtn = getDeleteBtn();

        container.getChildren().addAll(valueField, deleteBtn);
        getChildren().add(container);
    }

    private @NotNull Button getDeleteBtn() {
        Button deleteBtn = new Button("✕");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585; -fx-cursor: hand;");
        deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle("-fx-background-color: #e81123; -fx-text-fill: white;"));
        deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585;"));
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) {
                ((VBox) getParent()).getChildren().remove(this);
            }
        });
        return deleteBtn;
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