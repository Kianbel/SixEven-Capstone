package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class PlusExpressionBlock extends Block {

    private DropZone leftZone;
    private DropZone rightZone;

    public PlusExpressionBlock() {
        super();

        setStyle("-fx-background-color: #25202b; " +
                "-fx-border-color: #C678DD; " +
                "-fx-border-width: 1.5; " +
                "-fx-background-radius: 8; " +
                "-fx-border-radius: 8; " +
                "-fx-padding: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");

        VBox container = new VBox(8);
        container.setStyle("-fx-background-color: transparent;");

        Label title = new Label("+ EXPRESSION");
        title.setStyle("-fx-text-fill: #C678DD; -fx-font-size: 11px; -fx-font-weight: bold; -fx-font-family: 'Consolas';");

        HBox slots = new HBox(10);
        slots.setAlignment(javafx.geometry.Pos.CENTER);

        leftZone = new DropZone("expression", "left");
//        leftZone.setMinWidth(80);
//        leftZone.setFillWidth(true);

        Label plus = new Label("+");
        plus.setStyle("-fx-text-fill: #DCDCAA; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Consolas';");

       rightZone = new DropZone("expression", "right");
//        rightZone.setMinWidth(80);
//        rightZone.setFillWidth(true);

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

        slots.getChildren().addAll(leftZone, plus, rightZone);

        HBox header = new HBox(10);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        header.getChildren().addAll(title, spacer, deleteBtn);

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