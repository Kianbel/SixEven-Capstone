package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import com.example.demo.gui.dragdrop.BlockTransfer;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ForLoopBlock extends Block {

    private DropZone initZone;      // Expects InitializationBlock
    private DropZone conditionZone; // Expects RightUpperBoundBlock
    private DropZone incrementZone; // Expects AdditiveAssignmentBlock
    private VBox bodyZone;
    private BlockTransfer transfer;

    public ForLoopBlock() {
        super();
        this.transfer = BlockTransfer.getInstance();

        Label title = new Label("FOR LOOP");
        title.setStyle("-fx-text-fill: #90CAF9; -fx-font-weight: bold;");

        HBox paramsRow = new HBox(10);

        // INIT slot - specifically for Initialization
        VBox initSlot = new VBox(5);
        initSlot.getChildren().addAll(
                new Label("INIT:"),
                initZone = new DropZone("initialization", "Drop Initialization here")
        );

        // CONDITION slot
        VBox condSlot = new VBox(5);
        condSlot.getChildren().addAll(
                new Label("CONDITION:"),
                conditionZone = new DropZone("bound", "Drop Bound here")
        );

        // INCREMENT slot
        VBox incSlot = new VBox(5);
        incSlot.getChildren().addAll(
                new Label("INCREMENT:"),
                incrementZone = new DropZone("assignment", "Drop Assignment here")
        );

        paramsRow.getChildren().addAll(initSlot, condSlot, incSlot);

        // Body zone
        Label bodyLabel = new Label("BODY:");
        bodyLabel.setStyle("-fx-text-fill: #90CAF9;");
        bodyZone = new VBox(8);
        bodyZone.setStyle("-fx-border-color: #64B5F6; -fx-border-width: 0 0 0 3; -fx-padding: 10 0 10 15; -fx-min-height: 80;");

        // Setup body drop zone using BlockTransfer
        setupBodyDropZone();

        // Delete button
        javafx.scene.control.Button deleteBtn = new javafx.scene.control.Button("✕");
        deleteBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) {
                ((VBox) getParent()).getChildren().remove(this);
            }
        });

        HBox header = new HBox(10);
        header.getChildren().addAll(title, deleteBtn);

        getChildren().addAll(header, paramsRow, new Separator(), bodyLabel, bodyZone);
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