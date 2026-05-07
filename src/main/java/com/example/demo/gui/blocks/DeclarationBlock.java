package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import com.example.demo.gui.dragdrop.BlockTransfer;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

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
        masterVariable = new Variable("x");
        // Accent: Amber/Gold
        setStyle(getStyle() + "-fx-background-color: #3d362a; -fx-border-color: #e1ad01;");

        HBox container = new HBox(8);
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        typeLabel = new Label("var");
        typeLabel.setStyle("-fx-text-fill: #e1ad01; -fx-font-weight: bold; -fx-font-family: 'Consolas';");

        varName = new TextField("x");
        varName.setPrefWidth(80);
        varName.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #CE9178; -fx-border-color: #555; -fx-border-radius: 3; -fx-font-family: 'Consolas';");

        Button deleteBtn = createDeleteButton();
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) {
                for (VariableBlock vb : new ArrayList<>(linkedVariableBlocks)) {
                    if (vb.getParent() != null) ((VBox) vb.getParent()).getChildren().remove(vb);
                }
                ((VBox) getParent()).getChildren().remove(this);
            }
        });

        lockIcon = new Label("🔓");
        lockIcon.setStyle("-fx-text-fill: #858585;");

        container.getChildren().addAll(typeLabel, varName, lockIcon, deleteBtn);
        getChildren().add(container);
    }

    private Button createDeleteButton() {
        javafx.scene.control.Button btn = new javafx.scene.control.Button("✕");
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585; -fx-font-size: 10px; -fx-cursor: hand;");

        // Hover effects
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #e81123; -fx-text-fill: white; -fx-font-size: 10px;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585; -fx-font-size: 10px;"));

        return btn;
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