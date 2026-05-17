package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class IncrementPostFixBlock extends Block {

    private DropZone varZone;

    public IncrementPostFixBlock() {
        super();

        setStyle("-fx-background-color: #1e2b2a; " +
                "-fx-border-color: #4EC9B0; " +
                "-fx-border-width: 1.5; " +
                "-fx-background-radius: 8; " +
                "-fx-border-radius: 8; " +
                "-fx-padding: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");

        HBox container = new HBox(10); // Increased to 10 for consistency with Declaration
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);


        varZone = new DropZone("variable", "variable");
        varZone.setMinWidth(60);

        Label incLabel = new Label("++");

        incLabel.setStyle("-fx-text-fill: #4EC9B0; -fx-font-weight: bold; -fx-font-family: 'Consolas'; -fx-font-size: 16px;");


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

        container.getChildren().addAll(varZone, incLabel, spacer, deleteBtn);
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