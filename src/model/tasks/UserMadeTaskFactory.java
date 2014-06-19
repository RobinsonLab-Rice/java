package model.tasks;

import model.tasks.basictasks.IExecuteTask;

/**
 * Created by Christian on 6/18/2014.
 */
public class UserMadeTaskFactory implements ITaskFactory {
    /**
     * Instantiate the specific IExecuteTask for which this factory is defined.
     *
     * @return An IExecuteTask instance.
     */
    @Override
    public IExecuteTask make() {
        return null;
    }
}
