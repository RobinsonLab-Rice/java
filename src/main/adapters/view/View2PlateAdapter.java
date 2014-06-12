package main.adapters.view;

import model.plate.PlateModel;
import model.plate.objects.PlateNumbering;
import model.plate.objects.PlateSpecifications;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Christian on 6/12/2014.
 */
public class View2PlateAdapter {

    private PlateModel plateModel;

    /**
     * Sets up model references necessary for this adapter.
     *
     * @param plateModel - reference to the plate model
     */
    public View2PlateAdapter(PlateModel plateModel) {
        this.plateModel = plateModel;
    }

    /**
     * Tells the model to make a plate with the defined specs at the specified location.
     *
     * @param numberingOrder - Method of ordering wells on a plate
     * @param platePos       - Bottom left corner of the plate.
     * @param specs          - Set of specs that the view has compiled into one wrapper class.
     */
    public void addPlate(PlateNumbering numberingOrder, Point2D platePos, PlateSpecifications specs) {
        plateModel.addPlate(numberingOrder, platePos, specs);
    }

    /**
     * Tells the model to paint everything it has (frame, plates, wells, etc).
     *
     * @param g - graphics to paint on
     */
    public void paint(Graphics g) {
        plateModel.paintAll(g);
    }

    /**
     * Method to set and/or update the frame visualizing the bounds the arm can move over.
     *
     * @param borderSize - width/height of the bounds, in centimeters
     * @param canvas     - which area to put the bounds on
     */
    public void setFrame(Point2D borderSize, Component canvas) {
        plateModel.setBorderFrame(borderSize, canvas);
    }

    /**
     * Forces the model to delete all plates on the screen.
     */
    public void clearAllPlates() {
        plateModel.clearAllPlates();
    }
}
