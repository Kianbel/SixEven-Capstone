module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires matheclipse.core;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo.gui;
    opens com.example.demo.gui to javafx.fxml;
}