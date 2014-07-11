package main.model.tasks.basictasks;

import java.io.OutputStream;

import main.model.plate.objects.ArmState;
import main.model.tasks.taskvisitors.ITaskVisitor;

/**
 * NullTask, the task that does nothing.
 * @author Christian
 *
 */
public class NullTask extends ALeafTask {

	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = 5306551209172287141L;

    /**
     * Null task, shouldn't execute anything.
     * @param armState - current position of the arm, when this task is executed
     * @param outputStream - ouput stream tasks will execute through
     */
	public void execute(ArmState armState, OutputStream outputStream) {
		
	}
	
	/**
	 * Calls the "Null" case of the given algo.
	 * @param algo The IPhraseVisitor algo to use.
	 * @param params vararg list of input parameters
	 * @return the result of running the Chord case of the visitor.
	 */
	@Override
	public Object executeVisitor(ITaskVisitor visitor, Object... params) {
		return visitor.caseAt("Null", this, params);
	}

    /**
     * When somebody changes text on JTree, check if the data is correct and, if it is, set this task's parameters
     * appropriately.
     *
     * @param object - parameters (as a String)
     */
    @Override
    public void setUserObject(Object object) {
        //Doesn't do anything, null task!
    }
	
}
