module com.pcb_analyser {
    requires javafx.controls;
    requires javafx.fxml;
    requires jmh.core;


    opens PCB_Analyser to javafx.fxml;
    exports PCB_Analyser;
}