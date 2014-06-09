package view;


import java.awt.Point;

import model.tasks.ExecutionParam;
import model.tasks.SetupParam;

/**
 * Adapter from view to the model that handles all task creation.
 * @author Christian
 *
 */
public interface TaskAdapter<TFactoryItem> {

	/**
	 * Adds the task, with its parameters, source, and destination to the execution queue.
	 */
	public void addToQueue(ExecutionParam executeParams, SetupParam setupParams, String source, String destination);
	
	/**
	 * Adds the task, with its parameters, source, and destination to the execution queue.
	 */
	public void addToQueue(ExecutionParam executeParams, SetupParam setupParams, Point source, Point destination);
	
	/**
	 * Adds a stage to the task model.
	 * @return total number of stages after the addition
	 */
	public int addStage();
	
	/**
	 * Clears all stages from the model.
	 */
	public void clearAllStages();
	
	/**
	 * Executes the stage specified by input number.
	 * @param stageNumber - number of the stage to execute
	 */
	public void executeStage(int stageNumber);
	
	/**
	 * Executes all stages, in order.
	 */
	public void executeAllStages();
	
	/**
	 * @param stageNumber - int to set the current stage number to
	 */
	public void setCurrentStage(int stageNumber);
	
	/**
	 * Add a single well task to the execution queue.
	 * @param wellNum - well number to move to
	 * @param fluidAmount - amount of fluid to move
	 */
	public void addSingleWellTask(int wellNum, int fluidAmount);
}
