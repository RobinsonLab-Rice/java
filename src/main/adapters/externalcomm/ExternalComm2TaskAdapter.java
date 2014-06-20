package main.adapters.externalcomm;

import model.externalcomm.ExternalCommModel;
import model.tasks.TaskModel;
import model.tasks.basictasks.IExecuteTask;

/**
 * Adapter for the external model to talk to the task model.
 * @author Christian
 *
 */
public class ExternalComm2TaskAdapter {

    private TaskModel taskModel;

    /* Sets up model references necessary for this adapter. */
    public ExternalComm2TaskAdapter(TaskModel taskModel) { this.taskModel = taskModel; }

    public void appendTaskToQueue(IExecuteTask taskToAdd){
        //taskModel.addToQueue(taskToAdd);
    }
	
}
