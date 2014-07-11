package main.model.tasks.basictasks;

import java.io.OutputStream;

import main.model.plate.objects.ArmState;
import main.model.tasks.taskvisitors.ITaskVisitor;
import main.util.Parser;

/**
 * Lower task, tells the nozzle servo to lower by the specified amount.
 * @author Christian
 */
public class NozzleHeightTask extends ALeafTask {

	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = -1956176933458574472L;
	
	private String heightToSet;
	
//	/**
//	 * Constructor, just sets the appropriate volume.
//	 * @param heightToSet - height (in cm) nozzle should be set to
//	 */
//	public NozzleHeightTask(Double heightToSet){
//		this.heightToSet = heightToSet;
//	}

    /**
     * @param heightToSet height this task moves nozzle to, can be string of a double or a variable
     */
    public NozzleHeightTask(String heightToSet) {
        this.heightToSet = heightToSet;
    }

	/**
	 * When a nozzle height task is executed, it just sends a message to the microcontroller telling it to
	 * move to a certain height, which is saved to this task in its constructor.
     * TODO: work with device to change heightToSet to be height and not servo microseconds
	 */
	public void execute(ArmState armState, OutputStream outputStream) {

        Double heightDouble = null;

        //if heightToSet is still a variable, return without executing
        if (!Parser.isNumeric(heightToSet)) {
            return;
        }
        else {
            heightDouble = Double.parseDouble(heightToSet);
        }

		String cmdString = "";
		if (heightDouble > 0){
			cmdString = "nozzleHeight(" + "1250" + ")";
		}
		else if (heightDouble < 0){
			cmdString = "nozzleHeight(" + "1500" + ")";
		}
		this.writeString(cmdString, outputStream);
	}

    /**
     * @param visitor The visitor to execute
     * @param params The input parameters supplied to the visitor when its appropriate case is called.
     * @return
     */
	@Override
	public Object executeVisitor(ITaskVisitor visitor, Object... params) {
		return visitor.caseAt("NozzleHeight", this, params);
	}

    /**
     * When somebody changes text on JTree, check if the data is correct and, if it is, set this task's parameters
     * appropriately.
     *
     * @param object - parameters (as a String)
     */
    @Override
    public void setUserObject(Object object) {
        heightToSet = (String) object;
    }

    /**
     * Replace variable in this task and all its children.
     *
     * @param variable - if the task's "heightToSet" matches this, change its value to newValue
     * @param newValue - new value to change to
     */
    @Override
    public void replaceAll(String variable, Object newValue) {
        if (variable.equals(heightToSet)) {
            heightToSet = (String) newValue;
        }
    }

    /**
     * Replace variable in this task and all its children.
     *
     * @param variable - if the task's "heightToSet" matches this, change its value to newValue
     * @param newValue - new value to change to
     */
    @Override
    public boolean replaceOne(String variable, Object newValue) {
        if (variable.equals(heightToSet)) {
            heightToSet = (String) newValue;
            return true;
        }
        else {
            return false;
        }
    }
}