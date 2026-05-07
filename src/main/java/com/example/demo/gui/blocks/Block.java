package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import com.example.demo.gui.dragdrop.BlockTransfer;
import javafx.scene.layout.VBox;

public abstract class Block extends VBox {

    protected BlockTransfer transfer;

    public Block() {
        setStyle("-fx-background-color: #252526; " +
                "-fx-border-color: #3e3e42; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5; " +
                "-fx-padding: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 5);");
        setSpacing(8);
        transfer = BlockTransfer.getInstance();
    }

    // These methods are called by BlockTransfer - they don't handle TransferMode themselves
    public void setupAsDragSource(boolean fromToolbox) {
        if (fromToolbox) {
            transfer.setupToolboxDragSource(this);
        } else {
            transfer.setupWorkspaceDragSource(this);
        }
    }

    public abstract Expression buildExpression();
    public abstract Statement buildStatement();
    public abstract String getBlockDescription();
    public abstract boolean canDropInto(String slotType);
    public abstract Block cloneBlock();
}