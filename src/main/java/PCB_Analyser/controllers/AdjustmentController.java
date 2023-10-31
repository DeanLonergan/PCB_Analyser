package PCB_Analyser.controllers;

import PCB_Analyser.utils.Utilities;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdjustmentController implements Initializable {
    private static Double hue, saturation, brightness;
    private static final ColorAdjust colorAdjust = new ColorAdjust();
    @FXML public Label labelHue, labelSaturation, labelBrightness;
    @FXML public Slider sliderHue, sliderSaturation, sliderBrightness;
    @FXML public Button confirmButton, closeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (hue != null) sliderHue.setValue(hue);
        if (saturation != null) sliderSaturation.setValue(saturation);
        if (brightness != null) sliderBrightness.setValue(brightness);
        onSliderChanged();
    }

    @FXML
    protected void onSliderChanged() {
        if (MainController.getImage() != null) {
            labelHue.setText("Hue: " + Utilities.roundAndMultiplyDouble(sliderHue.getValue(), 4));
            labelSaturation.setText("Saturation: " + Utilities.roundAndMultiplyDouble(sliderSaturation.getValue(), 4));
            labelBrightness.setText("Brightness: " + Utilities.roundAndMultiplyDouble(sliderBrightness.getValue(), 4));
        }
    }

    @FXML
    protected void confirmButton() {
        hue = sliderHue.getValue();
        saturation = sliderSaturation.getValue();
        brightness = sliderBrightness.getValue();
        adjustments();
        closeButton();
    }

    @FXML
    protected void closeButton() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public static void adjustments() {
        colorAdjust.setHue(Objects.requireNonNullElse(hue, 0.0));
        colorAdjust.setSaturation(Objects.requireNonNullElse(saturation, 0.0));
        colorAdjust.setBrightness(Objects.requireNonNullElse(brightness, 0.0));
    }

    public static void setHue(Double hue) {
        AdjustmentController.hue = hue;
    }

    public static void setSaturation(Double saturation) {
        AdjustmentController.saturation = saturation;
    }

    public static void setBrightness(Double brightness) {
        AdjustmentController.brightness = brightness;
    }

    public static ColorAdjust getColorAdjust() {
        return colorAdjust;
    }
}