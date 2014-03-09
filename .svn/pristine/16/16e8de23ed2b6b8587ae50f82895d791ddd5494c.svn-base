package view;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.geom.Point2D;

import model.plate.objects.PlateNumbering;
import model.plate.objects.PlateSpecifications;

/**
 * Adapter to link the view to the plate model. All functions that the view needs the model
 * to perform are described here.
 * @author Christian
 *
 */
public interface PlateAdapter{
	/**
	 * Tells the model to make a plate with the defined specs at the specified location.
	 * @param _platePos - Bottom left corner of the plate.
	 * @param _specs - Set of specs that the view has compiled into one wrapper class.
	 */
	public void addPlate(PlateNumbering numberingOrder, Point2D _platePos, PlateSpecifications _specs);

	/**
	 * Tells the model to paint everything it has (frame, plates, wells, etc).
	 * @param g - graphics to paint on
	 */
	public void paint(Graphics g);

	/**
	 * Method to set and/or update the frame visualizing the bounds the arm can move over.
	 * @param borderSize - width/height of the bounds, in centimeters
	 * @param canvas - which area to put the bounds on
	 */
	public void setFrame(Point2D borderSize, Component canvas);

	/**
	 * Forces the model to delete all plates on the screen.
	 */
	public void clearAllPlates();
}
