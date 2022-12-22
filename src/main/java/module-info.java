module com.example.java2_vitomirhardi_checkers_projekt {
    requires javafx.controls;
    requires javafx.fxml;


    opens hr.algebra.java2_vitomirhardi_checkers_projekt to javafx.fxml;
    exports hr.algebra.java2_vitomirhardi_checkers_projekt;
    exports hr.algebra.java2_vitomirhardi_checkers_projekt.controllers;
    opens hr.algebra.java2_vitomirhardi_checkers_projekt.controllers to javafx.fxml;
}