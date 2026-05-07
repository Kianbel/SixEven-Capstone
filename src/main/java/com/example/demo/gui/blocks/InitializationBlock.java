package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

public class InitializationBlock extends Block {

    private DropZone varZone;
    private DropZone valueZone;

    public InitializationBlock() {
        super();
        // Accent: Emerald Green
        setStyle(getStyle() + "-fx-background-color: #2a3d31; -fx-border-color: #4EC9B0;");

        HBox container = new HBox(8);
        container.setAlignment(Pos.CENTER_LEFT);

        Label eq = new Label("=");
        eq.setStyle("-fx-text-fill: #4EC9B0; -fx-font-weight: bold;");

        javafx.scene.control.Button deleteBtn = createDeleteButton();
        deleteBtn.setOnAction(e -> { if (getParent() != null) ((VBox) getParent()).getChildren().remove(this); });

        container.getChildren().addAll(
                varZone = new DropZone("variable", "var"),
                eq,
                valueZone = new DropZone("expression", "value"),
                deleteBtn
        );
        getChildren().add(container);
    }

    private javafx.scene.control.Button createDeleteButton() {
        javafx.scene.control.Button btn = new javafx.scene.control.Button("✕");
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585; -fx-font-size: 10px; -fx-cursor: hand;");

        // Hover effects
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #e81123; -fx-text-fill: white; -fx-font-size: 10px;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #858585; -fx-font-size: 10px;"));

        return btn;
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