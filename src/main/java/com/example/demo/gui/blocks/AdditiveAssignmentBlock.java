package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

public class AdditiveAssignmentBlock extends Block {

    private DropZone varZone;
    private DropZone stepZone;

    public AdditiveAssignmentBlock() {
        super();
        // Accent: Emerald Green
        setStyle(getStyle() + "-fx-background-color: #2a3d31; -fx-border-color: #4EC9B0;");

        HBox container = new HBox(8);
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label opLabel = new Label("+=");
        opLabel.setStyle("-fx-text-fill: #4EC9B0; -fx-font-weight: bold; -fx-font-size: 14px;");

        javafx.scene.control.Button deleteBtn = createDeleteButton();
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) ((VBox) getParent()).getChildren().remove(this);
        });

        // Uses the helper to label the zones for better UX
        container.getChildren().addAll(
                createLabeledSlot("variable", varZone = new DropZone("variable", "var")),
                opLabel,
                createLabeledSlot("step", stepZone = new DropZone("expression", "value")),
                deleteBtn
        );

        getChildren().add(container);
    }

    private javafx.scene.control.Button createDeleteButton() {
        javafx.scene.control.Button btn = new javafx.scene.control.Button("✕");
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585; -fx-font-size: 10px; -fx-cursor: hand;");

        // Hover effects
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #e81123; -fx-text-fill: white; -fx-font-size: 10px;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585; -fx-font-size: 10px;"));

        return btn;
    }

    // Cleaner slot labeling
    private VBox createLabeledSlot(String text, DropZone zone) {
        VBox slot = new VBox(3);
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #858585; -fx-font-size: 9px; -fx-font-weight: bold; -fx-text-transform: uppercase;");
        zone.setMinWidth(70);
        slot.getChildren().addAll(l, zone);
        return slot;
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