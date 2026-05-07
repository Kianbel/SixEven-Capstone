package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

public class PlusExpressionBlock extends Block {

    private DropZone leftZone;
    private DropZone rightZone;

    public PlusExpressionBlock() {
        super();
        // Accent: Magenta/Purple
        setStyle(getStyle() + "-fx-background-color: #352a3d; -fx-border-color: #c586c0;");

        Label title = new Label("ADDITION");
        title.setStyle("-fx-text-fill: #c586c0; -fx-font-size: 10px; -fx-font-weight: bold;");

        HBox slots = new HBox(8);
        slots.setAlignment(javafx.geometry.Pos.CENTER);

        Label plus = new Label("+");
        plus.setStyle("-fx-text-fill: #c586c0; -fx-font-weight: bold; -fx-font-size: 16px;");

        slots.getChildren().addAll(
                leftZone = new DropZone("expression", "left"),
                plus,
                rightZone = new DropZone("expression", "right")
        );

        javafx.scene.control.Button deleteBtn = createDeleteButton();
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) ((VBox) getParent()).getChildren().remove(this);
        });

        HBox header = new HBox(10);
        header.getChildren().addAll(title, new Region(), deleteBtn);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);

        getChildren().addAll(header, slots);
    }

    private javafx.scene.control.Button createDeleteButton() {
        javafx.scene.control.Button btn = new javafx.scene.control.Button("✕");
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585; -fx-font-size: 10px; -fx-cursor: hand;");

        // Hover effects
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #e81123; -fx-text-fill: white; -fx-font-size: 10px;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585; -fx-font-size: 10px;"));

        return btn;
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