package PCB_Analyser.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailController implements Initializable {
    @FXML public Label labelName, labelHeight, labelWidth, labelSize;
    @FXML public Button closeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(MainController.getImage() != null) {
            labelName.setText("Name:     " + MainController.getName());
            labelHeight.setText("Height:   " + MainController.getHeight());
            labelWidth.setText("Width:    " + MainController.getWidth());
            labelSize.setText("Size:        " + MainController.getSize());
        }
    }

    @FXML
    protected void closeButton() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}