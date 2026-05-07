package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import com.example.demo.gui.dragdrop.BlockTransfer;
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

public class RightUpperBoundBlock extends Block {

    private DropZone iteratorZone;   // Expects VariableBlock
    private DropZone boundZone;       // Expects ANY expression (Variable, Value, BinaryOperation)
    private ComboBox<String> comparator;
    private BlockTransfer transfer;

    public RightUpperBoundBlock() {
        super();
        setStyle(getStyle() + "-fx-background-color: #1e2a3a; -fx-border-color: #4da6ff;");

        HBox slots = new HBox(10);
        slots.setAlignment(Pos.CENTER_LEFT);

        comparator = new ComboBox<>();
        comparator.getItems().addAll("<", "<=");
        comparator.setValue("<");
        comparator.setButtonCell(new ListCell<String>() {
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

        comparator.setStyle("-fx-background-color: #1e1e1e; " +
                "-fx-border-color: #3e3e42; " +
                "-fx-border-radius: 3;");

        slots.getChildren().addAll(
                createLabeledSlot("iterator", iteratorZone = new DropZone("variable", "var")),
                comparator,
                createLabeledSlot("bound", boundZone = new DropZone("expression", "expr"))
        );

        javafx.scene.control.Button deleteBtn = createDeleteButton();
        deleteBtn.setOnAction(e -> { if (getParent() != null) ((VBox) getParent()).getChildren().remove(this); });

        Label title = new Label("LIMIT");
        title.setStyle("-fx-text-fill: #4da6ff; -fx-font-weight: bold; -fx-font-size: 12px;");

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