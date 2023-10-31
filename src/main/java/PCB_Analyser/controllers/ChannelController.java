package PCB_Analyser.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class ChannelController implements Initializable {
    @FXML public ImageView redImageView, greenImageView, blueImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            if (MainController.getImage() != null) {
                Image image = MainController.getImage();
                int width = (int) image.getWidth();
                int height = (int) image.getHeight();
                WritableImage redImage = new WritableImage(width, height);
                WritableImage greenImage = new WritableImage(width, height);
                WritableImage blueImage = new WritableImage(width, height);
                for (int i = 0;  i < width;  i++) {
                    for (int j = 0; j < height;  j++) {
                        Color c = image.getPixelReader().getColor(i, j);
                        redImage.getPixelWriter().setColor(i, j, new Color(c.getRed(), 0, 0, 1.0));
                        greenImage.getPixelWriter().setColor(i, j, new Color(0, c.getGreen(), 0, 1.0));
                        blueImage.getPixelWriter().setColor(i, j, new Color(0, 0, c.getBlue(), 1.0));
                    }
                }
                redImageView.setImage(redImage);
                greenImageView.setImage(greenImage);
                blueImageView.setImage(blueImage);
            }
        } catch (Exception e) {
            System.out.println("error converting colour channels");
        }
    }
}