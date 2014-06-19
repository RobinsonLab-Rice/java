package model.tasks.basictasks;

import java.awt.geom.Point2D;
import java.io.OutputStream;
import java.math.BigDecimal;

import model.plate.PlateModel;
import model.plate.objects.ArmState;
import model.tasks.ITaskVisitor;

/**
 * Move task, tells the arm to move to the context well at its specified speed.
 * @author Christian
 *
 */
public class MoveTask extends ALeafTask {
	
	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = 8216224355050783969L;
	
	private Point2D destination;
	
	int wellNum;
	
	/**
	 * Constructor, takes in where the arm should move to.
	 * @param destination - where to move the arm (in cm)
	 */
	public MoveTask(Point2D destination){
		this.destination = destination;
		PlateModel.getLocationFromNumber(wellNum);
	}
	
	public MoveTask(int wellNum) {
		this.wellNum = wellNum;
		destination = PlateModel.getLocationFromNumber(wellNum);
	}

	/**
	 * When a move task is executed, it compares its destination to the current arm location, sending the difference (in cm)
	 * through the output stream.
	 */
	public void execute(ArmState armState, OutputStream outputStream) {
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
		return visitor.caseAt("Move", this, params);
	}

	/**
	 * This is a leaf task: the task path should contain only the move task,
	 * so we can freely parse and change move parameters according to the toChange String.
	 */
	@Override
	public void traverseOrModify(Object[] taskPath, String toChange) {
		System.out.println(taskPath + ":" + taskPath.length);
		if (taskPath.length != 1){
			System.out.println("Something went wrong with move task, length of path is not 1.");
		}
		else {
			//parse the string for a colon, set volume to be the double after the colon
			wellNum = Integer.parseInt(toChange.split(":")[1]);
			destination = PlateModel.getLocationFromNumber(wellNum);
		}
	}
	
	/**
	 * Define how this task should be printed.
	 */
	public String toString() {
		return "Move:" + wellNum;
	}

    //TODO: REMOVE THIS TEST!!!!!!//
    public void changeData(int newDestination) {
        wellNum = newDestination;
    }
}