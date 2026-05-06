package com.example.demo.gui.blocks;

import com.example.demo.classes.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UnaryOperationBlock extends Block {

    private DropZone operandZone;
    private ComboBox<String> operatorBox;
    private ComboBox<String> positionBox;

    public UnaryOperationBlock() {
        super();

        VBox container = new VBox(5);

        Label title = new Label("UNARY OPERATION");
        title.setStyle("-fx-text-fill: #CE9178; -fx-font-size: 10px;");

        HBox slots = new HBox(5);
        slots.setAlignment(javafx.geometry.Pos.CENTER);

        positionBox = new ComboBox<>();
        positionBox.getItems().addAll("Prefix", "Postfix");
        positionBox.setValue("Postfix");
        positionBox.setPrefWidth(70);

        operatorBox = new ComboBox<>();
        operatorBox.getItems().addAll("++", "--");
        operatorBox.setValue("++");
        operatorBox.setPrefWidth(50);

        operandZone = new DropZone("variable", "variable");
        operandZone.setMinWidth(80);

        javafx.scene.control.Button deleteBtn = new javafx.scene.control.Button("✕");
        deleteBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 8px;");
        deleteBtn.setOnAction(e -> {
            if (getParent() != null) {
                ((VBox) getParent()).getChildren().remove(this);
            }
        });

        // Arrange based on prefix/postfix
        updateLayout(slots);

        positionBox.setOnAction(e -> {
            slots.getChildren().clear();
            updateLayout(slots);
        });

        HBox header = new HBox(10);
        header.getChildren().addAll(title, positionBox, operatorBox, deleteBtn);

        container.getChildren().addAll(header, slots);
        getChildren().add(container);
    }

    private void updateLayout(HBox slots) {
        boolean isPrefix = "Prefix".equals(positionBox.getValue());

        if (isPrefix) {
            slots.getChildren().addAll(
                    new Label(operatorBox.getValue()),
                    operandZone
            );
        } else {
            slots.getChildren().addAll(
                    operandZone,
                    new Label(operatorBox.getValue())
            );
        }

        // Style the operator labels
        for (javafx.scene.Node node : slots.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setStyle("-fx-text-fill: #4EC9B0; -fx-font-weight: bold; -fx-font-size: 14px;");
            }
        }
    }

    @Override
    public Expression buildExpression() {
        Block operandBlock = operandZone.getBlock();

        if (operandBlock == null) {
            throw new RuntimeException("Unary operation missing operand");
        }

        Variable var = (Variable) operandBlock.buildExpression();
        String op = operatorBox.getValue();
        boolean isPrefix = "Prefix".equals(positionBox.getValue());

        if ("++".equals(op)) {
            return new Unary(new Operator("++"), var, isPrefix);
        } else {
            return new Unary(new Operator("--"), var, isPrefix);
        }
    }

    @Override
    public Statement buildStatement() {
        // For standalone increment/decrement statements
        Expression expr = buildExpression();
        return new SingleLineStatement(expr);
    }

    @Override
    public String getBlockDescription() {
        String op = operatorBox.getValue();
        boolean isPrefix = "Prefix".equals(positionBox.getValue());
        String var = operandZone.getBlock() != null ? operandZone.getBlock().getBlockDescription() : "?";

        if (isPrefix) {
            return op + var;
        } else {
            return var + op;
        }
    }

    @Override
    public boolean canDropInto(String slotType) {
        return "expression".equals(slotType) || "assignment".equals(slotType);
    }

    @Override
    public Block cloneBlock() {
        UnaryOperationBlock clone = new UnaryOperationBlock();
        clone.operatorBox.setValue(this.operatorBox.getValue());
        clone.positionBox.setValue(this.positionBox.getValue());
        return clone;
    }
}