package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import com.example.demo.gui.dragdrop.BlockTransfer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

public class ForLoopBlock extends Block {

    private DropZone initZone;      // Expects InitializationBlock
    private DropZone conditionZone; // Expects RightUpperBoundBlock
    private DropZone incrementZone; // Expects AdditiveAssignmentBlock
    private VBox bodyZone;
    private BlockTransfer transfer;

    public ForLoopBlock() {
        super();
        // Accent: Bright Blue
        setStyle(getStyle() + "-fx-background-color: #1e2a3a; -fx-border-color: #4da6ff;");

        Label title = new Label("FOR LOOP");
        title.setStyle("-fx-text-fill: #4da6ff; -fx-font-weight: bold; -fx-font-size: 12px;");

        HBox paramsRow = new HBox(12);
        paramsRow.setPadding(new Insets(5, 0, 5, 0));

        VBox initSlot = createLabeledSlot("initialize", initZone = new DropZone("initialization", "start"));
        VBox condSlot = createLabeledSlot("condition", conditionZone = new DropZone("bound", "limit"));
        VBox incSlot = createLabeledSlot("step", incrementZone = new DropZone("assignment", "update"));

        paramsRow.getChildren().addAll(initSlot, condSlot, incSlot);

        bodyZone = new VBox(8);
        bodyZone.setStyle("-fx-border-color: #4da6ff; -fx-border-width: 0 0 0 3; -fx-padding: 10 0 10 20; -fx-min-height: 60; -fx-background-color: rgba(77, 166, 255, 0.05);");

        javafx.scene.control.Button deleteBtn = createDeleteButton();
        deleteBtn.setOnAction(e -> { if (getParent() != null) ((VBox) getParent()).getChildren().remove(this); });

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getChildren().addAll(title, new Region(), deleteBtn);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);

        getChildren().addAll(header, paramsRow, bodyZone);
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

    private void setupBodyDropZone() {
        transfer.setupBodyTarget(bodyZone);
    }

    @Override
    public Statement buildStatement() {
        Block initBlock = initZone.getBlock();
        Block condBlock = conditionZone.getBlock();
        Block incBlock = incrementZone.getBlock();

        // Validate: initBlock must be InitializationBlock
        if (initBlock == null) {
            throw new RuntimeException("ForLoop missing Initialization block");
        }
        if (!(initBlock instanceof InitializationBlock)) {
            throw new RuntimeException("INIT slot requires Initialization block, got " + initBlock.getClass().getSimpleName());
        }

        // Validate: condBlock must be RightUpperBoundBlock
        if (condBlock == null) {
            throw new RuntimeException("ForLoop missing Bound block");
        }
        if (!(condBlock instanceof RightUpperBoundBlock)) {
            throw new RuntimeException("CONDITION slot requires Bound block, got " + condBlock.getClass().getSimpleName());
        }

        // Validate: incBlock must be AdditiveAssignmentBlock
        if (incBlock == null) {
            throw new RuntimeException("ForLoop missing Increment block");
        }
        if (!(incBlock instanceof AdditiveAssignmentBlock)) {
            throw new RuntimeException("INCREMENT slot requires Assignment block, got " + incBlock.getClass().getSimpleName());
        }

        // Build the for loop - using Initialization specifically
        Initialization init = (Initialization) initBlock.buildStatement();
        Bound condition = (Bound) condBlock.buildExpression();
        AdditiveAssignment increment = (AdditiveAssignment) incBlock.buildStatement();

        ForLoop loop = new ForLoop(init, condition, increment);

        // Add body statements
        for (javafx.scene.Node node : bodyZone.getChildren()) {
            if (node instanceof Block) {
                Statement stmt = ((Block) node).buildStatement();
                if (stmt != null) {
                    loop.addStatement(stmt);
                }
            }
        }

        return loop;
    }

    @Override
    public Expression buildExpression() {
        throw new UnsupportedOperationException("ForLoop cannot be used as expression");
    }

    @Override
    public String getBlockDescription() {
        return "For Loop";
    }

    @Override
    public boolean canDropInto(String slotType) {
        return false; // ForLoop can't be dropped into slots, only into body zones
    }

    @Override
    public Block cloneBlock() {
        ForLoopBlock clone = new ForLoopBlock();
        // Note: Don't clone contents - they'll be re-dropped
        return clone;
    }
}