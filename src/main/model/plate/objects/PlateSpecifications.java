package main.model.plate.objects;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Wrapper class that the view uses to combine all plate specifications together, in order to
 * concisely ship it to the model to create the plate.
 * @author Christian
 *
 */
public class PlateSpecifications implements Serializable{
	
	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = -5978528650721015506L;
	
	private Point2D wellCorner;
	private double wellSpacing;
	private Point2D borderDimensions;
	private int numRows;
	private int numCols;
	private double wellVolume;
	private double wellDiameter;
    private double wellDepth;
	
	/**
	 * Constructor that takes in raw values for the plate's dimensions and compiles it into usable points and rectangles.
	 * The bottom left corner is not needed, since it can be inferred from the other 3.
	 * @param TLcornerX - x location of top left corner
	 * @param TLcornerY - y location of top left corner
	 * @param wellSpacing - spacing between well centers
	 * @param numRows - number of rows of wells on the plate
	 * @param numCols - number of columns of wells on the plate
	 * @param width - physical width of the plate (shorter side)
	 * @param length - physical length of the plate (longer side)
	 * @param wellDiameter - top diameter of each well
	 * @param wellVolume - max volume of each well on the plate
     * @param wellDepth - depth of the well
	 */
	public PlateSpecifications(double TLcornerX, double TLcornerY, double wellSpacing,
			int numRows, int numCols, double width, double length, double wellDiameter, double wellVolume, double wellDepth){
		this.wellCorner = new Point2D.Double(TLcornerX, TLcornerY);
		this.wellSpacing = wellSpacing;
		this.borderDimensions = new Point2D.Double(length, width);
		this.numRows = numRows;
		this.numCols = numCols;
		this.wellDiameter = wellDiameter;
		this.wellVolume = wellVolume;
        this.wellDepth = wellDepth;
	}
	
	/**
	 * End section for all setters and getters
	 */
	public Point2D getWellCorner(){
		return wellCorner;
	}
	public double getWellSpacing(){
		return wellSpacing;
	}
	public Point2D getBorderDimensions(){
		return borderDimensions;
	}
	public int getNumRows(){
		return numRows;
	}
	public int getNumCols(){
		return numCols;
	}
	public double getWellVolume(){
		return wellVolume;
	}
	public double getWellDiameter(){
		return wellDiameter;
	}
    public double getWellDepth() { return wellDepth; }
}
