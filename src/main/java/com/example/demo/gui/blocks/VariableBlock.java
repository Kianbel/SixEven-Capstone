package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class VariableBlock extends Block {

    private Variable masterVariable;  // Reference to the SAME Variable from Declaration
    private DeclarationBlock declarationBlock;
    private Label nameLabel;

    // Constructor for variables linked to a declaration
    public VariableBlock(Variable masterVariable) {
        super();
        this.masterVariable = masterVariable;

        HBox container = new HBox(5);
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        nameLabel = new Label(masterVariable.toString());
        nameLabel.setStyle("-fx-text-fill: #CE9178; -fx-font-family: 'Consolas'; -fx-font-weight: bold;");

        javafx.scene.control.Button deleteBtn = new javafx.scene.control.Button("✕");
        deleteBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 8px;");
        deleteBtn.setOnAction(e -> {
            if (declarationBlock != null) {
                declarationBlock.unlinkVariableBlock(this);
            }
            if (getParent() != null) {
                ((VBox) getParent()).getChildren().remove(this);
            }
        });

        container.getChildren().addAll(nameLabel, deleteBtn);
        getChildren().add(container);

        // Context menu for navigation
        ContextMenu contextMenu = new ContextMenu();
        MenuItem goToDeclaration = new MenuItem("Go to Declaration");
        goToDeclaration.setOnAction(e -> {
            if (declarationBlock != null) {
                declarationBlock.requestFocus();
                // Scroll to declaration (optional)
            }
        });
        contextMenu.getItems().add(goToDeclaration);
        setOnContextMenuRequested(event -> {
            contextMenu.show(this, event.getScreenX(), event.getScreenY());
            event.consume();
        });
    }

    public void setDeclarationBlock(DeclarationBlock block) {
        this.declarationBlock = block;
    }

    public void updateVariableReference(Variable newVariable) {
        this.masterVariable = newVariable;
        nameLabel.setText(masterVariable.toString());
    }

    @Override
    public Expression buildExpression() {
        return masterVariable;  // Return the SAME instance
    }

    @Override
    public Statement buildStatement() {
        throw new UnsupportedOperationException("Variable alone is not a statement");
    }

    @Override
    public String getBlockDescription() {
        return "Variable: " + masterVariable.toString();
    }

    @Override
    public boolean canDropInto(String slotType) {
        return "variable".equals(slotType) || "expression".equals(slotType);
    }

    @Override
    public Block cloneBlock() {
        // When cloning, reference the SAME master variable
        VariableBlock clone = new VariableBlock(this.masterVariable);
        clone.setDeclarationBlock(this.declarationBlock);
        if (declarationBlock != null) {
            declarationBlock.unlinkVariableBlock(this);
            declarationBlock.unlinkVariableBlock(clone); // Actually add to list
        }
        return clone;
    }
}