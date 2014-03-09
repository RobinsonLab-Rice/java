package model.serialization;

import model.tasks.basictasks.MultiTask;

/**
 * Adapter from serialization model to the task model.
 * 
 * @author christianhenry
 */
public interface TaskAdapter {

	public MultiTask getTasks();
	
	public void setTasks(MultiTask taskQueue);
	
}
