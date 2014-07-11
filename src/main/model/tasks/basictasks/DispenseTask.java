package main.model.tasks.basictasks;

import java.io.OutputStream;

import main.model.plate.objects.ArmState;
import main.model.tasks.taskvisitors.ITaskVisitor;
import main.util.Parser;

/**
 * Dispense task, tells the robot to dispense the input amount of liquid.
 * @author Christian
 */
public class DispenseTask extends ALeafTask {

	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = 919292764151689689L;
	
//	/**
//	 * Volume of liquid to move, currently the time in ms before switching to air.
//	 */
//	private Double volume = null;

    /**
     * Variable to store if volume is null.
     */
    private String volume = null;

    /**
     * @param volume - amount to dispense, as a string to be parsed when this task needs to be worked with
     */
    public DispenseTask(String volume) {
        this.volume = volume;
    }

	/**
	 * Called when the dispense task needs to execute. Sends a command to the Arduino
	 * with time to flow liquid and time to flow air, in ms.
	 */
	public void execute(ArmState armState, OutputStream outputStream) {
		String cmdString = "dispense(" + volume + ")";
		this.writeString(cmdString, outputStream);
	}
	
	/**
	 * Calls the "Dispense" case of the given algo.
	 * @param algo The IPhraseVisitor algo to use.
	 * @param params vararg list of input parameters
	 * @return the result of running the visitor
	 */
	@Override
	public Object executeVisitor(ITaskVisitor algo, Object... params) {
	    return algo.caseAt("Dispense", this, params);
	}

    /**
     * @return amount to dispense. if the amount is still a variable, return 0
     */
    public double getVolume() {
        //if volume is a number, return that number
        if (Parser.Singleton.isNumeric(volume)) {
            return Double.parseDouble(volume);
        }
        else return 0;
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
        String input = (String) object;

        this.volume = input;
    }

    /**
     * If variable matches the stored dispense string, replace dispense value with newValue.
     * @param variable - if the task's volume matches this, change volume to newValue
     * @param newValue - new value of dispense
     */
    public void replaceAll(String variable, Object newValue) {
        if (volume.equals(variable)) {
            volume = (String) newValue;
        }
    }

    /**
     * If variable matches the stored dispense string, replace dispense value with newValue.
     * @param variable - if the task's volume matches this, change volume to newValue
     * @param newValue - new value of dispense
     */
    public boolean replaceOne(String variable, Object newValue) {
        if (volume.equals(variable)) {
            volume = (String) newValue;
            return true;
        }
        else {
            return false;
        }
    }
}
