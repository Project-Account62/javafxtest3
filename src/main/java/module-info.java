module com.example.javafxtest3 {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.javafxtest3 to javafx.fxml;
    exports com.example.javafxtest3;
    exports com.example.javafxtest3.controller;
    opens com.example.javafxtest3.controller to javafx.fxml;
}