package main.model.tasks.basictasks;

import java.io.OutputStream;

import main.model.plate.objects.ArmState;
import main.model.tasks.taskvisitors.ITaskVisitor;

/**
 * Lower task, tells the nozzle servo to lower by the specified amount.
 * @author Christian
 */
public class LowerTask extends ALeafTask {

	private static final long serialVersionUID = 3963417044553710729L;

	/**
	 * Constructor, just sets the appropriate volume.
	 * @param heightToSet - height (in cm) nozzle should be set to
	 */
	public LowerTask(){
	}

	/**
	 * When a nozzle height task is executed, it just sends a message to the microcontroller telling it to
	 * move to a certain height, which is saved to this task in its constructor.
	 */
	public void execute(ArmState armState, OutputStream outputStream) {
		String cmdString = "nozzleHeight(" + "1325" + ")";
		this.writeString(cmdString, outputStream);
	}
	
	/**
	 * Calls the "Lower" case of the given algo.
	 * @param algo The IPhraseVisitor algo to use.
	 * @param params vararg list of input parameters
	 * @return the result of running the Chord case of the visitor.
	 */
	@Override
	public Object executeVisitor(ITaskVisitor visitor, Object... params) {
		return visitor.caseAt("Lower", this, params);
	}
	
	/**
	 * Define how this task should be printed.
	 */
	public String toString() {
		return "Lower";
	}

    /**
     * When somebody changes text on JTree, check if the data is correct and, if it is, set this task's parameters
     * appropriately.
     *
     * @param object - parameters (as a String)
     */
    @Override
    public void setUserObject(Object object) {
        //Don't allow somebody to change this task.
    }
}