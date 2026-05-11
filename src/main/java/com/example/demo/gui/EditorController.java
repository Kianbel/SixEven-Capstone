package com.example.demo.gui;

import com.example.demo.classes.*;
import com.example.demo.database.DatabaseHandler;
import com.example.demo.gui.blocks.*;
import com.example.demo.gui.dragdrop.BlockTransfer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class EditorController {

    // FXML injected fields
    @FXML private VBox workspace;
    @FXML private VBox toolbox;
    @FXML private TextArea outputArea;
    @FXML private TextField functionNameField;
    @FXML private FlowPane parametersContainer;
    @FXML private TextField newParameterField;
    @FXML private Button addParameterButton;
    @FXML private Button calculateButton;

    private String calculatedRuntime = "";

    // Internal state
    private BlockTransfer transfer;
    private List<String> parameterList = new ArrayList<>();  // Renamed from 'parameters'

    @FXML
    private void initialize() {
        transfer = BlockTransfer.getInstance();
        setupToolbox();
        setupWorkspace();
        setupParameterControls();
    }

    private void setupParameterControls() {
        addParameterButton.setOnAction(e -> addParameter());
        newParameterField.setOnAction(e -> addParameter());
    }

    private void addParameter() {
        String paramName = newParameterField.getText().trim();
        if (paramName.isEmpty()) {
            showAlert("Error", "Parameter name cannot be empty");
            return;
        }
        if (parameterList.contains(paramName)) {
            showAlert("Error", "Parameter '" + paramName + "' already exists");
            return;
        }

        parameterList.add(paramName);

        // Add parameter chip to UI
        HBox chip = new HBox(5);
        chip.setAlignment(Pos.CENTER_LEFT);
        chip.setStyle("-fx-background-color: #3e3e42; -fx-background-radius: 15; -fx-padding: 5 10 5 10;");

        Label nameLabel = new Label(paramName);
        nameLabel.setStyle("-fx-text-fill: #4EC9B0; -fx-font-weight: bold;");

        Button removeBtn = new Button("✕");
        removeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #d32f2f; -fx-font-size: 10px;");
        removeBtn.setOnAction(e -> removeParameter(paramName, chip));

        chip.getChildren().addAll(nameLabel, removeBtn);
        parametersContainer.getChildren().add(chip);

        // Add VariableBlock to toolbox
        VariableBlock paramBlock = new VariableBlock(new Variable(paramName));
        addBlockToToolbox(paramBlock);

        newParameterField.clear();
    }

    private void removeParameter(String paramName, HBox chip) {
        parameterList.remove(paramName);
        parametersContainer.getChildren().remove(chip);

        // Remove from toolbox
        toolbox.getChildren().removeIf(node -> {
            if (node instanceof VariableBlock pvb) {
                return pvb.getBlockDescription().equals("Parameter: " + paramName);
            }
            return false;
        });
    }

    private void setupToolbox() {
        // Add regular blocks (these are always available)
        addBlockToToolbox(new DeclarationBlock());
//        addBlockToToolbox(new VariableBlock());
        addBlockToToolbox(new ValueBlock());
        addBlockToToolbox(new BinaryOperationBlock());
        addBlockToToolbox(new InitializationBlock());
        addBlockToToolbox(new RightUpperBoundBlock());
        addBlockToToolbox(new AdditiveAssignmentBlock());
        addBlockToToolbox(new ForLoopBlock());
        addBlockToToolbox(new IncrementPostFixBlock());

        // Parameter variables will be added dynamically as users add parameters
    }

    private void addBlockToToolbox(Block block) {
        block.setStyle(block.getStyle() +
                "-fx-cursor: hand; " +
                "-fx-opacity: 0.9; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 1);");

        Tooltip.install(block, new Tooltip("Drag this " + block.getBlockDescription() + " block"));
        transfer.setupToolboxDragSource(block);
        toolbox.getChildren().add(block);
    }

    private void setupWorkspace() {
        transfer.setupBodyTarget(workspace);

        Label hint = new Label("Drag blocks here to build your algorithm");
        hint.setStyle("-fx-text-fill: #858585; -fx-font-style: italic;");
        workspace.getChildren().add(hint);
    }

    @FXML
    private void onClear() {
        workspace.getChildren().clear();
        setupWorkspace(); // Re-add the hint
        outputArea.clear();
    }

    @FXML
    private void onCalculate() {
        try {
            calculatedRuntime = "";
            outputArea.setText("Building function...");

            Function fn = buildFunctionFromWorkspace();

            String runtime = fn.getRuntime();

            StringBuilder result = new StringBuilder();
            result.append("=== GENERATED CODE ===\n\n");
            result.append(fn.toString());
            result.append("\n\n=== COMPLEXITY ANALYSIS ===\n\n");
            result.append("T(n) = ").append(runtime);
            result.append("\n\n=== NOTES ===\n");
            result.append("Only '+' operator is guaranteed to work correctly for complexity sums.\n");
            result.append("Other operators may produce inaccurate results.");

            calculatedRuntime = "T(n) = " + runtime;
            outputArea.setText(result.toString());

        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage() + "\n\nStack trace will be printed to console.");
            e.printStackTrace();
        }
    }

    private Function buildFunctionFromWorkspace() {
        String name = functionNameField.getText().trim();
        if (name.isEmpty()) name = "myAlgorithm";

        Function fn = new Function(name);

        // Add parameters to function from parameterList
        for (String param : parameterList) {
            fn.addParameter(new Declaration(param));
        }

        // Build all top-level blocks in workspace (skip the hint label)
        for (javafx.scene.Node node : workspace.getChildren()) {
            if (node instanceof Block && !(node instanceof Label && ((Label) node).getText().contains("Drag blocks here"))) {
                Block block = (Block) node;
                try {
                    Statement stmt = block.buildStatement();
                    if (stmt != null) {
                        fn.addStatement(stmt);
                    }
                } catch (UnsupportedOperationException e) {
                    System.out.println("Skipping " + block.getClass().getSimpleName() + " - " + e.getMessage());
                }
            }
        }

        return fn;
    }

    @FXML
    private void onSave() {
        String text = outputArea.getText();
        if (text.isEmpty() || text.equals("Building function...")) {
            showAlert("Error", "Calculate complexity first!");
            return;
        }
        User user = deserializeUser();
        if(user == null) throw new RuntimeException("user is null on saving code to database");

        String title = functionNameField.getText();
        String code = outputArea.getText();
        String runtime = calculatedRuntime;
        String language = "Java";
        int userid = user.getUid();

        DatabaseHandler.saveCode(title, code, runtime, language, userid);
        // TODO: implement saving to database
        showAlert("Saved", "Function saved to database (simulated)");
    }

    @FXML
    private void onGoToRepository() {
        System.out.println("navigated to repository");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/CodeRepository_view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) newParameterField.getScene().getWindow();
            stage.setScene(scene);
            stage.sizeToScene();
            stage.setTitle("Code Repository");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLogout() {
        System.out.println("navigated to login");
        deleteSession();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/Login_view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) newParameterField.getScene().getWindow();
            stage.setScene(scene);
            stage.sizeToScene();
            stage.setTitle("Login");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteSession() {
        File session = new File("user.ser");
        if(session.exists()) {
            if(session.delete()) {
                System.out.printf("deleted user.ser");
            }
            else {
                System.out.println("unable to delete user.ser");
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private User deserializeUser() {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user.ser"))) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}