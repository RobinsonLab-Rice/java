package model.plate.objects;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import model.plate.IWellCmd;
import model.plate.WellDispatcher;

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
	 * Dispatcher that talks to all wells. Safe to store since it will never change.
	 * Should never give this reference away though, for coupling reasons.
	 */
	private WellDispatcher dispatcher;
	
	/**
	 * Well the arm is currently over. Should change every time the location is set.
	 * -1 corresponds to not being over any well.
	 */
	private int currentWell = -1;
	
	/**
	 * Class that holds all information relevant to the arm's current state.
	 * @param currentLocation - Location of the arm at program startup. Not automatically
	 * 		initialized to 0, since arm could start at a corner not considered the origin.
	 * @param dispatcher - Dispatcher that talks to all wells. Safe to store since it will
	 * 		never change.
	 */
	public ArmState(Point2D currentLocation, WellDispatcher dispatcher){
		this.currentLocation = currentLocation;
		this.dispatcher = dispatcher;
	}
	
	public double getX(){
		return currentLocation.getX();
	}
	
	public double getY(){
		return currentLocation.getY();
	}
	
	public void setLocation(double x, double y){
		//update the current location
		currentLocation = new Point2D.Double(x, y);
		
		//and update which well we are currently over
		currentWell = -1;
		dispatcher.notifyAll(
			new IWellCmd(){
				public void apply(Well context, WellDispatcher disp){
					if (context.getAbsoluteLocation().distance(currentLocation) < context.getDiameter()/2)
						currentWell = context.getNumber();
				}
			}
		);
	}
	
	/**
	 * Returns whether or not the well the arm is currently on top of will overflow if given
	 * volume is dispensed. Also returns true if you would take more liquid than the well
	 * currently has in it.
	 * @param volume volume to dispense
	 * @return boolean for whether or not the well will overflow
	 */
	public boolean willOverflow(final double volume){
		//have to wrap the boolean in an ArrayList to circumvent anonymous inner class stuff
		final ArrayList<Boolean> willOverflow = new ArrayList<Boolean>();
		willOverflow.add(false);
		dispatcher.notifyAll(
			new IWellCmd(){
				public void apply(Well context, WellDispatcher disp){
					//if the well is the one we are on top of
					if (context.getNumber() == currentWell){
						//if adding volume would overflow it, or if removing volume would go below 0
						if (context.getCurrentVolume() + volume > context.getMaxVolume() ||
							context.getCurrentVolume() - volume < 0)
							willOverflow.set(0, true);
					}
				}
			}
		);
		return willOverflow.get(0);
	}
	
	public void updateWellVolume(final double volume){
		dispatcher.notifyAll(
			new IWellCmd(){
				public void apply(Well context, WellDispatcher disp){
					//if the well is the one we are on top of
					if (context.getNumber() == currentWell){
						//update its volume
						context.changeCurrentVolume(volume);
					}
				}
			}
		);
	}
}
