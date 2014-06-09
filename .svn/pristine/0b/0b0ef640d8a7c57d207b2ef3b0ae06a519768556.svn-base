package model.tasks.basictasks;

import java.io.OutputStream;

import model.plate.objects.ArmState;
import model.tasks.ITaskVisitor;

/**
 * Lower task, tells the nozzle servo to lower by the specified amount.
 * @author Christian
 */
public class LowerTask extends ASerialTask {

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
	 * Lower task doesn't have any parameters, do nothing.
	 */
	@Override
	public void traverseOrModify(Object[] taskPath, String toChange) {
	}
	
	/**
	 * Define how this task should be printed.
	 */
	public String toString() {
		return "Lower";
	}
}