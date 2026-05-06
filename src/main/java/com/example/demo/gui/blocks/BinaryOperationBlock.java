package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BinaryOperationBlock extends Block {

    private DropZone leftZone;
    private DropZone rightZone;
    private ComboBox<String> operatorBox;

    public BinaryOperationBlock() {
        super();

        VBox container = new VBox(5);

        Label title = new Label("BINARY OPERATION");
        title.setStyle("-fx-text-fill: #CE9178; -fx-font-size: 10px;");

        HBox slots = new HBox(5);
        slots.setAlignment(javafx.geometry.Pos.CENTER);

        leftZone = new DropZone("expression", "left");
        leftZone.setMinWidth(80);

        operatorBox = new ComboBox<>();
        operatorBox.getItems().addAll("+", "-", "*", "/");
        operatorBox.setValue("+");
        operatorBox.setPrefWidth(60);
        operatorBox.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #4EC9B0;");

        rightZone = new DropZone("expression", "right");
        rightZone.setMinWidth(80);

        javafx.scene.control.Button deleteBtn = new javafx.scene.control.Button("✕");
        deleteBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 8px;");
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) {
                ((VBox) getParent()).getChildren().remove(this);
            }
        });

        slots.getChildren().addAll(leftZone, operatorBox, rightZone);

        HBox header = new HBox(10);
        header.getChildren().addAll(title, deleteBtn);

        container.getChildren().addAll(header, slots);
        getChildren().add(container);
    }

    @Override
    public Expression buildExpression() {
        Block leftBlock = leftZone.getBlock();
        Block rightBlock = rightZone.getBlock();

        if (leftBlock == null || rightBlock == null) {
            throw new RuntimeException("Binary operation missing left or right side");
        }

        Expression left = leftBlock.buildExpression();
        Expression right = rightBlock.buildExpression();
        String op = operatorBox.getValue();

        // For now, only + is fully supported by your backend
        if (!"+".equals(op)) {
            System.out.println("Warning: Only + operator is fully supported for complexity analysis");
        }

        return new Binary(new ArithmeticOperator(op), left, right);
    }

    @Override
    public Statement buildStatement() {
        throw new UnsupportedOperationException("Binary operation cannot be used as statement");
    }

    @Override
    public String getBlockDescription() {
        return "(" + leftZone.getBlock() + " " + operatorBox.getValue() + " " + rightZone.getBlock() + ")";
    }

    @Override
    public boolean canDropInto(String slotType) {
        return "expression".equals(slotType);
    }

    @Override
    public Block cloneBlock() {
        BinaryOperationBlock clone = new BinaryOperationBlock();
        clone.operatorBox.setValue(this.operatorBox.getValue());
        return clone;
    }
}