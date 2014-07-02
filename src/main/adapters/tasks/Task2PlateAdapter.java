package main.adapters.tasks;

import java.awt.Point;
import java.awt.geom.Point2D;

import model.plate.PlateModel;
import model.plate.objects.ArmState;

/**
 * Adapter that allows the task model to talk to the plate model through specified methods only.
 * @author Christian
 *
 */
public class Task2PlateAdapter {

    private PlateModel plateModel;

    /* Sets up model references necessary for this adapter. */
    public Task2PlateAdapter(PlateModel plateModel) {
        this.plateModel = plateModel;
    }

    /**
	 * Given a well number, returns its actual location.
	 * @param wellNumber
	 * @return location of the well, in cm from origin
	 */
	public Point2D getLocationFromNumber(int wellNumber){
        return null;
    }
	
	/**
	 * Gets variable that wraps all information relevant to current state of the arm.
	 * @return
	 */
	public ArmState getArmState(){
        return plateModel.getArmState();
    }

}
