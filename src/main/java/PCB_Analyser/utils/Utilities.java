package PCB_Analyser.utils;

import PCB_Analyser.controllers.AdjustmentController;
import javafx.scene.paint.Color;

import java.io.File;

/**
 * Utilities class.
 *
 * @author Dean Lonergan
 */
public class Utilities {

    /**
     * Method to test if a file is a valid image type.
     * @param file input file.
     * @return true if the file has a .jpg or .png suffix, false otherwise.
     */
    public static Boolean validImageFile(File file) {
        return (file != null) && (file.getName().contains(".jpg") || file.getName().contains(".png"));
    }

    /**
     * Method to take in a file and calculate its size in megabytes and present it as a String.
     * @param file input file.
     * @return the size of the file in MB as a String.
     */
    public static String fileSizeMB(File file) {
        String fileSize = String.valueOf((double) file.length() / (1024*1024));
        return fileSize.substring(0,4) + "MB";
    }

    /**
     * Method that resets any variables that have been changed by the user.
     */
    public static void resetAdjustments() {
        AdjustmentController.setHue(null);
        AdjustmentController.setSaturation(null);
        AdjustmentController.setBrightness(null);
        AdjustmentController.adjustments();
    }

    /**
     * Method that returns a double to a specified number of decimal places, multiplied by 100.
     * Used to convert a double between 0.0 and 1.0 into a more user-friendly 0.0 to 100.0.
     * @param value input double.
     * @param places the desired number of decimal places.
     * @return the double rounded to the desired decimal place, and multiplied by 100.
     */
    public static double roundAndMultiplyDouble(double value, int places) {
        double scale = Math.pow(10, places);
        return (Math.round(value * scale) / scale) * 100;
    }

    /**
     * Method that returns true if the distance between two colours is measured to be less than the required
     * colour distance that has been entered.
     * @param i colour 1
     * @param j colour 2
     * @param requiredDistance the required colour distance.
     * @return true if the distance less than or equal to required, false otherwise.
     */
    public static boolean colourDistance(Color i, Color j, double requiredDistance) {
        double a = j.getRed() - i.getRed();
        double b = j.getRed() - i.getRed();
        double c = j.getBlue() - i.getBlue();
        double measuredDistance = Math.sqrt(a * a + b * b + c * c);
        return (measuredDistance <= requiredDistance);
    }

    /**
     * Method that unions the two disjoint sets containing the elements x and y.
     * @param array a disjoint set.
     * @param p element 1
     * @param q element 2
     */
    public static void union(int[] array, int p, int q) {
        array[find(array, q)] = find(array, p);
    }

    /**
     * Method that recursively searches for the root of an element in a set.
     * @param array the disjoint set.
     * @param id the element.
     * @return the root of the set.
     */
    public static int find(int[] array, int id) {
        return array[id]==id ? id : find(array, array[id]);
    }

    /**
     * Method that searches for the index of an element in an array.
     * @param array the array.
     * @param value the element.
     * @return the index of the element.
     */
    public static int findIndex(int[] array, int value){
        for (int i = 0; i < array.length; i++) {
            if(array[i] == value) return i + 1;
        }
        return -1;
    }
}
