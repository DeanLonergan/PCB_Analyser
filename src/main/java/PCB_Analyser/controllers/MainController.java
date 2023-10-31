package PCB_Analyser.controllers;


import PCB_Analyser.main.Main;
import PCB_Analyser.utils.Utilities;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

public class MainController {
    @FXML public ImageView imageView;
    private static int totalPresent, totalResistors, totalCapacitors, totalChips;
    private static String name, height, width, size;
    private static Image image, baseImage, searchedImage;
    private static Hashtable<Integer, ArrayList<Integer>> resistorTable = new Hashtable<>();
    private static Hashtable<Integer, ArrayList<Integer>> capacitorTable = new Hashtable<>();
    private static Hashtable<Integer, ArrayList<Integer>> chipTable = new Hashtable<>();
    public static final ArrayList<Integer> componentSizes = new ArrayList<>();

    @FXML
    protected void file() {
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG", "PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilter);
            File file = fileChooser.showOpenDialog(null);
            Utilities.resetAdjustments();
            if (Utilities.validImageFile(file)) {
                image = baseImage = new Image(file.toURI().toString(), 512, 512, false, false);
                imageView.setImage(image);
                name = file.getName();
                height = String.valueOf((int) image.getHeight());
                width = String.valueOf((int) image.getWidth());
                size = Utilities.fileSizeMB(file);
            }
        } catch (Exception e) {
            System.out.println("error loading file");
        }
    }

    @FXML
    protected void details() throws IOException {
        VBox detailsVB = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/PCB_Analyser/views/detail-view.fxml")));
        Scene details = new Scene(detailsVB, 200, 100);
        Stage stage = new Stage();
        stage.setScene(details);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    protected void parameters() throws IOException {
        if (image != null) {
            VBox analyseImageVB = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/PCB_Analyser/views/parameters-view.fxml")));
            Scene analyseImage = new Scene(analyseImageVB, 536, 600);
            Stage stage = new Stage();
            stage.setTitle("Analyse Image");
            stage.setScene(analyseImage);
            stage.setResizable(false);
            stage.show();
        }
    }

    @FXML
    protected void analyse() throws IOException {
        if (searchedImage != null) {
            if (ParametersController.getComponentTypeSelected().equals("Resistor")) highlight(resistorTable, Color.RED, ParametersController.getComponentTypeSelected());
            else if (ParametersController.getComponentTypeSelected().equals("Capacitor")) highlight(capacitorTable, Color.BLUE, ParametersController.getComponentTypeSelected());
            else highlight(chipTable, Color.YELLOW, ParametersController.getComponentTypeSelected());
            VBox detailsVB = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/PCB_Analyser/views/analyse-view.fxml")));
            Scene details = new Scene(detailsVB, 200, 150);
            Stage stage = new Stage();
            stage.setScene(details);
            stage.setResizable(false);
            stage.show();
        }
    }

    @FXML
    protected void visualize() throws IOException {
        if (searchedImage != null) {
            VBox detailsVB = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/PCB_Analyser/views/visualize-view.fxml")));
            Scene details = new Scene(detailsVB, 536, 600);
            Stage stage = new Stage();
            stage.setScene(details);
            stage.setResizable(false);
            stage.show();
        }
    }

    @FXML
    protected void grayscale() {
        if (image != null) {
            try {
                WritableImage grayImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
                for (int i = 0; i < (int) image.getHeight(); i++) {
                    for (int j = 0; j < (int) image.getWidth(); j++) {
                        Color c = image.getPixelReader().getColor(j, i);
                        double r = (c.getRed() * 0.299);
                        double g = (c.getGreen() * 0.587);
                        double b = (c.getBlue() * 0.114);
                        Color gray = new Color(r + g + b, r + g + b, r + g + b, 1.0);
                        grayImage.getPixelWriter().setColor(j, i, gray);
                    }
                }
                image = grayImage;
                imageView.setImage(image);
            } catch (Exception e) {
                System.out.println("error converting image to grayscale");
            }
        }
    }

    @FXML
    protected void colours() throws IOException {
        VBox channelsVB = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/PCB_Analyser/views/colour-adjustment-view.fxml")));
        Scene colours = new Scene(channelsVB, 200, 200);
        Stage stage = new Stage();
        stage.setScene(colours);
        stage.setResizable(false);
        stage.show();
        if (imageView != null) imageView.setEffect(AdjustmentController.getColorAdjust());
    }

    @FXML
    protected void channels() throws IOException {
        VBox channelsVB = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/PCB_Analyser/views/colour-channel-view.fxml")));
        Scene channels = new Scene(channelsVB, 400, 300);
        Stage stage = new Stage();
        stage.setScene(channels);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    protected void undo() {
        if (image != null) {
            image = baseImage;
            imageView.setImage(image);
            ((Pane) imageView.getParent()).getChildren().removeIf(r -> r instanceof Rectangle);
            ((Pane) imageView.getParent()).getChildren().removeIf(r -> r instanceof Text);
        }
        Utilities.resetAdjustments();
        totalPresent = totalResistors = totalCapacitors = totalChips = 0;
        componentSizes.clear();
    }

    @FXML
    protected void close() {
        if (imageView != null) {
            image = null;
            imageView.setImage(null);
            Utilities.resetAdjustments();
        }
    }

    @FXML
    protected void exit() {
        System.exit(0);
    }

    /**
     * Method that draws a coloured rectangle around a component.
     * @param pixelTable the hash table of pixels that represent the components to be highlighted.
     * @param color the colour of the rectangle.
     * @param component the component(s) being highlighted.
     */
    public void highlight(Hashtable<Integer, ArrayList<Integer>> pixelTable, Color color, String component) {
        setTotalPresent(0);
        int count;
        int[] order = new int[pixelTable.size()];
        sort(order, pixelTable);
        for (int root : pixelTable.keySet()) {                                                                              //Loop through the pixel table.
            int top = Integer.MAX_VALUE, bottom = -Integer.MAX_VALUE, left = Integer.MAX_VALUE, right = -Integer.MAX_VALUE; //Set the dimension of the highlight to max values.
            for (int i : pixelTable.get(root)) {                                                                            //Loop through the list at the current root in the pixel table.
                if (i % 512 < left) left = i % 512;                                                                         //If the current value is larger than the previous-
                if (i % 512 > right) right = i % 512;                                                                       //-and within the bounds of the image. Then set the-
                if (i / 512 < top) top = i / 512;                                                                           //-corresponding dimension with that value.
                if (i / 512 > bottom) bottom = i / 512;
            }
            if (pixelTable.get(root).size() >= ParametersController.getComponentSizeSelected()) {                            //If the set of pixels is large enough.
                componentSizes.add(pixelTable.get(root).size());                                                            //Add the size of the set to the list of component sizes.
                setTotalPresent(totalPresent + 1);                                                                          //Increment the number of present components by 1.
                Rectangle rect = new Rectangle(left, top, right - left, bottom - top);                               //Create a new rectangle with the calculated dimensions.
                count = Utilities.findIndex(order, root);                                                                   //Set count to the index of the current component.
                rect.setTranslateX(imageView.getLayoutX());
                rect.setTranslateY(imageView.getLayoutY());
                rect.setStroke(color);
                rect.setFill(Color.TRANSPARENT);
                Tooltip.install(rect, new Tooltip("Component: " + component + "\nNumber: " + count + "\nSize: " + pixelTable.get(root).size()));
                Text t = new Text(rect.getTranslateX() + left + 2, rect.getTranslateY() + bottom - 10, count + "");
                t.setFont(new Font(20));
                t.setFill(color);
                ((Pane) imageView.getParent()).getChildren().add(rect);
                ((Pane) imageView.getParent()).getChildren().add(t);
            }
        }
        if (ParametersController.getComponentTypeSelected().equals("Resistor")) setTotalResistors(totalPresent);
        else if (ParametersController.getComponentTypeSelected().equals("Capacitor")) setTotalCapacitors(totalPresent);
        else setTotalChips(totalPresent);
    }

    public void sort(int[] array, Hashtable<Integer, ArrayList<Integer>> pixelTable) {
        int max = Integer.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            int maxRoot = 0;
            int maxCurrentVal = 0;
            for (int j : pixelTable.keySet()) {
                if(pixelTable.get(j).size() >= maxCurrentVal && pixelTable.get(j).size() < max) {
                    maxCurrentVal = pixelTable.get(j).size();
                    maxRoot = j;
                }
            }
            array[i] = maxRoot;
            max = maxCurrentVal;
        }
    }

    public static void setTotalPresent(int totalPresent) {
        MainController.totalPresent = totalPresent;
    }

    public static int getTotalResistors() {
        return totalResistors;
    }

    public static void setTotalResistors(int totalResistors) {
        MainController.totalResistors = totalResistors;
    }

    public static int getTotalCapacitors() {
        return totalCapacitors;
    }

    public static void setTotalCapacitors(int totalCapacitors) {
        MainController.totalCapacitors = totalCapacitors;
    }

    public static int getTotalChips() {
        return totalChips;
    }

    public static void setTotalChips(int totalChips) {
        MainController.totalChips = totalChips;
    }

    public static Image getImage() {
        return image;
    }

    public static Image getSearchedImage() {
        return searchedImage;
    }

    public static void setSearchedImage(Image searchedImage) {
        MainController.searchedImage = searchedImage;
    }

    public static String getName() {
        return name;
    }

    public static String getHeight() {
        return height;
    }

    public static String getWidth() {
        return width;
    }

    public static String getSize() {
        return size;
    }

    public static Hashtable<Integer, ArrayList<Integer>> getResistorTable() {
        return resistorTable;
    }

    public static void setResistorTable(Hashtable<Integer, ArrayList<Integer>> resistorTable) {
        MainController.resistorTable = resistorTable;
    }

    public static Hashtable<Integer, ArrayList<Integer>> getCapacitorTable() {
        return capacitorTable;
    }

    public static void setCapacitorTable(Hashtable<Integer, ArrayList<Integer>> capacitorTable) {
        MainController.capacitorTable = capacitorTable;
    }

    public static Hashtable<Integer, ArrayList<Integer>> getChipTable() {
        return chipTable;
    }

    public static void setChipTable(Hashtable<Integer, ArrayList<Integer>> chipTable) {
        MainController.chipTable = chipTable;
    }
}