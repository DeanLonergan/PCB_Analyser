package PCB_Analyser.controllers;

import PCB_Analyser.models.Component;
import PCB_Analyser.utils.Utilities;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class ParametersController implements Initializable {
    @FXML public Button saveButton;
    @FXML public ImageView parametersImageView;
    @FXML public Rectangle displaySelectedColour;
    @FXML public Slider searchDistanceSlider;
    @FXML public ChoiceBox<String> componentType;
    @FXML public ChoiceBox<String> componentSize;
    private static int componentSizeSelected = 50;
    private static String componentTypeSelected = "Resistor";
    private static Color resistorColour, capacitorColour, chipColour;
    public Color selectedColour;
    public Image mainImage = MainController.getImage();
    public WritableImage searchedImage;
    public Component component = new Component(null,0);
    public Hashtable<Integer, ArrayList<Integer>> pixelTable = new Hashtable<>();
    public int[] pixelArray = new int[(512*512)];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        parametersImageView.setImage(MainController.getImage());
    }

    @FXML
    protected void colour() {
        parametersImageView.setOnMouseClicked(click -> {
            Color colourClicked = parametersImageView.getImage().getPixelReader().getColor((int) click.getX(), (int) click.getY());
            component.setColourSelected(colourClicked);
            displaySelectedColour.setFill(colourClicked);
            selectedColour = colourClicked;
        });
    }

    @FXML
    protected void reset() {
        parametersImageView.setImage(mainImage);
        component.setColourSelected(null);
        displaySelectedColour.setFill(null);
    }

    @FXML
    protected void search() {
        searchedImage = new WritableImage(512, 512);
        if (component.getColourSelected() != null) {
            try {
                for (int i = 0; i < 512; i++) {
                    for (int j = 0; j < 512; j++) {
                        if (Utilities.colourDistance(mainImage.getPixelReader().getColor(j, i), component.getColourSelected(), component.getDistance())) {
                            searchedImage.getPixelWriter().setColor(j, i, Color.BLACK);
                            pixelArray[512 * i + j] = 512 * i + j;
                        } else {
                            pixelArray[512 * i + j] = -1;
                        }
                    }
                }
                unionPixelsInArray();
                parametersImageView.setImage(searchedImage);
            } catch (Exception e) {
                System.out.println("error analyzing image");
            }
        }
        noise();
        buildPixelTable();
        componentColour();
        parametersImageView.setImage(searchedImage);
    }

    public void unionPixelsInArray() {
        for (int i = 0; i < pixelArray.length; i++) {                                                   //Loop through the array of pixels.
            if (pixelArray[i] >= 0) {                                                                   //When a pixel is present.
                if ((i + 1) < pixelArray.length && pixelArray[i + 1] >= 0 && (i + 1) % 512 != 0) {      //If the next pixel is within the array, is non-zero, and isn't on the next line.
                    Utilities.union(pixelArray, i, i + 1);                                           //Union the current and next pixels.
                }
                if ((i + 512) < pixelArray.length && pixelArray[i + 512] >= 0) {                        //If the pixel below is within the array and is non-zero.
                    Utilities.union(pixelArray, i, i + 512);                                         //Union the current pixel with the pixel below.
                }
            }
        }
    }

    @FXML
    protected void type() {
        componentTypeSelected = componentType.getValue();
        displaySelectedColour.setFill(component.getColourSelected());
        searchDistanceSlider.setValue(component.getDistance()*100);
    }

    @FXML
    protected void distance() {
        component.setDistance(searchDistanceSlider.getValue()*.01);
    }

    @FXML
    protected void componentSize() {
        if (componentSize.getValue().equals("Large")) componentSizeSelected = 200;
        else if (componentSize.getValue().equals("Small")) componentSizeSelected = 20;
        else componentSizeSelected = 50;
    }

    @FXML
    protected void set() {
        search();
        MainController.setSearchedImage(searchedImage);
        Stage stage = (Stage) saveButton.getScene().getWindow();
        search();
        buildPixelTable();
        stage.close();
    }

    public void buildPixelTable() {
        pixelTable = new Hashtable<>();                                     //Create a new HashTable for pixels.
        for (int i = 0; i < pixelArray.length; i++) {                       //Loop through the pixel array.
            if (pixelArray[i] != -1) {                                      //If the pixel isn't blank.
                int root = Utilities.find(pixelArray, i);                   //Set root to the root of the current pixel.
                if (pixelTable.containsKey(root)) {                         //If the pixel table contains the root as a key.
                    ArrayList<Integer> elements = pixelTable.get(root);     //Create an array list of pixels and add the root-
                    elements.add(i);                                        //-and the current pixel.
                } else {                                                    //If the root is already in present in the pixel table.
                    ArrayList<Integer> elements = new ArrayList<>();        //Create an array list of pixels.
                    elements.add(i);                                        //Add the current pixel to the list-
                    pixelTable.put(root, elements);                         //-and add the list to the pixel table.
                }
            }
        }
        //Component dependant  hash tables.
        if (componentTypeSelected.equals("Resistor")) MainController.setResistorTable(pixelTable);
        else if (componentTypeSelected.equals("Capacitor")) MainController.setCapacitorTable(pixelTable);
        else MainController.setChipTable(pixelTable);
    }

    public void noise() {
        for (int root : pixelTable.keySet()) {
            ArrayList<Integer> values = pixelTable.get(root);
            if (values.size() < componentSizeSelected) {
                for (int element : values) {
                    int column = element % 512;
                    int row = element / 512;
                    searchedImage.getPixelWriter().setColor(column, row, Color.WHITE);
                }
            }
        }
        parametersImageView.setImage(searchedImage);
        pixelTable.keySet().removeIf(k -> pixelTable.get(k).size() < componentSizeSelected);
    }

    public void componentColour() {
        if (componentTypeSelected.equals("Resistor")) setResistorColour(selectedColour);
        else if (componentTypeSelected.equals("Capacitor")) setCapacitorColour(selectedColour);
        else setChipColour(selectedColour);
    }

    public static int getComponentSizeSelected() {
        return componentSizeSelected;
    }

    public static String getComponentTypeSelected() {
        return componentTypeSelected;
    }

    public static Color getResistorColour() {
        return resistorColour;
    }

    public static void setResistorColour(Color resistorColour) {
        ParametersController.resistorColour = resistorColour;
    }

    public static Color getCapacitorColour() {
        return capacitorColour;
    }

    public static void setCapacitorColour(Color capacitorColour) {
        ParametersController.capacitorColour = capacitorColour;
    }

    public static Color getChipColour() {
        return chipColour;
    }

    public static void setChipColour(Color chipColour) {
        ParametersController.chipColour = chipColour;
    }
}