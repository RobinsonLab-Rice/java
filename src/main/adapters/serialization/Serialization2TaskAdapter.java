package main.adapters.serialization;

import model.tasks.TaskModel;
import model.tasks.basictasks.MultiTask;

/**
 * Adapter from serialization model to the task model.
 * 
 * @author christianhenry
 */
public class Serialization2TaskAdapter {

    private TaskModel taskModel;

    /* Sets up model references necessary for this adapter. */
    public Serialization2TaskAdapter(TaskModel taskModel) {
        this.taskModel = taskModel;
    }

    public MultiTask getTasks(){
        return taskModel.getTasks();
    }
	
	public void setTasks(MultiTask taskQueue){
        //taskModel.setTasks(taskQueue);
    }
	
}
