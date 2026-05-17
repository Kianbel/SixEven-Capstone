package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import com.example.demo.gui.dragdrop.BlockTransfer;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class RightUpperBoundBlock extends Block {

    private DropZone iteratorZone;   // Expects VariableBlock
    private DropZone boundZone;       // Expects ANY expression (Variable, Value, BinaryOperation)
    private ComboBox<String> comparator;
    private BlockTransfer transfer;

    public RightUpperBoundBlock() {
        super();
        this.transfer = BlockTransfer.getInstance();

        setStyle("-fx-background-color: #2b212b; " +
                "-fx-border-color: #C678DD; " +
                "-fx-border-width: 1.5; " +
                "-fx-background-radius: 8; " +
                "-fx-border-radius: 8; " +
                "-fx-padding: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");

        VBox container = new VBox(8);
        container.setStyle("-fx-background-color: transparent;");

        Label title = new Label("RIGHT UPPER BOUND");
        title.setStyle("-fx-text-fill: #C678DD; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: 'Consolas';");

        HBox slots = new HBox(12);
        slots.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        VBox iterSlot = new VBox(3);
        Label iterLabel = new Label("iterator");
        iterLabel.setStyle("-fx-text-fill: #858585; -fx-font-size: 9px; -fx-font-weight: bold;");
        iteratorZone = new DropZone("variable", "drop variable");
//        iteratorZone.setMinWidth(80);
        iterSlot.getChildren().addAll(iterLabel, iteratorZone);

        comparator = new ComboBox<>();
        comparator.getItems().addAll("<", "<=");
        comparator.setValue("<");
        comparator.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white; " +
                "-fx-border-color: #444; -fx-border-radius: 3; -fx-font-family: 'Consolas';");
        comparator.setButtonCell(new ListCell<String>() {
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

        comparator.setPrefWidth(65);

        VBox boundSlot = new VBox(3);
        Label boundLabel = new Label("upper bound");
        boundLabel.setStyle("-fx-text-fill: #858585; -fx-font-size: 9px; -fx-font-weight: bold;");
        boundZone = new DropZone("expression", "drop expression");
//        boundZone.setFillWidth(true);
//        boundZone.setMinWidth(100);
        boundSlot.getChildren().addAll(boundLabel, boundZone);

        slots.getChildren().addAll(iterSlot, comparator, boundSlot);

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