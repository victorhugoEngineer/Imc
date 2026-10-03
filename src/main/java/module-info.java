module com.example.org {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.org to javafx.fxml;
    opens com.example.org.controller to javafx.fxml;
    opens com.example.org.model to javafx.base;

    exports com.example.org;
}