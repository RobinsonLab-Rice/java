package main.adapters.view;

import model.plate.PlateModel;
import model.plate.objects.PlateSpecifications;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 * Created by Christian on 6/12/2014.
 */
public class View2PlateAdapter {

    private PlateModel plateModel;

    /* Sets up model references necessary for this adapter. */
    public View2PlateAdapter(PlateModel plateModel) {
        this.plateModel = plateModel;
    }

    /**
     * Tells the model to make a plate with the defined specs at the specified location.
     * @param name name to refer to this plate by
     * @param numberingOrder String conveying how the wells should be numbered (going across rows, down columns, etc.)
     * @param xPos x coordinate of the plate's top left corner
     * @param yPos y coordinate of the plate's top left corner
     * @param specs specs of the plate, taken from the datasheet
     * @return whether or not adding the plate was successful
     */
    public boolean addPlate(String name, String numberingOrder, String xPos, String yPos, PlateSpecifications specs) {
        return plateModel.addPlate(name, numberingOrder, new Point2D.Double(Double.parseDouble(xPos), Double.parseDouble(yPos)), specs);
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

    /**
     * Delete first plate at specified location.
     * @param location point (in pixels) of plate location
     */
    public void deletePlate(Point location) {
        plateModel.deletePlate(location);
    }

    /**
     * Pass along mouse clicked info of start and end locations to plate model to handle selecting wells.
     * @param start MouseEvent containing all mouse info of start location
     * @param end MouseEvent containing all mouse info of end location
     */
    public void clickAndDrag(MouseEvent start, MouseEvent end) {
        plateModel.clickAndDrag(start, end);
    }
}
