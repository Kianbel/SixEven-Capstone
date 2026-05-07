package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

public class BinaryOperationBlock extends Block {

    private DropZone leftZone;
    private DropZone rightZone;
    private ComboBox<String> operatorBox;

    public BinaryOperationBlock() {
        super();
        // Accent: Magenta/Purple
        setStyle(getStyle() + "-fx-background-color: #352a3d; -fx-border-color: #c586c0;");

        Label title = new Label("MATH OPERATION");
        title.setStyle("-fx-text-fill: #c586c0; -fx-font-size: 12px; -fx-font-weight: bold;");

        HBox slots = new HBox(8);
        slots.setAlignment(Pos.CENTER);

        leftZone = new DropZone("expression", "left");
        rightZone = new DropZone("expression", "right");

        operatorBox = new ComboBox<>();
        operatorBox.getItems().addAll("+", "-", "*", "/");
        operatorBox.setValue("+");

        operatorBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white;");
                }
            }
        });

        operatorBox.setPrefWidth(70);

        operatorBox.setStyle("-fx-background-color: #1e1e1e; " +
                "-fx-border-color: #3e3e42; " +
                "-fx-border-radius: 3;");

        slots.getChildren().addAll(leftZone, operatorBox, rightZone);

        javafx.scene.control.Button deleteBtn = createDeleteButton();
        deleteBtn.setOnAction(e -> { if (getParent() != null) ((VBox) getParent()).getChildren().remove(this); });

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