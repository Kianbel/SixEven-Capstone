package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class InitializationBlock extends Block {

    private DropZone varZone;
    private DropZone valueZone;

    public InitializationBlock() {
        super();

        HBox container = new HBox(5);
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        varZone = new DropZone("variable", "var");
        varZone.setMinWidth(80);

        Label eqLabel = new Label("=");
        eqLabel.setStyle("-fx-text-fill: #4EC9B0; -fx-font-weight: bold;");

        valueZone = new DropZone("expression", "value");
        valueZone.setMinWidth(80);

        javafx.scene.control.Button deleteBtn = new javafx.scene.control.Button("✕");
        deleteBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 8px;");
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) {
                ((VBox) getParent()).getChildren().remove(this);
            }
        });

        container.getChildren().addAll(varZone, eqLabel, valueZone, deleteBtn);
        getChildren().add(container);
    }

    @Override
    public Statement buildStatement() {
        Block varBlock = varZone.getBlock();
        Block valBlock = valueZone.getBlock();

        if (varBlock == null || valBlock == null) {
            throw new RuntimeException("Initialization missing variable or value");
        }

        Variable var = (Variable) varBlock.buildExpression();
        Expression val = valBlock.buildExpression();

        return new Initialization(var, val);
    }

    @Override
    public Expression buildExpression() {
        throw new UnsupportedOperationException("Initialization cannot be used as expression");
    }

    @Override
    public String getBlockDescription() {
        return "Initialize variable";
    }

    @Override
    public boolean canDropInto(String slotType) {
        return "initialization".equals(slotType);
    }

    @Override
    public Block cloneBlock() {
        return new InitializationBlock();
    }
}