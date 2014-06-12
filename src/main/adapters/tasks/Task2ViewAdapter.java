package main.adapters.tasks;

import model.tasks.basictasks.MultiTask;
import view.MainPanel;

/**
 * Adapter for the task model to talk to main view.
 * @author Christian
 *
 */
public class Task2ViewAdapter {

    private MainPanel view;

    /* Sets up model references necessary for this adapter. */
    public Task2ViewAdapter(MainPanel view) { this.view = view; }

    public void updateView(){
        view.update();
    }

	public void setTask(MultiTask taskQueue){
        view.setTask(taskQueue);
    }
	
}
