package main.model.tasks.basictasks;

import java.awt.geom.Point2D;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

import main.model.plate.objects.ArmState;
import main.model.plate.objects.Well;
import main.model.tasks.taskvisitors.ITaskVisitor;
import main.util.Parser;

/**
 * Move task, tells the arm to move to specified well.
 *
 * @author Christian
 *
 */
public class MoveToWellTask extends ALeafTask {
	
	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = 8216224355050783969L;

    /**
     * Plate name that the well is on.
     */
    private String plate;

    /**
     * Well number to move to. This is parsed when the command is executed, so could point to different locations
     * based on the experiment.
     */
	private String identifier;

    /**
     * Sets well identifier to move to.
     * @param plate - plate to move to
     * @param identifier - identifier for well to move to (could be its number, alphanumeric, etc.)
     */
	public MoveToWellTask(String plate, String identifier) {
        this.plate = plate;
		this.identifier = identifier;
	}

    /**
     * Allow user to move to a specific well object, info will be generated.
     * @param destination - well object to move to
     */
    public MoveToWellTask(Well destination) {
        this.plate = destination.getParentPlate().getName();
        this.identifier = destination.getIdentifier();
    }

	/**
	 * When a move task is executed, it compares its destination to the current arm location, sending the difference (in cm)
	 * through the output stream.
	 */
	public void execute(ArmState armState, OutputStream outputStream) {
        Point2D destination = null;

        //if the destination identifier is not correct (i.e. is still a variable) do nothing
        if (!Parser.isIdentifier(identifier)){
            return;
        }
        else{
            //find out what absolute location the saved plate and well correspond to
            destination = armState.getPlateModel().getLocationFromIdentifier(plate, identifier);
        }

		double xCmToMove = armState.getX() - destination.getX();
		double yCmToMove = armState.getY() - destination.getY();
		
		//round this result using BigDecimals to send over only 2 decimal places
		xCmToMove = (new BigDecimal(xCmToMove).setScale(2, BigDecimal.ROUND_HALF_DOWN)).doubleValue();
		yCmToMove = (new BigDecimal(yCmToMove).setScale(2, BigDecimal.ROUND_HALF_DOWN)).doubleValue();
		
		//construct and send the command
		String cmdString = "move(" + xCmToMove + "," + yCmToMove + ")";
		this.writeString(cmdString, outputStream);
		
		//update the arm location, making sure to only save it to 2 decimal places.
		BigDecimal roundedX = new BigDecimal(armState.getX() - xCmToMove).setScale(2, BigDecimal.ROUND_HALF_DOWN);
		BigDecimal roundedY = new BigDecimal(armState.getY() - yCmToMove).setScale(2, BigDecimal.ROUND_HALF_DOWN);
		armState.setLocation(roundedX.doubleValue(), roundedY.doubleValue());
		
	}
	
	/**
	 * Calls the "Move" case of the given algo.
	 * @param visitor The IPhraseVisitor algo to use.
	 * @param params vararg list of input parameters
	 * @return the result of running the Chord case of the visitor.
	 */
	@Override
	public Object executeVisitor(ITaskVisitor visitor, Object... params) {
		return visitor.caseAt("MoveToWell", this, params);
	}

    /**
     * Replace variable in this task and all its children.
     *
     * @param variable - if the move task's "identifier" matches this, change value of identifier to newValue
     * @param newValue - new value to change to
     */
    @Override
    public void replace(String variable, Object newValue) {
        if (variable.equals(identifier)){
            identifier = (String) newValue;
        }
    }

    /**
     * @return arm location after executing this task
     */
    public ArrayList<String> getDestination() {
        ArrayList<String> destination = new ArrayList<String>();
        destination.add(plate);
        destination.add(identifier);
        return destination;
    }
	
	/**
	 * Define how this task should be printed.
	 */
	public String toString() {
		return "Move to: plate = " + plate + ", well = " + identifier;
	}

    /**
     * When somebody changes text on JTree, check if the data is correct and, if it is, set this task's parameters
     * appropriately.
     *
     * @param object - parameters (as a String)
     */
    @Override
    public void setUserObject(Object object) {

        //parse the input, making sure they input 2 values separated by a comma
        String[] halves = ((String) object).split(",");
        if (halves.length != 2) {
            System.out.println("Did not input proper format for moving to a well.");
            return;
        }
        try {
            plate = halves[0].trim();
            identifier = halves[1].trim();
        } catch (NumberFormatException e) {
            System.out.println("Did not input proper format for moving to a well.");
            e.printStackTrace();
        }
    }
}