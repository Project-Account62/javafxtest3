module com.example.javafxtest3 {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.javafxtest3 to javafx.fxml;
    opens com.example.javafxtest3.model to javafx.base;
    exports com.example.javafxtest3;
    exports com.example.javafxtest3.controller;
    opens com.example.javafxtest3.controller to javafx.fxml;
}