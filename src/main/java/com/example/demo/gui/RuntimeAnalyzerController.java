package com.example.demo.gui;

import com.example.demo.classes.*;
import com.example.demo.database.DatabaseHandler;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.ComboBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class RuntimeAnalyzerController {

    @FXML private Button calculateButton;
    @FXML private VBox workspace;
    @FXML private TextArea outputArea;

    @FXML private void onAddVar(){ workspace.getChildren().add(createActualBlock("Var")); }
    @FXML private void onAddArray(){ workspace.getChildren().add(createActualBlock("Array")); }
    @FXML private void onAddInit(){ workspace.getChildren().add(createActualBlock("Init")); }
    @FXML private void onAddFor(){ workspace.getChildren().add(createActualBlock("For")); }
    @FXML private void onAddInc(){ workspace.getChildren().add(createActualBlock("Inc")); }
    @FXML private void onAddDec(){ workspace.getChildren().add(createActualBlock("Dec")); }
    @FXML private void onAddIndexing(){ workspace.getChildren().add(createActualBlock("Indexing")); }
    @FXML private void onAddRecursion(){ workspace.getChildren().add(createActualBlock("Recursion")); }

    private User currentUser = null;

    @FXML
    private void initialize() {
        currentUser = deserializeUser();
        if(currentUser == null) throw new RuntimeException("Error during deserializing user.ser");
    }

    @FXML
    private void onCalculate() {
        outputArea.setText("Calculating...");

         calculateButton.setDisable(true);

        Task<String> calculationTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                Function currentFunction = new Function("myAlgorithm");
                currentFunction.addParameter(new Declaration("n"));

                buildLogicFromUI(workspace, currentFunction);

                return "CODE:\n" + currentFunction + "\n\nCOMPLEXITY:\nT(n) = " + currentFunction.getRuntime();
            }
        };

        calculationTask.setOnSucceeded(e -> {
            outputArea.setText(calculationTask.getValue());
             calculateButton.setDisable(false);
        });

        calculationTask.setOnFailed(e -> {
            Throwable exception = calculationTask.getException();
            outputArea.setText("Error: " + exception.getMessage());
            exception.printStackTrace();
             calculateButton.setDisable(false);
        });

        Thread backgroundThread = new Thread(calculationTask);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    @FXML
    private void onSave() {
        String fullText = outputArea.getText();
        if (fullText == null || fullText.isEmpty()) {
            showToast("Nothing to save! Calculate complexity first.");
            return;
        }

        // TODO: Implement loadUserFromSer
//        User currentUser = loadUserFromSer();
//        if (currentUser == null) {
//            showToast("Error: No user session found.");
//            return;
//        }

        showToast("Saving to database..."); // Immediate feedback

        Task<Boolean> saveTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {

                int start = fullText.indexOf("CODE:") + 5;
                int end = fullText.indexOf("(n)");
                String title = (start > 4 && end > start) ? fullText.substring(start, end).trim() : "Untitled Analysis";
                String language = fullText.contains("#include") ? "C++" : "Java";

                String runtime = "Unknown";
                if (fullText.contains("T(n) =")) {
                    int rStart = fullText.indexOf("T(n) =");
                    int rEnd = fullText.indexOf("\n", rStart);
                    runtime = (rEnd != -1) ? fullText.substring(rStart, rEnd).trim() : fullText.substring(rStart).trim();
                }

                return DatabaseHandler.saveCode(title, fullText, runtime, language, currentUser.getUid());
                // TODO: replace when loading user is implemented
//                return DatabaseHandler.saveCode(title, fullText, runtime, language, currentUser.getUserid());
            }
        };

        saveTask.setOnSucceeded(e -> {
            if (saveTask.getValue()) showToast("Successfully saved!");
            else showToast("Database Error: Save failed.");
        });

        new Thread(saveTask).start();
    }

    private void showToast(String message) {
        Stage stage = (Stage) workspace.getScene().getWindow();
        Popup popup = new Popup();

        Label label = new Label(message);
        label.setStyle("-fx-background-color: #252526; -fx-text-fill: #dcdcdc; " +
                "-fx-padding: 15px; -fx-background-radius: 10px; " +
                "-fx-border-color: #007acc; -fx-border-radius: 10px; " +
                "-fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");

        popup.getContent().add(label);
        popup.show(stage);

        popup.setX(stage.getX() + stage.getWidth()/2 - label.getWidth()/2);
        popup.setY(stage.getY() + stage.getHeight() - 100);

        FadeTransition fade = new FadeTransition(Duration.seconds(3), label);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> popup.hide());
        fade.play();
    }

    private Label addLabel(String message){
        Label l = new Label(message);
        l.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold;");
        return l;
    }

    private Node createActualBlock(String type) {
        VBox blockBase = new VBox(5);
        blockBase.setPrefWidth(1200);
        blockBase.setMaxWidth(Double.MAX_VALUE);
        blockBase.setPadding(new Insets(5));
        blockBase.setStyle("-fx-border-color: #444; -fx-border-radius: 5; -fx-background-radius: 5;");
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        switch (type) {
            case "Var" -> {
                blockBase.setStyle(blockBase.getStyle() + "-fx-background-color: #FFCC80;");
                TextField name = new TextField("n");
                name.setPrefWidth(60);
                header.getChildren().addAll(addLabel("Declare"), name);
                blockBase.setUserData(new BlockData("Var", name, null, null, null, null, null));
            }
            case "Array" -> {
                blockBase.setStyle(blockBase.getStyle() + "-fx-background-color: #FFAB91;");
                TextField name = new TextField("A");
                name.setPrefWidth(50);
                TextField size = new TextField("10");
                size.setPrefWidth(50);
                header.getChildren().addAll(addLabel("Array"), name, addLabel("["), size, addLabel("]"));
                blockBase.setUserData(new BlockData("Array", name, size, null, null, null, null));
            }
            case "Init" -> {
                blockBase.setStyle(blockBase.getStyle() + "-fx-background-color: #C5E1A5;");
                TextField varName = new TextField("sum");
                varName.setPrefWidth(60);
                TextField val = new TextField("0");
                val.setPrefWidth(60);
                header.getChildren().addAll(addLabel("Set"), varName, addLabel("="), val);
                blockBase.setUserData(new BlockData("Init", varName, val, null, null, null, null));
            }
            case "For" -> {
                blockBase.setStyle(blockBase.getStyle() + "-fx-background-color: #90CAF9;");

                TextField iter  = new TextField("i");  iter.setPrefWidth(30);
                TextField start = new TextField("0");   start.setPrefWidth(40);
                TextField limit = new TextField("n");   limit.setPrefWidth(60);
                TextField step  = new TextField("1");   step.setPrefWidth(40);
                ComboBox<String> isEq = new ComboBox<>();
                isEq.getItems().addAll("<", "≤");
                isEq.setValue("<");
                isEq.setPrefWidth(55);

                VBox innerBody = new VBox(5);
                innerBody.setPadding(new Insets(10, 10, 10, 30));
                innerBody.setStyle("-fx-border-color: #64B5F6; -fx-border-width: 0 0 0 4; -fx-min-height: 40;");

                MenuButton addMenu = new MenuButton("+");
                addMenuOption(addMenu,"Initialize","Init",innerBody);
                addMenuOption(addMenu,"Nested For","For",innerBody);
                addMenuOption(addMenu,"IncrementPostFix","Inc",innerBody);
                addMenuOption(addMenu,"Decrement","Dec",innerBody);
                addMenuOption(addMenu,"Indexing","Indexing",innerBody);
                addMenuOption(addMenu,"Recursion","Recursion",innerBody);

                header.getChildren().addAll(addLabel("for"), iter, addLabel("= "), start, addLabel("to"), limit, isEq, addLabel("step"), step, addMenu);
                blockBase.getChildren().add(innerBody);
                blockBase.setUserData(new BlockData("For", iter, limit, step, start, isEq, innerBody));
            }
            case "Inc" -> {
                blockBase.setStyle(blockBase.getStyle() + "-fx-background-color: #CE93D8;");
                TextField varName = new TextField("i");
                varName.setPrefWidth(50);
                header.getChildren().addAll(varName, addLabel("++"));
                blockBase.setUserData(new BlockData("Inc", varName, null, null, null, null, null));
            }
            case "Dec" -> {
                blockBase.setStyle(blockBase.getStyle() + "-fx-background-color: #CE93D8;");
                TextField varName = new TextField("i");
                varName.setPrefWidth(50);
                header.getChildren().addAll(varName, addLabel("--"));
                blockBase.setUserData(new BlockData("Dec", varName, null, null, null, null, null));
            }
            case "Indexing" -> {
                blockBase.setStyle(blockBase.getStyle() + "-fx-background-color: #80DEEA;");
                TextField arr = new TextField("A");
                arr.setPrefWidth(40);
                TextField idx = new TextField("i");
                idx.setPrefWidth(40);
                header.getChildren().addAll(addLabel("Access"), arr, addLabel("["), idx, addLabel("] ++"));
                blockBase.setUserData(new BlockData("Indexing", arr, idx, null, null, null, null));
            }
            case "Recursion" -> {
                blockBase.setStyle(blockBase.getStyle() + "-fx-background-color: #F48FB1;");
                TextField name = new TextField("solve");
                name.setPrefWidth(80);
                TextField arg  = new TextField("n-1");
                arg.setPrefWidth(60);
                // base case: when n == this value, recursion stops
                TextField base = new TextField("0");
                base.setPrefWidth(40);
                header.getChildren().addAll(
                        addLabel("Recurse"), name,
                        addLabel("("), arg, addLabel(")"),
                        addLabel("  base case: n ≤"), base
                );
                blockBase.setUserData(new BlockData("Recursion", name, arg, base, null, null, null));
            }
        }

        Button del = new Button("✕");
        del.setOnAction(e -> {
            if (blockBase.getParent() instanceof Pane p)
                p.getChildren().remove(blockBase);
        });
        header.getChildren().add(del);
        blockBase.getChildren().add(0, header);
        return blockBase;
    }

    private void addMenuOption(MenuButton menu, String label, String type, VBox target) {
        MenuItem item = new MenuItem(label);
        item.setOnAction(e -> target.getChildren().add(createActualBlock(type)));
        menu.getItems().add(item);
    }

    private void buildLogicFromUI(VBox container, Object logicParent) {
        if (container == null) return;
        for (Node node : container.getChildren()) {
            if (!(node instanceof VBox)) continue;
            BlockData data = (BlockData) node.getUserData();
            if (data == null) continue;

            switch (data.type) {

                case "Var" ->
                        addStatement(logicParent, new Declaration(data.f1.getText()));

//                case "Array" -> {
//                    int size = parseIntSafe(data.f2.getText(), 10);
//                    Array arr = new Array(data.f1.getText(), new Value<>(size));
//                    addStatement(logicParent, new Declaration(arr.toString()));
//                }

                case "Init" -> {
                    Variable v = new Variable(data.f1.getText());
                    addStatement(logicParent, new Initialization(v, parseExpression(data.f2.getText())));
                }

                case "Inc" ->
                        addStatement(logicParent, new SingleLineStatement(
                                new IncrementPostFix(new Variable(data.f1.getText()))));

//                case "Dec" ->
//                        addStatement(logicParent, new SingleLineStatement(
//                                new Decrement(new Variable(data.f1.getText()), false)));
//
//                case "Indexing" -> {
//                    Array arr = new Array(data.f1.getText());
//                    Indexing idx = new Indexing(arr, parseExpression(data.f2.getText()));
//                    addStatement(logicParent, new SingleLineStatement(
//                            new IncrementPostFix(new Variable(idx.toString()), false)));
//                }

                case "For" -> {
                    Variable iter   = new Variable(data.f1.getText());
                    boolean isEq = data.isEqualCheck != null && "≤".equals(data.isEqualCheck.getValue());
                    int      startV = parseIntSafe(data.f4 != null ? data.f4.getText() : "0", 0);
                    int      stepV  = parseIntSafe(data.f3.getText(), 1);

                    ForLoop loop = new ForLoop(
                            new Initialization(iter, new Value<>(startV)),
                            new RightUpperBound(iter, parseExpression(data.f2.getText()), isEq),
                            new AdditiveAssignment(iter, new Value<>(stepV))
                    );
                    buildLogicFromUI(data.innerContainer, loop);
                    addStatement(logicParent, loop);
                }

//                case "Recursion" -> {
//                    String funcName = data.f1.getText();
//                    String arg      = data.f2.getText();
//                    int    baseCase = parseIntSafe(data.f3 != null ? data.f3.getText() : "0", 0);
//
//                    if (baseCase < 0) {
//                        outputArea.setText("Error: base case must be >= 0 for recursion '" + funcName + "'");
//                        return;
//                    }
//
//                    Recursion rec = new Recursion(funcName);
//                    addStatement(logicParent, new SingleLineStatement(
//                            new Variable(funcName + "(" + arg + ")  // base: n <= " + baseCase)
//                    ));
//                }
            }
        }
    }


    private Expression parseExpression(String input) {
        input = input.trim();
        if (input.matches("-?\\d+"))
            return new Value<>(Integer.parseInt(input));
        if (input.contains("+")) {
            String[] parts = input.split("\\+", 2);
            return new Binary(new ArithmeticOperator("+"),
                    parseExpression(parts[0].trim()),
                    parseExpression(parts[1].trim()));
        }
        if (input.contains("-")) {
            String[] parts = input.split("-", 2);
            return new Binary(new ArithmeticOperator("-"), parseExpression(parts[0].trim()), parseExpression(parts[1].trim()));
        }
        return new Variable(input);
    }

    private int parseIntSafe(String text, int fallback) {
        try { return Integer.parseInt(text.trim()); }
        catch (NumberFormatException e) { return fallback; }
    }

    private void addStatement(Object parent, Statement s) {
        if (parent instanceof Function fn){
            fn.addStatement(s);
        } else if(parent instanceof MultiLineStatement mls){
            mls.addStatement(s);
        }
    }

    private record BlockData(String type, TextField f1, TextField f2, TextField f3, TextField f4, ComboBox<String> isEqualCheck, VBox innerContainer) {}

    private User deserializeUser() {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user.ser"))) {
            return (User) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}