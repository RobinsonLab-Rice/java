package model.tasks;

import model.tasks.basictasks.MultiTask;

/**
 * Adapter for the task model to talk to main view.
 * @author Christian
 *
 */
public interface ViewAdapter {

	public void updateView();

	public void setTask(MultiTask taskQueue);
	
}
