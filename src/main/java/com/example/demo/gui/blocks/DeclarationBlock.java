package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import com.example.demo.gui.dragdrop.BlockTransfer;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class DeclarationBlock extends Block {

    private TextField varName;
    private Label typeLabel;
    private Label lockIcon;
    private boolean isLocked = false;
    private Variable masterVariable;  // The ONE Variable instance
    private List<VariableBlock> linkedVariableBlocks = new ArrayList<>();

    public DeclarationBlock() {
        super();

        // Create the master Variable
        masterVariable = new Variable("x");

        HBox container = new HBox(5);
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        typeLabel = new Label("var");
        typeLabel.setStyle("-fx-text-fill: #4EC9B0; -fx-font-weight: bold;");

        varName = new TextField("x");
        varName.setPrefWidth(60);
        varName.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #CE9178; -fx-font-family: 'Consolas';");

        // Update master variable when text changes
        varName.textProperty().addListener((obs, old, newName) -> {
            if (!isLocked && masterVariable != null) {
                // Update the master variable
                masterVariable = new Variable(newName); // Variable stores String name
                // Update all linked VariableBlocks to show the new name
                for (VariableBlock vb : linkedVariableBlocks) {
                    vb.updateVariableReference(masterVariable);
                }
            }
        });

        javafx.scene.control.Button deleteBtn = new javafx.scene.control.Button("✕");
        deleteBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 8px;");
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) {
                // Remove all linked VariableBlocks first
                for (VariableBlock vb : new ArrayList<>(linkedVariableBlocks)) {
                    if (vb.getParent() != null) {
                        ((VBox) vb.getParent()).getChildren().remove(vb);
                    }
                }
                ((VBox) getParent()).getChildren().remove(this);
            }
        });

        lockIcon = new Label("🔓");
        lockIcon.setStyle("-fx-text-fill: #858585; -fx-font-size: 10px;");

        container.getChildren().addAll(typeLabel, varName, lockIcon, deleteBtn);
        getChildren().add(container);

        setupContextMenu();
        setupLongPress();
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem createVariableBlock = new MenuItem("Create Variable Block for '" + varName.getText() + "'");
        createVariableBlock.setOnAction(e -> createVariableBlock());

        MenuItem lockItem = new MenuItem("Lock Declaration");
        lockItem.setOnAction(e -> setLocked(true));

        MenuItem unlockItem = new MenuItem("Unlock Declaration");
        unlockItem.setOnAction(e -> setLocked(false));

        contextMenu.getItems().addAll(createVariableBlock, lockItem, unlockItem);

        setOnContextMenuRequested(event -> {
            createVariableBlock.setText("Create Variable Block for '" + varName.getText() + "'");
            lockItem.setVisible(!isLocked);
            unlockItem.setVisible(isLocked);
            contextMenu.show(this, event.getScreenX(), event.getScreenY());
            event.consume();
        });
    }

    private void setLocked(boolean locked) {
        this.isLocked = locked;
        varName.setEditable(!locked);

        if (locked) {
            lockIcon.setText("🔒");
            lockIcon.setStyle("-fx-text-fill: #4EC9B0; -fx-font-size: 10px;");
            setStyle(getStyle() + "-fx-border-color: #4EC9B0; -fx-border-width: 1;");
        } else {
            lockIcon.setText("🔓");
            lockIcon.setStyle("-fx-text-fill: #858585; -fx-font-size: 10px;");
            setStyle(getStyle().replace("-fx-border-color: #4EC9B0; -fx-border-width: 1;", ""));
        }
    }

    private void setupLongPress() {
        final javafx.animation.Timeline longPressTimeline = new javafx.animation.Timeline();
        longPressTimeline.getKeyFrames().add(
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(500), e -> createVariableBlock())
        );
        longPressTimeline.setCycleCount(1);

        setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY && !isLocked) {
                longPressTimeline.playFromStart();
            }
        });

        setOnMouseReleased(event -> longPressTimeline.stop());
        setOnMouseDragged(event -> longPressTimeline.stop());
    }

    private void createVariableBlock() {
        // Create a VariableBlock that references the SAME master variable
        VariableBlock varBlock = new VariableBlock(masterVariable);
        varBlock.setDeclarationBlock(this);
        linkedVariableBlocks.add(varBlock);

        // Insert after this declaration
        if (getParent() != null && getParent() instanceof VBox parentBox) {
            int index = parentBox.getChildren().indexOf(this);
            parentBox.getChildren().add(index + 1, varBlock);
            transfer.setupWorkspaceDragSource(varBlock);
        }

        // Auto-lock after first variable is created
        if (!isLocked && !linkedVariableBlocks.isEmpty()) {
            setLocked(true);
        }
    }

    public void unlinkVariableBlock(VariableBlock varBlock) {
        linkedVariableBlocks.remove(varBlock);
    }

    public Variable getMasterVariable() {
        return masterVariable;
    }

    @Override
    public Statement buildStatement() {
        return new Declaration(varName.getText());
    }

    @Override
    public Expression buildExpression() {
        throw new UnsupportedOperationException("Declaration cannot be used as expression");
    }

    @Override
    public String getBlockDescription() {
        return "Declaration: " + varName.getText();
    }

    @Override
    public boolean canDropInto(String slotType) {
        return false;
    }

    @Override
    public Block cloneBlock() {
        DeclarationBlock clone = new DeclarationBlock();
        clone.varName.setText(this.varName.getText());
        clone.masterVariable = new Variable(this.varName.getText());
        if (this.isLocked) {
            clone.setLocked(true);
        }
        return clone;
    }
}