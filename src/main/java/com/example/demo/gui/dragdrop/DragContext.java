package com.example.demo.gui.dragdrop;

import com.example.demo.gui.blocks.Block;

public class DragContext {
    private static DragContext instance;
    private Block draggedBlock;
    private boolean fromToolbox;
    private Object sourceParent; // Store original parent for MOVE operations
    private int sourceIndex;      // Store original index for reordering

    private DragContext() {}

    public static DragContext getInstance() {
        if (instance == null) instance = new DragContext();
        return instance;
    }

    public void startDrag(Block block, boolean fromToolbox) {
        this.draggedBlock = block;
        this.fromToolbox = fromToolbox;

        // Store original parent info for potential rollback
        if (!fromToolbox && block.getParent() != null) {
            this.sourceParent = block.getParent();
            if (block.getParent() instanceof javafx.scene.layout.Pane pane) {
                this.sourceIndex = pane.getChildren().indexOf(block);
            }
        }
    }

    public Block getDraggedBlock() {
        return draggedBlock;
    }

    public boolean isFromToolbox() {
        return fromToolbox;
    }

    public Object getSourceParent() {
        return sourceParent;
    }

    public int getSourceIndex() {
        return sourceIndex;
    }

    public void clear() {
        draggedBlock = null;
        fromToolbox = false;
        sourceParent = null;
        sourceIndex = -1;
    }

    // Optional: For rollback if drop fails
    public void restoreOriginalPosition() {
        if (!fromToolbox && draggedBlock != null && sourceParent != null) {
            if (sourceParent instanceof javafx.scene.layout.Pane pane) {
                if (!pane.getChildren().contains(draggedBlock)) {
                    if (sourceIndex >= 0 && sourceIndex <= pane.getChildren().size()) {
                        pane.getChildren().add(sourceIndex, draggedBlock);
                    } else {
                        pane.getChildren().add(draggedBlock);
                    }
                }
            }
        }
        clear();
    }
}