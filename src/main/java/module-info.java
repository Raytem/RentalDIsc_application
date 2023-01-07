module com.example.oop_coursework_sem3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.oop_coursework_sem3 to javafx.fxml;
    exports com.example.oop_coursework_sem3;
    exports com.example.oop_coursework_sem3.controllers;
    opens com.example.oop_coursework_sem3.controllers to javafx.fxml;
}