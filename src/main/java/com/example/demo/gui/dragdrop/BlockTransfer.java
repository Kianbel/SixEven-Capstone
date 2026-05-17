package com.example.demo.gui.dragdrop;

import com.example.demo.gui.blocks.Block;
import com.example.demo.gui.blocks.DropZone;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;

public class BlockTransfer {

    private static BlockTransfer instance;
    private DragContext dragContext;

    private BlockTransfer() {
        dragContext = DragContext.getInstance();
    }

    public static BlockTransfer getInstance() {
        if (instance == null) {
            instance = new BlockTransfer();
        }
        return instance;
    }

    // For blocks in TOOLBOX - uses COPY mode, keeps original
    public void setupToolboxDragSource(Block block) {
        block.setDeleteButtonVisible(false);

        block.setOnDragDetected(event -> {
            dragContext.startDrag(block, true);

            Dragboard db = block.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(block.getClass().getSimpleName());
            db.setContent(content);

            // Optional: Set drag view
            db.setDragView(block.snapshot(null, null));

            event.consume();
        });

        block.setOnDragDone(event -> {
            // For COPY, we don't need to remove the original
            dragContext.clear();
            event.consume();
        });
    }

    // For blocks in WORKSPACE - uses MOVE mode, removes original on successful drop
    public void setupWorkspaceDragSource(Block block) {
        block.setDeleteButtonVisible(true);

        block.setOnDragDetected(event -> {
            dragContext.startDrag(block, false);

            Dragboard db = block.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(block.getClass().getSimpleName());
            db.setContent(content);

            // Optional: Set drag view with ghost effect
            db.setDragView(block.snapshot(null, null));

            event.consume();
        });

        block.setOnDragDone(event -> {
            // Only clear if drop was successful (TransferMode.MOVE)
            // If drop failed, restore position
            if (event.getTransferMode() != TransferMode.MOVE) {
                dragContext.restoreOriginalPosition();
            } else {
                dragContext.clear();
            }
            event.consume();
        });
    }

    // Setup a drop zone (for INIT, CONDITION, INCREMENT slots)
    public void setupDropZone(DropZone zone) {
        zone.setOnDragOver(event -> {
            Block dragged = dragContext.getDraggedBlock();
            if (dragged != null && dragged.canDropInto(zone.getSlotType())) {
                if (dragContext.isFromToolbox()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
                return;
            }
            event.consume();
        });

        zone.setOnDragDropped(event -> {
            Block dragged = dragContext.getDraggedBlock();
            if (dragged != null && dragged.canDropInto(zone.getSlotType())) {
                Block droppedBlock;

                if (dragContext.isFromToolbox()) {
                    // COPY: Create new instance
                    droppedBlock = dragged.cloneBlock();
                    if (droppedBlock != null) {
                        zone.setBlock(droppedBlock);
                        droppedBlock.setDeleteButtonVisible(true);
                        event.setDropCompleted(true);
                    }
                } else {
                    // MOVE: Remove from old location first
                    Object oldParent = dragContext.getSourceParent();
                    if (oldParent instanceof VBox oldVBox) {
                        oldVBox.getChildren().remove(dragged);
                    }

                    droppedBlock = dragged;
                    zone.setBlock(droppedBlock);
                    event.setDropCompleted(true);
                }
            }
            dragContext.clear();
            event.consume();
        });
    }

    // Setup a body zone (for loop bodies, main workspace)
    public void setupBodyTarget(VBox bodyZone) {
        bodyZone.setOnDragOver(event -> {
            Block dragged = dragContext.getDraggedBlock();
            if (dragged != null) {
                if (dragContext.isFromToolbox()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            }
            event.consume();
        });

        bodyZone.setOnDragDropped(event -> {
            Block dragged = dragContext.getDraggedBlock();
            if (dragged != null) {
                Block droppedBlock;

                if (dragContext.isFromToolbox()) {
                    // COPY: Create new instance
                    droppedBlock = dragged.cloneBlock();
                    if (droppedBlock != null) {
                        bodyZone.getChildren().add(droppedBlock);
                        // Setup drag source for the new block
                        setupWorkspaceDragSource(droppedBlock);
                        event.setDropCompleted(true);
                    }
                } else {
                    // MOVE: Remove from old location first
                    Object oldParent = dragContext.getSourceParent();
                    if (oldParent instanceof VBox oldVBox) {
                        oldVBox.getChildren().remove(dragged);
                    }

                    droppedBlock = dragged;
                    bodyZone.getChildren().add(droppedBlock);
                    event.setDropCompleted(true);
                }
            }
            dragContext.clear();
            event.consume();
        });
    }
}