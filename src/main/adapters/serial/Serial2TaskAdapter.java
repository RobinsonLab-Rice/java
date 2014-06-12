package main.adapters.serial;

import model.tasks.TaskModel;

/**
 * Adapter that allows the serial model to talk to the task model through specified methods.
 * @author Christian
 *
 */
public class Serial2TaskAdapter {

    private TaskModel taskModel;

    /* Sets up model references necessary for this adapter. */
    public Serial2TaskAdapter(TaskModel taskModel) { this.taskModel = taskModel; }

    /**
	 * When the serial model receives word that the arm completed the previous task, start the new one.
	 */
	public void executeNext(){

    }
}
