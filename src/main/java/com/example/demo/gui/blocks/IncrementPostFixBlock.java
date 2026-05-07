package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class IncrementPostFixBlock extends Block {

    private DropZone varZone;

    public IncrementPostFixBlock() {
        super();

        HBox container = new HBox(5);
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        varZone = new DropZone("variable", "variable");
        varZone.setMinWidth(60);

        Label incLabel = new Label("++");
        incLabel.setStyle("-fx-text-fill: #4EC9B0; -fx-font-weight: bold;");

        javafx.scene.control.Button deleteBtn = new javafx.scene.control.Button("✕");
        deleteBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 8px;");
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) {
                ((VBox) getParent()).getChildren().remove(this);
            }
        });

        container.getChildren().addAll(varZone, incLabel, deleteBtn);
        getChildren().add(container);
    }

    @Override
    public Statement buildStatement() {
        Block varBlock = varZone.getBlock();
        if (varBlock == null) {
            throw new RuntimeException("Increment missing variable");
        }
        Variable var = (Variable) varBlock.buildExpression();
        return new SingleLineStatement(new IncrementPostFix(var));
    }

    @Override
    public Expression buildExpression() {
        throw new UnsupportedOperationException("Increment cannot be used as expression");
    }

    @Override
    public String getBlockDescription() {
        return varZone.getBlock() + "++";
    }

    @Override
    public boolean canDropInto(String slotType) {
        return false; // Can only be dropped into body zones
    }

    @Override
    public Block cloneBlock() {
        return new IncrementPostFixBlock();
    }
}