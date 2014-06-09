package model.tasks.basictasks;

import java.io.OutputStream;

import model.plate.objects.ArmState;
import model.tasks.ITaskVisitor;

/**
 * NullTask, the task that does nothing.
 * @author Christian
 *
 */
public class NullTask extends ASerialTask{

	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = 5306551209172287141L;

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
	 * Null task, do nothing.
	 */
	@Override
	public void traverseOrModify(Object[] taskPath, String toChange) {
		// TODO Auto-generated method stub
		
	}
	
}
