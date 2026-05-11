package com.example.demo.gui.blocks;

import com.example.demo.gui.dragdrop.BlockTransfer;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DropZone extends VBox {
    private String slotType;
    private Block currentBlock;
    private Label placeholderLabel;
    private BlockTransfer transfer;

    public DropZone(String slotType, String placeholderText) {
        this.slotType = slotType;
        this.transfer = BlockTransfer.getInstance();

        setMinHeight(60);
//        setMinWidth(150);
        setFillWidth(true);
        setStyle("-fx-border-color: #666; -fx-border-style: dashed; -fx-border-radius: 5; -fx-background-color: #1e1e1e; -fx-background-radius: 5;");
        setAlignment(javafx.geometry.Pos.CENTER);

        placeholderLabel = new Label(placeholderText);
        placeholderLabel.setStyle("-fx-text-fill: #666; -fx-font-style: italic;");
        getChildren().add(placeholderLabel);

        // Use BlockTransfer for drop handling
        transfer.setupDropZone(this);
    }

    public void setBlock(Block block) {
        if (currentBlock != null) {
            getChildren().remove(currentBlock);
        }
        currentBlock = block;
        getChildren().clear();
        getChildren().add(block);
        block.setStyle(block.getStyle() + "-fx-border-color: #4EC9B0; -fx-border-width: 2;");

        // Make the dropped block draggable as a workspace block (MOVE mode)
        transfer.setupWorkspaceDragSource(block);
    }

    public Block getBlock() {
        return currentBlock;
    }

    public void clear() {
        if (currentBlock != null) {
            getChildren().remove(currentBlock);
            currentBlock = null;
        }
        getChildren().add(placeholderLabel);
    }

    public String getSlotType() {
        return slotType;
    }
}