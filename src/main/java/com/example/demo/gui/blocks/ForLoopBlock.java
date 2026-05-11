package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import com.example.demo.gui.dragdrop.BlockTransfer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ForLoopBlock extends Block {

    private DropZone initZone;      // Expects InitializationBlock
    private DropZone conditionZone; // Expects RightUpperBoundBlock
    private DropZone incrementZone; // Expects AdditiveAssignmentBlock
    private VBox bodyZone;
    private BlockTransfer transfer;

    public ForLoopBlock() {
        super();
        // 1. Initialize the transfer instance as you had it
        this.transfer = BlockTransfer.getInstance();

        setStyle("-fx-background-color: #28212b; " +
                "-fx-border-color: #C678DD; " +
                "-fx-border-width: 1.5; " +
                "-fx-background-radius: 8; " +
                "-fx-border-radius: 8; " +
                "-fx-padding: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");


        HBox header = new HBox(10);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label titleLabel = new Label("for");
        titleLabel.setStyle("-fx-text-fill: #C678DD; -fx-font-weight: bold; -fx-font-family: 'Consolas'; -fx-font-size: 13px;");

        javafx.scene.control.Button deleteBtn = new javafx.scene.control.Button("✕");
        deleteBtn.setMinWidth(20);
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585; -fx-font-size: 10px; -fx-cursor: hand;");
        deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle("-fx-background-color: #e81123; -fx-text-fill: white; -fx-background-radius: 4;"));
        deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585;"));
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) ((VBox) getParent()).getChildren().remove(this);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(titleLabel, spacer, deleteBtn);

        // Parameter Row (Init, Condition, Increment)
        HBox paramsRow = new HBox(10);
        paramsRow.setPadding(new Insets(5, 0, 5, 0));
        paramsRow.setAlignment(Pos.CENTER_LEFT);

        VBox initSlot = createLabeledSlot("initialize", initZone = new DropZone("initialization", "start"));
        VBox condSlot = createLabeledSlot("condition", conditionZone = new DropZone("bound", "limit"));
        VBox incSlot = createLabeledSlot("step", incrementZone = new DropZone("assignment", "update"));

        paramsRow.getChildren().addAll(initSlot, condSlot, incSlot);

        bodyZone = new VBox(8);
        bodyZone.setMinWidth(200);
        bodyZone.setMinHeight(60);
        // Nesting Sidebar style
        bodyZone.setStyle("-fx-border-color: #C678DD; " +
                "-fx-border-width: 0 0 0 3; " +
                "-fx-background-color: rgba(198, 120, 221, 0.03); " +
                "-fx-padding: 10; " +
                "-fx-margin: 5 0 0 10;");

        // Add components to the block
        getChildren().addAll(header, paramsRow, bodyZone);

        setupBodyDropZone();
    }

    // Private helper to keep the UI clean
    private VBox createLabeledSlot(String text, DropZone zone) {
        VBox slot = new VBox(3);
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #858585; -fx-font-size: 9px; -fx-font-weight: bold; -fx-font-family: 'Consolas';");
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