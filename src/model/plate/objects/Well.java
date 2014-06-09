package model.plate.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.Observable;
import java.util.Observer;

import model.plate.IWellCmd;
import model.plate.WellDispatcher;

/**
 * Class representing wells found on the plates. These are contacted by the well dispatcher to do most functions.
 * @author Christian
 *
 */
public class Well implements Observer{
	
	/**
	 * Location of the well (in cm), defined by its center, relative to the top left corner of its plate.
	 */
	private Point2D relativeLocation;
	
	/**
	 * Diameter of the well.
	 */
	private double diameter;
	
	/**
	 * Plate this well resides on.
	 */
	private Plate parentPlate;
	
	/**
	 * Number of this well.
	 */
	private int number;
	
	/**
	 * Volume of liquid currently in the well, in uL.
	 */
	private double currentVolume;
	
	/**
	 * Constructor for well, sets the necessary parameters.
	 * @param _parentPlate - plate the well is on
	 * @param _location - where on the plate this particular well is
	 * @param _diameter - diameter of the well
	 */
	public Well(Plate _parentPlate, Point2D _location, double _diameter, int wellLabel){
		parentPlate = _parentPlate;
		relativeLocation = _location;
		diameter = _diameter;
		this.number = wellLabel;
	}
	
	/**
	 * General command used to get the well to do something from the model.
	 */
	@Override
	public void update(Observable o, Object cmd) {
		((IWellCmd)cmd).apply(this, (WellDispatcher) o);
	}
	
	/**
	 * Called by the dispatcher, tells its well to paint itself at the proper location.
	 * @param g - graphics to paint on
	 * @param sF - scale factor from cm to pixels
	 */
	public void paint(Graphics g, double sF){
		int screenLocX = (int)Math.round((parentPlate.getTLcorner().getX() + relativeLocation.getX()-diameter/2)*sF);
		int screenLocY = (int)Math.round((parentPlate.getTLcorner().getY() + relativeLocation.getY()-diameter/2)*sF);
		
		g.setColor(Color.BLACK);
		g.drawOval(screenLocX, screenLocY, (int)Math.round(diameter*sF), (int)Math.round(diameter*sF));
		
		//regardless, draw the well's label in the corner
		g.setFont(g.getFont().deriveFont((float) (sF*diameter/2)));
		screenLocY += Math.round(diameter*sF);
		g.drawString(Integer.toString(number), screenLocX, screenLocY);
	}
	
	/**
	 * Returns the well's position in cm, with all modifiers.
	 */
	public Point2D getAbsoluteLocation(){
		return new Point2D.Double(parentPlate.getTLcorner().getX() + relativeLocation.getX(),
								  parentPlate.getTLcorner().getY() + relativeLocation.getY());
	}
	
	/**
	 * End section for setters and getters without special calculations.
	 */
	public Point2D getRelativeLocation(){
		return relativeLocation;
	}
	public double getDiameter(){
		return diameter;
	}
	public Plate getParentPlate(){
		return parentPlate;
	}
	public int getNumber(){
		return number;
	}
	
	/**
	 * @return amount of liquid (that should be) currently in the well, in uL
	 */
	public double getCurrentVolume(){
		return currentVolume;
	}
	
	/**
	 * Add or remove some volume from this well's internal tracker.
	 * @param volume Volume changed from well, can be positive or negative.
	 */
	public void changeCurrentVolume(double volume){
		currentVolume += volume;
	}
	
	/**
	 * @return amount of liquid each well can hold, as per plate specifications
	 */
	public double getMaxVolume(){
		return parentPlate.getMaxWellVolume();
	}
}
