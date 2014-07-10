package util;

import java.awt.*;

/**
 * Utility class that supplies class routines for generating various random values
 */
public class Randomizer implements IRandomizer {

  public static Randomizer Singleton = new Randomizer();
  
  private Randomizer() {
  }
  
  /**
   * Generates a random location point subject to the constraint that 0<=X<=rect.width and 0<=Y<=rect.height.
   */
  public Point randomLoc( Rectangle rect) {
    return (new Point( randomInt(0, rect.width), randomInt(0, rect.height)));
  }
  
  
  /**
   * Generates a random location point subject to the constraint that 0<=X<=dim.width and 0<=Y<=dim.height.
   */
  public Point randomLoc( Dimension dim) {
    return (new Point( randomInt(0, dim.width), randomInt(0, dim.height)));
  }
  
  /**
   * Returns a random integer greater than or equal to min and less than max.
   */
  public int randomInt(int min, int max) {
    return (int)Math.floor((Math.random()*(1+max-min))+min);
  }
  
  /**
   * Returns a random double greater than or equal to min and less than max.
   */
  public double randomDouble(double min, double max) {
    return (Math.random()*(max-min))+min;
  }
  
  /**
   * Returns a random velocity (as a Point) such that 0<=|Vx|<=rect.width and 0<=|Vy|<=rect.height
   */
  public Point randomVel( Rectangle rect) {
    return (new Point (randomInt(-rect.width, rect.width), randomInt(-rect.height, rect.height)));
  }
  
  
  /**
   * Generates a random color
   */
  public Color randomColor()
  {
    return new Color( randomInt(0,255),randomInt(0,255),randomInt(0,255));
  }
}

