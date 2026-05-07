package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import com.example.demo.gui.dragdrop.BlockTransfer;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RightUpperBoundBlock extends Block {

    private DropZone iteratorZone;   // Expects VariableBlock
    private DropZone boundZone;       // Expects ANY expression (Variable, Value, BinaryOperation)
    private ComboBox<String> comparator;
    private BlockTransfer transfer;

    public RightUpperBoundBlock() {
        super();
        this.transfer = BlockTransfer.getInstance();

        VBox container = new VBox(5);
        container.setStyle("-fx-background-color: #2d2d30; -fx-border-radius: 5; -fx-padding: 5;");

        Label title = new Label("RIGHT UPPER BOUND");
        title.setStyle("-fx-text-fill: #4EC9B0; -fx-font-size: 10px; -fx-font-weight: bold;");

        HBox slots = new HBox(10);
        slots.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Iterator slot (left side)
        VBox iterSlot = new VBox(3);
        Label iterLabel = new Label("iterator");
        iterLabel.setStyle("-fx-text-fill: #858585; -fx-font-size: 9px;");
        iteratorZone = new DropZone("variable", "drop variable");
        iteratorZone.setMinWidth(80);
        iterSlot.getChildren().addAll(iterLabel, iteratorZone);

        // Comparator combo box
        comparator = new ComboBox<>();
        comparator.getItems().addAll("<", "<=");
        comparator.setValue("<");
        comparator.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #4EC9B0; " +
                "-fx-border-color: #3e3e42; -fx-border-radius: 3;");
        comparator.setPrefWidth(60);

        // Bound slot (right side) - accepts ANY expression
        VBox boundSlot = new VBox(3);
        Label boundLabel = new Label("upper bound (expression)");
        boundLabel.setStyle("-fx-text-fill: #858585; -fx-font-size: 9px;");
        boundZone = new DropZone("expression", "drop expression");
        boundZone.setMinWidth(100);
        boundSlot.getChildren().addAll(boundLabel, boundZone);

        slots.getChildren().addAll(iterSlot, comparator, boundSlot);

        // Delete button
        javafx.scene.control.Button deleteBtn = new javafx.scene.control.Button("✕");
        deleteBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 8px;");
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) {
                ((VBox) getParent()).getChildren().remove(this);
            }
        });

        HBox header = new HBox(10);
        header.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        header.getChildren().addAll(title, deleteBtn);
        HBox.setHgrow(title, javafx.scene.layout.Priority.ALWAYS);

        container.getChildren().addAll(header, slots);
        getChildren().add(container);
    }

    @Override
    public Expression buildExpression() {
        Block iterBlock = iteratorZone.getBlock();
        Block boundBlock = boundZone.getBlock();

        if (iterBlock == null) {
            throw new RuntimeException("RightUpperBound missing iterator variable");
        }
        if (boundBlock == null) {
            throw new RuntimeException("RightUpperBound missing bound expression");
        }

        // Get the iterator variable
        Variable iterator;
        try {
            iterator = (Variable) iterBlock.buildExpression();
        } catch (Exception e) {
            throw new RuntimeException("Iterator must be a Variable block, got: " + iterBlock.getClass().getSimpleName());
        }

        // Get the bound expression (can be Variable, Value, or BinaryOperation)
        Expression boundExpr = boundBlock.buildExpression();

        boolean isEqual = "<=".equals(comparator.getValue());

        return new RightUpperBound(iterator, boundExpr, isEqual);
    }

    @Override
    public Statement buildStatement() {
        throw new UnsupportedOperationException("Bound cannot be used as statement");
    }

    @Override
    public String getBlockDescription() {
        String iter = iteratorZone.getBlock() != null ? iteratorZone.getBlock().getBlockDescription() : "?";
        String bound = boundZone.getBlock() != null ? boundZone.getBlock().getBlockDescription() : "?";
        return iter + " " + comparator.getValue() + " " + bound;
    }

    @Override
    public boolean canDropInto(String slotType) {
        return "bound".equals(slotType);
    }

    @Override
    public Block cloneBlock() {
        RightUpperBoundBlock clone = new RightUpperBoundBlock();
        clone.comparator.setValue(this.comparator.getValue());
        return clone;
    }
}