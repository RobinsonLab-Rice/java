package main.model.tasks;

import main.model.tasks.basictasks.IExecuteTask;

/**
 * Interface defining factories that will make IExecuteTasks.
 * @author Christian
 *
 */
public interface ITaskFactory {
	/**
     * Instantiate the specific IExecuteTask for which this factory is defined.
     * @return An IExecuteTask instance.
     */
	public IExecuteTask make();

    /**
     * Define how to print this task factory.
     * @return
     */
    public String toString();
}
