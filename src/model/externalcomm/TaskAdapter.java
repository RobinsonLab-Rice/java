package model.externalcomm;

import model.tasks.basictasks.IExecuteTask;

/**
 * Adapter for the external model to talk to the task model.
 * @author Christian
 *
 */
public interface TaskAdapter {
	
	public void appendTaskToQueue(IExecuteTask taskToAdd);
	
}
