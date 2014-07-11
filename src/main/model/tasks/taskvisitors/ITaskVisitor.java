package main.model.tasks.taskvisitors;

import main.model.tasks.basictasks.IExecuteTask;

/**
 * Interface for a task visitor.
 * @author Christian
 *
 */
public interface ITaskVisitor {
	/**
	 * Called by the host to run the appropriate processing for that host.
	 * @param id The id of the host
	 * @param host The host itself
	 * @param params vararg list of input parameters that the algo might use.
	 * @return the result of running the associated processing for the host.
	 */
	public Object caseAt(String id, IExecuteTask host, Object...params);
}
