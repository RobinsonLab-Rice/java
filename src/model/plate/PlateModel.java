package model.plate;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
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


    private ArrayList<Well> selectedWells = new ArrayList<Well>();
	
	/**
	 * Constructor that links the model to view and other models.
	 */
	public PlateModel(){
		plates = new ArrayList<Plate>();
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
	 * @return well located on input point
	 */
	public Well getWellFromScreenLocation(Point point) {
		final Point2D clicked = new Point2D.Double(point.x/border.getScaleFactor(), point.y/border.getScaleFactor());
		final ArrayList<Well> returnPoint = new ArrayList<Well>();
		returnPoint.add(null);
        //go through all plates' dispatcher and see if any wells fall in clicked point
        for (Plate plate : plates) {
            plate.dispatcher.notifyAll(
                new IWellCmd(){
                    public void apply(Well context, WellDispatcher disp){
                        if (context.getAbsoluteLocation().distance(clicked) < context.getDiameter()/2){
                            returnPoint.set(0, context);
                        }
                    }
                }
            );
        }
		return returnPoint.get(0);
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

    /**
     * When mouse is clicked, dragged, and released, parse the location and modifiers of the two locations to
     * select wells appropriately.
     * @param start
     * @param end
     */
    public void clickAndDrag(MouseEvent start, MouseEvent end) {

        Well startWell = getWellFromScreenLocation(start.getPoint());
        Well endWell = getWellFromScreenLocation(end.getPoint());

        //if neither location was on a well, do nothing
        if (startWell == null || endWell == null) return;
        //if start and end well are the same, count this click and drag as a regular mouseClicked
        else if (startWell == endWell) selectWell(end, endWell);
        /**
         * Else, they are different wells and we should make a task for movement between them.
         */
        else {
            ArrayList<Double> dispenseAmounts = new ArrayList<Double>();

            //add in option for user to reverse the flow of liquid by holding down alt
            boolean shouldReverse = false;
            if (end.isAltDown()) shouldReverse = true;

            //also if ctrl/cmd was held down on this drag, popup the dialog for a custom move amount. in the process,
            //change dispenseAmounts accordingly
            if (end.isControlDown() || end.isMetaDown()) {
                //TODO: ask GUI for user input on new dispense amounts
            }

            /**
             * There are a few cases for actually moving:
             * 1) start well was not selected, so we just move that single well to the single end well
             * 2) start well was selected, so we make moves for ALL selected wells to the end well
             */
            if (!startWell.isSelected) {
                ArrayList<Well> startWellWrapper = new ArrayList<Well>();
                startWellWrapper.add(startWell);
                taskModel.makeMoveTask(startWellWrapper, endWell, dispenseAmounts, shouldReverse);
            }
            else taskModel.makeMoveTask(selectedWells, endWell, dispenseAmounts, shouldReverse);
        }
        view.updateView();
    }

    /**
     * When mouse is clicked, parse the location and modifiers to select wells appropriately.
     * @param e MouseEvent containing all mouse info
     */
    public void selectWell(MouseEvent e, Well justSelected) {

        //if click was a ctrl-click, toggle the well's selection
        if (e.isMetaDown() || e.isControlDown()) {
            doCtrlClick(justSelected);
        }
        //remove all wells but the last one, select all wells between last one and selected one
        else if (e.isShiftDown()) {
            doShiftClick(justSelected);
        }
        //neither down, treat it as a regular click, deleting all selected wells and adding this single one
        else {
            doRegularClick(justSelected);
        }
        view.updateView(); //regardless of what happened, update the visualization
    }

    /**
     * Perform necessary operations for clicking with ctrl or command pressed down.
     * @param justSelected - well that was just selected by the user
     */
    public void doCtrlClick(Well justSelected) {
        if (justSelected.isSelected) {
            justSelected.isSelected = false;
            selectedWells.remove(justSelected);
        }
        else {
            justSelected.isSelected = true;
            selectedWells.add(justSelected);
        }
    }

    /**
     * Perform necessary operations for clicking with shift pressed down.
     * @param justSelected - well that was just selected by the user
     */
    public void doShiftClick(final Well justSelected) {
        //if no wells are currently selected, just select all up to the selected one
        if (selectedWells.isEmpty()) {
            justSelected.getParentPlate().dispatcher.notifyAll(
                new IWellCmd(){
                    public void apply(Well context, WellDispatcher disp){
                        if (context.getWellNumber() <= justSelected.getWellNumber()) {
                            selectedWells.add(context);
                            context.isSelected = true;
                        }
                    }
                }
            );
        }
        else {
            /**
             * If we have selected wells already, de-select them all and select those between just-selected one and
             * last one selected previously.
             */

            //de-select all and save last one
            final Well lastWell = deselectAllWells();

            //select all in between them
            justSelected.getParentPlate().dispatcher.notifyAll(
                new IWellCmd(){
                    public void apply(Well context, WellDispatcher disp){
                        if (justSelected.getWellNumber() < lastWell.getWellNumber()){ //just-selected well is smaller
                            //add well in if its number is after just-selected and before last selected
                            if (justSelected.getWellNumber() <= context.getWellNumber()  && context.getWellNumber() <= lastWell.getWellNumber()){
                                context.isSelected = true;
                                selectedWells.add(context);
                            }
                        }
                        else { //last previously selected well is smaller
                            //check opposite
                            if (lastWell.getWellNumber() <= context.getWellNumber()  && context.getWellNumber() <= justSelected.getWellNumber()){
                                context.isSelected = true;
                                selectedWells.add(context);
                            }
                        }
                    }
                }
            );
        }
    }

    /**
     * Perform necessary operations for clicking without any modifiers.
     * @param justSelected - well that was just selected by the user
     */
    public void doRegularClick(Well justSelected) {
        deselectAllWells();
        //add the clicked well
        justSelected.isSelected = true;
        selectedWells.add(justSelected);
    }

    /**
     * De-select all wells, returning the last one in the arraylist.
     *
     * @return last well to be removed
     */
    public Well deselectAllWells() {
        Well lastWell = null;
        for (Well savedWell : selectedWells) {
            savedWell.isSelected = false;
            lastWell = savedWell;
        }
        selectedWells.clear();
        return lastWell;
    }
}
