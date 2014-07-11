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
	
	/**
	 * Volume of liquid to move, currently the time in ms before switching to air.
	 */
	private Double volume = null;

    /**
     * Variable to store if volume is null.
     */
    private String variable = null;
	
	/**
	 * @param volume - amount of volume to fill associated well with
	 */
	public DispenseTask(Double volume){
        this.volume = volume;
        this.variable = null;
	}

    /**
     * @param variable - variable to later overwrite with volume
     */
    public DispenseTask(String variable) {
        this.volume = null;
        this.variable = variable;
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
     * @return amount to dispense
     */
    public double getVolume() {
        return volume;
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

        //if input is a number, set the item normally.
        if (Parser.Singleton.isNumeric(input)) {
            try {
                this.volume = Double.parseDouble((String) object);
            } catch (NumberFormatException e) {
                System.out.println("Tried to change dispense something that wasn't a double.");
                e.printStackTrace();
            }
        }
        //else, input is a string and we should set the variable to later be filled in
        else {
            this.variable = input;
        }
    }

    /**
     * If variable matches the stored variable, replace dispense value with newValue.
     * @param variable - if the task's "variable" matches this, change value
     * @param newValue - new value of dispense
     */
    public void replace(String variable, Object newValue) {
        if (this.variable.equals(variable)) {
            volume = (double) newValue;
        }
    }
}
