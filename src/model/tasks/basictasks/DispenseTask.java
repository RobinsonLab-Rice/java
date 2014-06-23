package model.tasks.basictasks;

import java.io.OutputStream;

import model.plate.objects.ArmState;
import model.tasks.ITaskVisitor;

/**
 * Dispense task, tells the robot to dispense the input amount of liquid.
 * @author Christian
 */
public class DispenseTask extends ALeafTask {

	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = 919292764151689689L;
	
	/**
	 * Volume of liquid to move, currently the time in ms before switching to air.
	 */
	private double volume;
	
	/**
	 * @param inputVolume - amount of volume to fill associated well with
	 */
	public DispenseTask(Double volume){
		this.volume = volume;
	}
	
	/**
	 * @param inputVolume - amount of volume to fill associated well with
	 */
	public DispenseTask(int volume){
		this.volume = volume;
	}

	/**
	 * Called when the dispense task needs to execute. Sends a command to the Arduino
	 * with time to flow liquid and time to flow air, in ms.
	 */
	public void execute(ArmState armState, OutputStream outputStream) {
		//check to see if it will overflow, notify if so
		if (armState.willOverflow(volume)){
			//System.out.println("Well might overflow from " + volume + " uL!");
		}
		
		//for now, send the command regardless
		String cmdString = "dispense(" + volume + ")";
		this.writeString(cmdString, outputStream);
		
		//update internal tracker
		armState.updateWellVolume(volume);
	}
	
	/**
	 * Calls the "Dispense" case of the given algo.
	 * @param algo The IPhraseVisitor algo to use.
	 * @param params vararg list of input parameters
	 * @return the result of running the Chord case of the visitor.
	 */
	@Override
	public Object executeVisitor(ITaskVisitor algo, Object... params) {
	    return algo.caseAt("Dispense", this, params);
	}
	
	public String toString() {
		return "Dispense:" + volume;
	}

    /**
     * When somebody changes text on JTree, check if the data is correct and, if it is, set this task's parameters
     * appropriately.
     *
     * @param object - parameters (as a String)
     */
    @Override
    public void setUserObject(Object object) {
        //if object can't be converted to double, throw an exception
        try {
            this.volume = Double.parseDouble((String) object);
        } catch (NumberFormatException e) {
            System.out.println("Tried to change dispense something that wasn't a double.");
            e.printStackTrace();
        }
    }

//	/**
//	 * This is a leaf task: the task path should contain only the dispense task,
//	 * so we can freely parse and change volume according to the toChange String.
//	 */
//	@Override
//	public void traverseOrModify(Object[] taskPath, String toChange) {
//		System.out.println(taskPath + ":" + taskPath.length);
//		if (taskPath.length != 1){
//			System.out.println("Something went wrong with dispense task, length of path is not 1.");
//		}
//		else {
//			//parse the string for a colon, set volume to be the double after the colon
//			volume = Double.parseDouble(toChange.split(":")[1]);
//		}
//	}
}
