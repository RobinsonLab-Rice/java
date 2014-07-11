package main.model.plate.objects;

import java.awt.geom.Point2D;

import main.model.plate.PlateModel;

/**
 * Class that holds all information relevant to the arm's current state.
 * @author Christian
 *
 */
public class ArmState {

	/**
	 * Current location of the arm. This is the only place this is stored.
	 */
	private Point2D currentLocation;

    /**
     * Reference to the plate model.
     */
    private PlateModel plateModel;
	
	/**
	 * Class that holds all information relevant to the arm's current state.
	 * @param currentLocation - Location of the arm at program startup. Not automatically
	 * 		initialized to 0, since arm could start at a corner not considered the origin.
	 */
	public ArmState(Point2D currentLocation, PlateModel plateModel){
		this.currentLocation = currentLocation;
        this.plateModel = plateModel;

	}
	
	public double getX(){
		return currentLocation.getX();
	}
	
	public double getY(){
		return currentLocation.getY();
	}

    public PlateModel getPlateModel() {
        return plateModel;
    }
	
	public void setLocation(double x, double y){
		//update the current location
		currentLocation = new Point2D.Double(x, y);
	}
}
