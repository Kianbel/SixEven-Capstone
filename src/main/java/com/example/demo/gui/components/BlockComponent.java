package com.example.demo.gui.components;

import com.example.demo.classes.*;
import javafx.scene.layout.VBox;

public abstract class BlockComponent extends VBox {
    protected Runnable onDelete;
    protected Runnable onChange;

    public BlockComponent() {
        setSpacing(5);
        setStyle("-fx-background-color: #2d2d30; -fx-border-color: #555; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10;");
    }

    public void setOnDelete(Runnable onDelete) {
        this.onDelete = onDelete;
    }

    public void setOnChange(Runnable onChange) {
        this.onChange = onChange;
    }

    protected void notifyChange() {
        if (onChange != null) onChange.run();
    }

    public abstract String getPreview();
    public abstract void buildInto(Function function);
    public abstract void buildInto(MultiLineStatement parent);
}