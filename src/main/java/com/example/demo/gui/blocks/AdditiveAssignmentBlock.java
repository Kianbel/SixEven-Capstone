package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AdditiveAssignmentBlock extends Block {

    private DropZone varZone;
    private DropZone stepZone;

    public AdditiveAssignmentBlock() {
        super();

        setStyle("-fx-background-color: #1e2b2a; " +
                "-fx-border-color: #4EC9B0; " +
                "-fx-border-width: 1.5; " +
                "-fx-background-radius: 8; " +
                "-fx-border-radius: 8; " +
                "-fx-padding: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");

        HBox container = new HBox(10); // Consistent spacing with your DeclarationBlock
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        varZone = new DropZone("variable", "var");
        varZone.setMinWidth(60);

        Label opLabel = new Label("+=");
        opLabel.setStyle("-fx-text-fill: #4EC9B0; -fx-font-weight: bold; -fx-font-family: 'Consolas'; -fx-font-size: 14px;");

        stepZone = new DropZone("expression", "step");
        stepZone.setMinWidth(60);

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

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        container.getChildren().addAll(varZone, opLabel, stepZone, spacer, deleteBtn);
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