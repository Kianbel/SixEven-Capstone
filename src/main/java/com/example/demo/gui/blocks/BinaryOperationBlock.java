package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class BinaryOperationBlock extends Block {

    private DropZone leftZone;
    private DropZone rightZone;
    private ComboBox<String> operatorBox;

    public BinaryOperationBlock() {
        super();

        setStyle("-fx-background-color: #25202b; " +
                "-fx-border-color: #C678DD; " +
                "-fx-border-width: 1.5; " +
                "-fx-background-radius: 8; " +
                "-fx-border-radius: 8; " +
                "-fx-padding: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");

        VBox container = new VBox(8); // Slightly increased spacing for better breathing room
        container.setStyle("-fx-background-color: transparent;");

        Label title = new Label("BINARY OPERATION");
        title.setStyle("-fx-text-fill: #C678DD; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: 'Consolas';");

        HBox slots = new HBox(10);
        slots.setAlignment(Pos.CENTER_LEFT);

        leftZone = new DropZone("expression", "left");
        leftZone.setMinWidth(80);

        operatorBox = new ComboBox<>();
        operatorBox.getItems().addAll("+", "-", "*", "/");
        operatorBox.setValue("+");
        operatorBox.setPrefWidth(65);

        operatorBox.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white; " +
                "-fx-border-color: #444; -fx-border-radius: 4; -fx-font-family: 'Consolas';");

        operatorBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-background-color: transparent;");
                }
            }
        });

        // Functional Code (Untouched)
        rightZone = new DropZone("expression", "right");
        rightZone.setMinWidth(80);

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

        slots.getChildren().addAll(leftZone, operatorBox, rightZone);

        HBox header = new HBox(10);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(title, spacer, deleteBtn);

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