package PCB_Analyser;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class AnalyseController implements Initializable {
    @FXML public Label labelTotal, labelType1, labelType2, labelType3, labelLargest, labelSmallest;
    @FXML public Button closeButton;
    private int high, low;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sizes();
        if(MainController.getSearchedImage() != null) {
            labelTotal.setText("Total: " + total());
            labelType1.setText("Resistors: " + MainController.getTotalResistors());
            labelType2.setText("Capacitors: " + MainController.getTotalCapacitors());
            labelType3.setText("Chips: " + MainController.getTotalChips());
            labelLargest.setText("Largest: " + high  + " pixels (apx)");
            labelSmallest.setText("Smallest: " + low + " pixels (apx)");
        }
    }

    @FXML
    protected void closeButton() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public int total() {
        return MainController.getTotalResistors()+MainController.getTotalCapacitors()+MainController.getTotalChips();
    }

    public void sizes() {
        ArrayList<Integer> sortedSizes = MainController.componentSizes;
        Collections.sort(sortedSizes);
        high = sortedSizes.get(sortedSizes.size()-1);
        low = sortedSizes.get(0);
    }
}