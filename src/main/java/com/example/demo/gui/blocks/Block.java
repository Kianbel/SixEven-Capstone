package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import com.example.demo.gui.dragdrop.BlockTransfer;
import javafx.scene.layout.VBox;

public abstract class Block extends VBox {

    protected BlockTransfer transfer;

    public Block() {
        setStyle("-fx-background-color: #2d2d30; -fx-border-color: #555; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8;");

        setFillWidth(true);
        setSpacing(5);
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