package model.externalcomm;

/**
 * Adapter for the external model to talk to the task model.
 * @author Christian
 *
 */
public interface TaskAdapter {

	/**
	 * Executes stage based on input stage number.
	 */
	public void executeStage(int stageNumber);
	
	public void addTaskAndExecute();
}
