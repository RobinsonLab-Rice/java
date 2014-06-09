package util;

import java.awt.*;

public interface IRandomizer {

	/**
	 * Generates a random location point subject to the constraint that 0<=X<=rect.width and 0<=Y<=rect.height.
	 */
	public abstract Point randomLoc( Rectangle rect); 
	
	/**
	 * Generates a random location point subject to the constraint that 0<=X<=dim.width and 0<=Y<=dim.height.
	 */
	public abstract Point randomLoc( Dimension dim);
  
	/**
	 * Returns a random integer greater than or equal to min and less than max.
	 */
	public abstract int randomInt(int min, int max);
  
	/**
	 * Returns a random double greater than or equal to min and less than max.
	 */
	public abstract double randomDouble(double min, double max);
  
	/**
	 * Returns a random velocity (as a Point) such that 0<=|Vx|<=rect.width and 0<=|Vy|<=rect.height
	 */
	public abstract Point randomVel( Rectangle rect);
  
	/**
	 * Generates a random color
	 */
	public abstract Color randomColor();
}