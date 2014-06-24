package model.plate;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import main.adapters.plate.Plate2TaskAdapter;
import main.adapters.plate.Plate2ViewAdapter;
import model.plate.objects.ArmState;
import model.plate.objects.BorderFrame;
import model.plate.objects.Plate;
import model.plate.objects.PlateSpecifications;
import model.plate.objects.Well;

/**
 * Model that keeps track of the current state of the frame, cell plates, and the wells themselves. Essentially,
 * keeps track of the physical world, any updates the user makes to it, and relays this between the view and
 * serial communication model.
 * @author Christian
 *
 */
public class PlateModel {

	/**
	 * Adapter from plate model to main view.
	 */
	private Plate2ViewAdapter view;
	
	/**
	 * Adapter from plate model to serial model.
	 */
	private Plate2TaskAdapter taskModel;
	
	/**
	 * All the plates that are currently on the screen.
	 */
	private ArrayList<Plate> plates;
	
	/**
	 * The border representing the area the arm can move over.
	 */
	private BorderFrame border;
	
	/**
	 * Current location of the arm, in steps.
	 */
	private ArmState armState;
	
	/**
	 * Constructor that links the model to view and other models.
	 */
	public PlateModel(){
		plates = new ArrayList<>();
	}

    /* On initialization, connects to given adapters. */
    public void start(Plate2ViewAdapter view, Plate2TaskAdapter taskModel){
        this.view = view;
        this.taskModel = taskModel;
    }
	
	/**
	 * Sets the width/length of the area our arm can move over, as well as
	 * where to draw it in the view.
	 * @param bounds - width/length of the area arm can move over
	 * @param canvas - canvas to draw the bounding box on
	 */
	public void setBorderFrame(Point2D bounds, Component canvas){
		armState = new ArmState(bounds, this);
		border = new BorderFrame(bounds, canvas);
		view.updateView();
	}
	
	/**
	 * Adds a plate to the current Iterable of plates.
     * @param name - name to refer to this well by
	 * @param numberingOrder - order to number all the wells on the plate
	 * @param platePos - where to position the (currently) top left corner of the plate
	 * @param specs - set of specifications for this particular plate, usually from data sheet
	 */
	public void addPlate(String name, String numberingOrder, Point2D platePos, PlateSpecifications specs){
		Plate plate = new Plate(name, platePos, specs, numberingOrder);
		addPlate(plate);
	}
	
	/**
	 * Adds a new plate to the internal list and updates the view so this is shown.
	 * @param plate - Plate object to add
	 */
	public void addPlate(Plate plate){
		plates.add(plate);
		view.updateView();
	}
	
	/**
	 * When the request is received from the view, draw everything necessary (border, plates, wells).
	 * @param g - graphics object to draw everything with
	 */
	public void paintAll(final Graphics g){
		final double sF = border.getScaleFactor();

		//tell the border to paint itself
		border.drawBorderFrame(g);

		//tell all plates to paint themselves
		for (int i = 0; i < plates.size(); i++){
			plates.get(i).paint(g, sF);
		}

		//tell the task model to draw whatever tasks it needs to
		taskModel.drawTasks(g, sF);
	}
	
	/**
	 * Clears all plates from the model.
	 */
	public void clearAllPlates(){
		plates.clear();
		view.updateView();
	}

	/**
	 * Find out which well was clicked on.
	 * @param point - location on screen that was clicked, in pixels
	 * @return location of well center, in cm
	 */
	public Point2D getLocationFromScreen(Point point) {
//		final Point2D clicked = new Point2D.Double(point.x/border.getScaleFactor(), point.y/border.getScaleFactor());
//		final ArrayList<Point2D> returnPoint = new ArrayList<Point2D>();
//		returnPoint.add(null);
//		dispatcher.notifyAll(
//			new IWellCmd(){
//				public void apply(Well context, WellDispatcher disp){
//					if (context.getAbsoluteLocation().distance(clicked) < context.getDiameter()/2){
//						returnPoint.set(0,context.getAbsoluteLocation());
//					}
//				}
//			}
//		);
//		return returnPoint.get(0);
        return null;
	}

    /**
     * Returns the well's location given its identifier.
     * @return absolute location of the well, in cm from origin
     */
    public Point2D getLocationFromIdentifier(String plateName, String identifier){
        //call method on input plate and return its result
        for (Plate plate : plates){
            if (plate.getName().equals(plateName))
                return plate.getWellLocation(identifier);
        }
        System.out.println("Could not find plate with specified name.");
        return null;
    }

    /**
     * @return variable encompassing current arm state
     */
    public ArmState getArmState() {
        return armState;
    }
	
	/**
	 * @return All the plates that are currently on the screen.
	 */
	public Iterable<Plate> getPlateList(){
		return plates;
	}
	
	/**
	 * @param plates - All the plates that are currently on the screen.
	 */
	public void setPlateList(Iterable<Plate> plates){
		//copy plates from other iterable over to our ArrayList
		this.plates = new ArrayList<Plate>();
		for (Plate plate : plates){
			addPlate(plate);
		}
		view.updateView();
	}
	
	/**
	 * Update the position of the internal arm tracker.
	 * @param x - x coordinate in mm
	 * @param y - y coordinate in mm
	 */
	public void setInternalPosition(double x, double y) {
		armState.setLocation(x, y);
	}
}
