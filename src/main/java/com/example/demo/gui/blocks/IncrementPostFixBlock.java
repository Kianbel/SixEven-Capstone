package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

public class IncrementPostFixBlock extends Block {

    private DropZone varZone;

    public IncrementPostFixBlock() {
        super();
        // Accent: Emerald Green
        setStyle(getStyle() + "-fx-background-color: #2a3d31; -fx-border-color: #4EC9B0;");

        HBox container = new HBox(8);
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label incLabel = new Label("++");
        incLabel.setStyle("-fx-text-fill: #4EC9B0; -fx-font-weight: bold; -fx-font-size: 16px;");

        javafx.scene.control.Button deleteBtn = createDeleteButton();
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) ((VBox) getParent()).getChildren().remove(this);
        });

        container.getChildren().addAll(
                createLabeledSlot("target", varZone = new DropZone("variable", "var")),
                incLabel,
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