module com.pcb_analyser {
    requires javafx.controls;
    requires javafx.fxml;

    exports PCB_Analyser.controllers;
    opens PCB_Analyser.controllers to javafx.fxml;
    exports PCB_Analyser.models;
    opens PCB_Analyser.models to javafx.fxml;
    exports PCB_Analyser.utils;
    opens PCB_Analyser.utils to javafx.fxml;
    exports PCB_Analyser.main;
    opens PCB_Analyser.main to javafx.fxml;
}