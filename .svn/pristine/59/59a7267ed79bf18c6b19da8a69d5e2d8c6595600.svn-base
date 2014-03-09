package model.tasks;

import java.awt.Point;
import java.awt.geom.Point2D;

import model.plate.objects.ArmState;

/**
 * Adapter that allows the task model to talk to the plate model through specified methods only.
 * @author Christian
 *
 */
public interface PlateAdapter {
	
	/**
	 * Given a well number, returns its actual location.
	 * @param wellNumber
	 * @return location of the well, in cm from origin
	 */
	public Point2D getLocationFromNumber(int wellNumber);
	
	/**
	 * Gets variable that wraps all information relevant to current state of the arm.
	 * @return
	 */
	public ArmState getArmState();

	public Point2D getLocationFromScreen(Point point);
}
