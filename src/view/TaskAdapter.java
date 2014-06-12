package view;


import java.awt.Point;

import javax.swing.tree.TreePath;

import model.tasks.ExecutionParam;
import model.tasks.SetupParam;
import model.tasks.basictasks.IExecuteTask;

/**
 * Adapter from view to the model that handles all task creation.
 * @author Christian
 *
 */
public interface TaskAdapter {

	/**
	 * Adds the task, with its parameters, source, and destination to the execution queue.
	 */
	public void addToQueue(ExecutionParam executeParams, SetupParam setupParams, String source, String destination);
	
	/**
	 * Adds the task, with its parameters, source, and destination to the execution queue.
	 */
	public void addToQueue(ExecutionParam executeParams, SetupParam setupParams, Point source, Point destination);
	
	/**
	 * Adds the task to the execution queue.
	 * @param taskToAdd - actual task to add directly to the queue
	 */
	public void addToQueue(IExecuteTask taskToAdd);
	
	/**
	 * Clears all stages from the model.
	 */
	public void clearAllTasks();
	
	/**
	 * Executes all stages, in order.
	 */
	public void executeAll();
	
	/**
	 * For debug purposes, execute all stages at once.
	 */
	public void debugExecuteAll();
	
	/**
	 * Add a single well task to the execution queue.
	 * @param wellNum - well number to move to
	 * @param fluidAmount - amount of fluid to move
	 */
	public void addSingleWellTask(int wellNum, int fluidAmount);
	
	/**
	 * Gets the full execution of tasks to slap into GUI tree.
	 * @return IExecuteTask MultiTask of what is going to be executed, so we can visualize it
	 */
	public IExecuteTask getTasks();
	
	public void changeExecutionData(Object[] path, String newData);
	
	public void deleteExecutionTask(Object[] path);
	
	public void insertAfterSelected(Object[] path, IExecuteTask taskToAdd);
	
}
