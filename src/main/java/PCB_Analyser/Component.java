package PCB_Analyser;

import javafx.scene.paint.Color;

public class Component {
    private Color colourSelected;
    private double distance;

    public Component(Color colourSelected, double distance) {
        this.colourSelected = colourSelected;
        this.distance = distance;
    }

    public Color getColourSelected() {
        return colourSelected;
    }

    public void setColourSelected(Color colourSelected) {
        this.colourSelected = colourSelected;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
