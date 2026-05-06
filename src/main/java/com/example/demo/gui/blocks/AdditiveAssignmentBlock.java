package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdditiveAssignmentBlock extends Block {

    private DropZone varZone;
    private DropZone stepZone;

    public AdditiveAssignmentBlock() {
        super();

        HBox container = new HBox(5);
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        varZone = new DropZone("variable", "var");
        varZone.setMinWidth(60);

        Label opLabel = new Label("+=");
        opLabel.setStyle("-fx-text-fill: #4EC9B0; -fx-font-weight: bold;");

        stepZone = new DropZone("expression", "step");
        stepZone.setMinWidth(60);

        javafx.scene.control.Button deleteBtn = new javafx.scene.control.Button("✕");
        deleteBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 8px;");
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) {
                ((VBox) getParent()).getChildren().remove(this);
            }
        });

        container.getChildren().addAll(varZone, opLabel, stepZone, deleteBtn);
        getChildren().add(container);
    }

    @Override
    public Statement buildStatement() {
        Block varBlock = varZone.getBlock();
        Block stepBlock = stepZone.getBlock();

        if (varBlock == null || stepBlock == null) {
            throw new RuntimeException("Assignment missing variable or step");
        }

        Variable var = (Variable) varBlock.buildExpression();
        Expression step = stepBlock.buildExpression();

        return new AdditiveAssignment(var, step);
    }

    @Override
    public Expression buildExpression() {
        throw new UnsupportedOperationException("Assignment cannot be used as expression");
    }

    @Override
    public String getBlockDescription() {
        return varZone.getBlock() + " += " + stepZone.getBlock();
    }

    @Override
    public boolean canDropInto(String slotType) {
        return "assignment".equals(slotType);
    }

    @Override
    public Block cloneBlock() {
        return new AdditiveAssignmentBlock();
    }
}