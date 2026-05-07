package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class InitializationBlock extends Block {

    private DropZone varZone;
    private DropZone valueZone;

    public InitializationBlock() {
        super();


        setStyle("-fx-background-color: #1e252e; " +
                "-fx-border-color: #61AFEF; " +
                "-fx-border-width: 1.5; " +
                "-fx-background-radius: 8; " +
                "-fx-border-radius: 8; " +
                "-fx-padding: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");

        HBox container = new HBox(10); // Consistent spacing with DeclarationBlock
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Functional Code (Untouched)
        varZone = new DropZone("variable", "var");
        varZone.setMinWidth(80);

        Label eqLabel = new Label("=");
        // Styled like a standard operator in VS Code (usually light gray or white)
        eqLabel.setStyle("-fx-text-fill: #DCDCAA; -fx-font-weight: bold; -fx-font-family: 'Consolas'; -fx-font-size: 16px;");

        // Functional Code (Untouched)
        valueZone = new DropZone("expression", "value");
        valueZone.setMinWidth(80);

        // Unified "Ghost" Delete Button from your DeclarationBlock
        javafx.scene.control.Button deleteBtn = new javafx.scene.control.Button("✕");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585; -fx-font-size: 10px; -fx-cursor: hand;");
        deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle("-fx-background-color: #e81123; -fx-text-fill: white; -fx-background-radius: 4;"));
        deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585;"));

        deleteBtn.setOnAction(e -> {
            if (getParent() != null) {
                ((VBox) getParent()).getChildren().remove(this);
            }
        });

        // Spacer to push the delete button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        container.getChildren().addAll(varZone, eqLabel, valueZone, spacer, deleteBtn);
        getChildren().add(container);
    }

    @Override
    public Statement buildStatement() {
        Block varBlock = varZone.getBlock();
        Block valBlock = valueZone.getBlock();

        if (varBlock == null || valBlock == null) {
            throw new RuntimeException("Initialization missing variable or value");
        }

        Variable var = (Variable) varBlock.buildExpression();
        Expression val = valBlock.buildExpression();

        return new Initialization(var, val);
    }

    @Override
    public Expression buildExpression() {
        throw new UnsupportedOperationException("Initialization cannot be used as expression");
    }

    @Override
    public String getBlockDescription() {
        return "Initialize variable";
    }

    @Override
    public boolean canDropInto(String slotType) {
        return "initialization".equals(slotType);
    }

    @Override
    public Block cloneBlock() {
        return new InitializationBlock();
    }
}