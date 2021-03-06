package main.model.plate.objects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.transform.Affine;
import main.model.plate.IWellCmd;
import main.model.plate.WellDispatcher;

/**
 * Class representing which plates are present in the system, as well as where they are.
 * @author Christian
 *
 */
public class Plate implements Serializable {
	
	/**
	 * Auto generated serial ID to be able to save the Plate.
	 */
	private static final long serialVersionUID = -5180528423168711163L;

	/**
	 * Where to position the bottom left corner of the plate.
	 */
	private Point2D TLcorner;
	
	/**
	 * Wrapper class that encompasses all necessary specifications of the physical plate.
	 */
	private PlateSpecifications plateSpecs;

    /**
     * User-given nickname for this plate.
     */
    private String name;

    /**
     * How the plate should order its wells on creation (by row, by column, alphanumeric, etc.)
     */
    private String orderingType;

    /**
     * Dispatcher used to talk to individual wells, should not be serialized to JSON.
     */
    public transient WellDispatcher dispatcher;

    public boolean isRotated;
	
	/**
	 * Easy constructor that sets the proper parameters.
     * @param name - name to refer to this plate by
	 * @param platePos - location of the top left corner of the plate (in millimeters)
	 * @param plateSpecs - object containing all necessary specifications of the physical plate
	 */
	public Plate(String name, Point2D platePos, PlateSpecifications plateSpecs){
        this.name = name;
		this.TLcorner = platePos;
		this.plateSpecs = plateSpecs;
        this.orderingType = "ALPHANUMERIC";
        this.dispatcher = new WellDispatcher();
        this.isRotated = false;
        addAllWells(dispatcher, orderingType);
	}

    /**
     * Zero arg constructor called when a plate is made via JSON. All variables will be set, but well dispatcher
     * still needs to be set up.
     */
    public Plate() {
        addAllWells(new WellDispatcher(), orderingType);
    }


	
	/**
	 * Called by the main model, draws the plate and its wells on the appropriate view.
	 * @param g - - graphics to draw on
	 * @param sF - scalefactor, how much to stretch this box based on its bounding frame
	 */
	public void paint(final Graphics g, final double sF){
        this.drawPlate(g, sF);
        //tell dispatcher to get all wells to paint
        dispatcher.notifyAll(
                new IWellCmd(){
                    public void apply(Well context, WellDispatcher disp){
                        context.paint(g, sF, isRotated);
                    }
                }
        );
	}

    /**
     * Get well's location based on input identifer
     * @param identifier String identifying the well
     * @return absolute location of the well
     */
    public Point2D getWellLocation(final String identifier) {

        //wrap the result in an arraylist to allow anonymous inner classes
        final ArrayList<Point2D> returnPoint = new ArrayList<Point2D>();

        //set a default value to return if nothing is found
        returnPoint.add(new Point2D.Double(-1,-1));
        dispatcher.notifyAll(
            new IWellCmd(){
                public void apply(Well context, WellDispatcher disp){
                    if (context.getIdentifier().equals(identifier)) {
                        //if we find a match, overwrite the default value
                        returnPoint.set(0, context.getAbsoluteLocation());
                    }
                }
            }
        );
        //if no well matches the number, tell the user and return a bad location
        if (returnPoint.get(0).equals(new Point2D.Double(-1,-1))){
            System.out.println("Could not find the well with specified number.");
        }
        return returnPoint.get(0);
    }
	
	/**
	 * Draw the bounding box for plate object (physical dimensions, defined by user), as well as its given name and
     * the well row and column labels
	 * @param g - graphics to draw on
	 * @param sF - scalefactor, how much to stretch this box based on its bounding frame
	 */
	private void drawPlate(Graphics g, double sF){
        //set up objects
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform at = new AffineTransform();

        //set up the affine transform, apply it to the graphics object. must apply in backwards-order
        at.translate(TLcorner.getX()*sF, (TLcorner.getY())*sF);
        if (isRotated) {
            at.translate(plateSpecs.getBorderDimensions().getY()*sF, 0);
            at.rotate(Math.PI/2);
        }
        at.scale(sF, sF);

        g2d.setTransform(at);
        g2d.setStroke(new BasicStroke((float) (1/sF)));
        g2d.setColor(Color.BLACK);

        //draw the border
        g2d.drawRect(0,0, (int)Math.round(plateSpecs.getBorderDimensions().getX()), (int)Math.round(plateSpecs.getBorderDimensions().getY()));

        //draw the name
        g2d.setFont(g2d.getFont().deriveFont((float) (16/sF)));
        g2d.drawString(name, 0, 0);

        //draw the row labels
        float fontSize = (float) (14/sF);
        g2d.setFont(g2d.getFont().deriveFont(fontSize));
        for (int i = 0; i < plateSpecs.getNumRows(); i++) {
            g2d.drawString(String.valueOf((char) ('A' + i)), (int) (plateSpecs.getWellCorner().getX() - plateSpecs.getWellDiameter()/2 - fontSize) ,
                    (int) (plateSpecs.getWellCorner().getY() + plateSpecs.getWellSpacing()*i + fontSize/2));
        }
        int increment = 1;
        if (plateSpecs.getNumCols() > 13) increment = 2;
        for (int i = 0; i < plateSpecs.getNumCols(); i+= increment) {
            g2d.drawString(String.valueOf(1 + i), (int) (plateSpecs.getWellCorner().getX() + plateSpecs.getWellSpacing()*i - fontSize/2),
                    (int) (plateSpecs.getWellCorner().getY() - plateSpecs.getWellDiameter()/2));
        }
	}
	
	/**
	 * Creates all wells on this plate and adds them to the plate's dispatcher.
	 */
	private void addAllWells(WellDispatcher disp, String orderingType){
        int wellIndex = 1;
        if (orderingType == "ALPHANUMERIC") {
            for (int i = 0; i < plateSpecs.getNumRows(); i++){
                for (int j = 0; j < plateSpecs.getNumCols(); j++){
                    char rowLetter = (char) (i + 65); //label rows, starting at A
                    String colNumber = Integer.toString(j + 1);
                    addWell(disp, j, i, rowLetter + colNumber, wellIndex);
                    wellIndex++;
                }
            }
        }
        else if (orderingType == "ROW") {
            for (int i = 0; i < plateSpecs.getNumRows(); i++){
                for (int j = 0; j < plateSpecs.getNumCols(); j++){
                    addWell(disp, j, i, Integer.toString(wellIndex), wellIndex);
                    wellIndex++;
                }
            }
        }
        else if (orderingType == "COLUMN") {
            for (int i = 0; i < plateSpecs.getNumCols(); i++){
                for (int j = 0; j < plateSpecs.getNumRows(); j++){
                    addWell(disp, i, j, Integer.toString(wellIndex), wellIndex);
                    wellIndex++;
                }
            }
        }
        else {
            System.out.println("Did not recognize numbering order, did not add wells.");
        }
	}
	
	/**
	 * Helper function for addAllWells, adds the specified well to the grid location.
	 * @param disp - dispatcher to add well with
	 * @param i - row the well is in
	 * @param j - column the well is in
     * @param identifier - how to identify this well
     * @param wellNumber - what number this well is on the plate
	 */
	private void addWell(WellDispatcher disp, int i, int j, String identifier, int wellNumber){
		Point2D unroundedLocation = new Point2D.Double(
				plateSpecs.getWellCorner().getX() + i*plateSpecs.getWellSpacing(),
				plateSpecs.getWellCorner().getY() + j*plateSpecs.getWellSpacing());
		disp.addObserver(new Well(this, unroundedLocation, plateSpecs.getWellDiameter(), identifier, wellNumber));
	}
	
	/**
	 * @return top left corner of the plate, in cm
	 */
	public Point2D getTLcorner(){
		return TLcorner;
	}
	
	/**
	 * @return the maximum volume per well, as defined by plate specifications
	 */
	public double getMaxWellVolume(){
		return plateSpecs.getWellVolume();
	}

    /**
     * @return this plate's name
     */
    public String getName() { return name; }

    /**
     * @return dimensions of this plate, as a rectangle
     */
    public Point2D getDimensions() {
        return plateSpecs.getBorderDimensions();
    }

    /**
     * @return plate specifications object of this plate, encapsulates all info that makes this type of plate
     */
    public PlateSpecifications getPlateSpecs() {
        return plateSpecs;
    }

    /**
     * Gets the identifiers (A1, B3, etc.) in a certain range on this well plate.
     * @param startVal start identifier
     * @param endVal end identifier
     * @param incVal how many wells to increment by each time
     * @return ArrayList of all wells included in the range
     */
    public ArrayList<String> getWellsInRange(String startVal, String endVal, int incVal) {

        if (incVal == 0) throw new IllegalArgumentException();

        int startNum = convertIdToNum(startVal);
        int endNum = convertIdToNum(endVal);

        ArrayList<String> returnList = new ArrayList<String>();
        for (int i = startNum; i <= endNum; i += incVal) {
            returnList.add(convertNumToId(i));
        }
        return returnList;
    }

    /**
     * Convert a well id (A4, C7, etc.) to a number, where A1 = 1, A2 = 2, etc. Varies depending on number of columns on
     * this plate.
     */
    public int convertIdToNum(String id) {
        int row = (int) id.charAt(0) - 65; //convert A to 0, B to 1, etc.
        int col = Integer.parseInt(id.substring(1));

        return row*plateSpecs.getNumCols() + col;
    }

    /**
     * Inverse of convertIdToNum, using a well number converts it to its id (A4, C7, etc.), which depends on this plate's
     * number of columns.
     */
    public String convertNumToId(int num) {
        num += -1;
        int rowNum = num/(plateSpecs.getNumCols());
        char rowLetter = (char) (rowNum + 65); //convert 0 to A, 1 to B, etc.
        int col = num % (plateSpecs.getNumCols()) + 1;

        return Character.toString(rowLetter) + String.valueOf(col);
    }

    /**
     * Toggles rotation of this plate.
     */
    public void toggleRotation() {
        this.isRotated = !this.isRotated;
    }
}

