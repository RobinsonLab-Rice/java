package main.model.plate.objects;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.geom.Point2D;

/**
 * Class representing the bounding box for the arm, i.e. it represents how far the arm can move in real space.
 * @author Christian
 *
 */
public class BorderFrame {
	
	/**
	 * Size of the border, in cm.
	 */
	private Point2D borderSize;
	
	/**
	 * The canvas this box should draw on.
	 */
	private Component canvas;
	
	/**
	 * How big to scale its actual size in cm to pixels on the screen.
	 */
	private double scaleFactor;
	
	/**
	 * Constructor for the bounding box, only needs to know how big it is (in real life) and where to draw on view.
	 * @param _borderSize - (length,width) in real life, should be given in millimeters
	 * @param _canvas - where to draw itself in the view
	 */
	public BorderFrame(Point2D _borderSize, Component _canvas){
		borderSize = _borderSize;
		canvas = _canvas;
		scaleFactor = Math.min(canvas.getHeight()/borderSize.getY(), canvas.getWidth()/borderSize.getX());
	}
	
	/**
	 * Draws the actual frame, i.e. a rectangle. Also recalculates scaleFactor in case the window size has changed.
	 * @param g - graphics object to draw with
	 */
	public void drawBorderFrame(Graphics g){
		scaleFactor = Math.min(canvas.getHeight()/borderSize.getY(), canvas.getWidth()/borderSize.getX());
		//subtracted 1 just for drawing it to make sure it always stays in the window, not on the edge
		g.drawRect(0, 0, (int)Math.floor(borderSize.getX()*scaleFactor)-1, (int)Math.floor(borderSize.getY()*scaleFactor)-1);
	}
	
	/**
	 * @return scale factor = pixels/cm
	 */
	public double getScaleFactor(){
		return scaleFactor;
	}
}
