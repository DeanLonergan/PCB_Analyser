package PCB_Analyser;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.ResourceBundle;

public class VisualizeController implements Initializable {
    @FXML public ImageView visualizeImageView;
    public ArrayList<Hashtable<Integer, ArrayList<Integer>>> hashTables = new ArrayList<>();
    public ArrayList<Color> componentColours = new ArrayList<>();
    WritableImage visualizedImage = new WritableImage(512, 512);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hashTables.add(MainController.getResistorTable());
        hashTables.add(MainController.getCapacitorTable());
        hashTables.add(MainController.getChipTable());
        componentColours.add(ParametersController.getResistorColour());
        componentColours.add(ParametersController.getCapacitorColour());
        componentColours.add(ParametersController.getChipColour());
    }

    @FXML
    protected void blackAndWhite() {
        for (Hashtable<Integer, ArrayList<Integer>> hashTable : hashTables) {
            for (int root : hashTable.keySet()) {
                for (int val : hashTable.get(root)) {
                    visualizedImage.getPixelWriter().setColor(val % 512, val / 512, Color.BLACK);
                    noise(hashTable, root, visualizedImage);
                }
            }
        }
        visualizeImageView.setImage(visualizedImage);
    }

    @FXML
    protected void random() {
        for (Hashtable<Integer, ArrayList<Integer>> hashTable : hashTables) {
            for (int root : hashTable.keySet()) {
                Color color = randomColor();
                for (int val : hashTable.get(root)) {
                    visualizedImage.getPixelWriter().setColor(val % 512, val / 512, color);
                    noise(hashTable, root, visualizedImage);
                }
            }
        }
        visualizeImageView.setImage(visualizedImage);
    }

    @FXML
    protected void sampled() {
        for (int i = 0; i < hashTables.size(); i++) {
            for (int root : hashTables.get(i).keySet()) {
                for (int val : hashTables.get(i).get(root)) {
                    visualizedImage.getPixelWriter().setColor(val % 512, val / 512, componentColours.get(i));
                    noise(hashTables.get(i), root, visualizedImage);
                }
            }
        }
        visualizeImageView.setImage(visualizedImage);
    }

    public void noise(Hashtable<Integer, ArrayList<Integer>> hashtable, int root, WritableImage writableImage) {
        ArrayList<Integer> values = hashtable.get(root);
        if (values.size() < ParametersController.getComponentSizeSelected()) {
            for (int element : values) {
                writableImage.getPixelWriter().setColor(element % 512, element / 512, Color.WHITE);
            }
        }
    }

    public Color randomColor () {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r, g, b);
    }
}