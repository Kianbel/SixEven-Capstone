package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import com.example.demo.gui.dragdrop.BlockTransfer;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public abstract class Block extends VBox {

    protected BlockTransfer transfer;
    // Add a reference to hold the delete button instance
    protected Button deleteBtn;

    public Block() {
        setStyle("-fx-background-color: #2d2d30; -fx-border-color: #555; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8;");

        setFillWidth(true);
        setSpacing(5);
        transfer = BlockTransfer.getInstance();
    }

    // Helper method to control button visibility and space management
    public void setDeleteButtonVisible(boolean visible) {
        if (deleteBtn != null) {
            deleteBtn.setVisible(visible);
            deleteBtn.setManaged(visible); // Keeps the layout clean when hidden
        }
    }

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