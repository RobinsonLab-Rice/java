package model.tasks.taskvisitors;

import model.tasks.basictasks.IExecuteTask;

/**
 * The command used by ATaskVisitor that is associated with a host or hosts.
 * @author Christian
 *
 */
public interface ITaskVisitorCmd {

	/**
	 * The method called when the APhraseVisitor delegates its caseAt call to this command.
	 * @param id  the id of the host
	 * @param host the host itself
	 * @param params vararg list of input parameters.
	 * @return the result of this processing of the host.
	 */
	public Object apply(String id, IExecuteTask host, Object... params);
}
