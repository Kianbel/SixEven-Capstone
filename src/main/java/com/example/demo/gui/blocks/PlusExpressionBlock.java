package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlusExpressionBlock extends Block {

    private DropZone leftZone;
    private DropZone rightZone;

    public PlusExpressionBlock() {
        super();

        VBox container = new VBox(5);

        Label title = new Label("+ EXPRESSION");
        title.setStyle("-fx-text-fill: #4EC9B0; -fx-font-size: 10px;");

        HBox slots = new HBox(5);
        slots.setAlignment(javafx.geometry.Pos.CENTER);

        leftZone = new DropZone("expression", "left");
        leftZone.setMinWidth(80);

        Label plus = new Label("+");
        plus.setStyle("-fx-text-fill: #4EC9B0; -fx-font-weight: bold; -fx-font-size: 16px;");

        rightZone = new DropZone("expression", "right");
        rightZone.setMinWidth(80);

        javafx.scene.control.Button deleteBtn = new javafx.scene.control.Button("✕");
        deleteBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 8px;");
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) {
                ((VBox) getParent()).getChildren().remove(this);
            }
        });

        slots.getChildren().addAll(leftZone, plus, rightZone);

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
            throw new RuntimeException("Plus expression missing left or right side");
        }

        Expression left = leftBlock.buildExpression();
        Expression right = rightBlock.buildExpression();

        return new Binary(new Plus(), left, right);
    }

    @Override
    public Statement buildStatement() {
        throw new UnsupportedOperationException("Expression cannot be used as statement");
    }

    @Override
    public String getBlockDescription() {
        return "(" + leftZone.getBlock() + " + " + rightZone.getBlock() + ")";
    }

    @Override
    public boolean canDropInto(String slotType) {
        return "expression".equals(slotType);
    }

    @Override
    public Block cloneBlock() {
        return new PlusExpressionBlock();
    }
}