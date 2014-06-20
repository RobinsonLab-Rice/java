package main.adapters.view;

import model.tasks.ExecutionParam;
import model.tasks.ITaskFactory;
import model.tasks.SetupParam;
import model.tasks.TaskModel;
import model.tasks.basictasks.IExecuteTask;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;

/**
 * Created by Christian on 6/12/2014.
 */
public class View2TaskAdapter {

    TaskModel taskModel;

    /* Sets up model references necessary for this adapter. */
    public View2TaskAdapter(TaskModel taskModel) { this.taskModel = taskModel; }

    /**
     * Adds the task, with its parameters, source, and destination to the execution queue.
     *
     * @param executeParams
     * @param setupParams
     * @param source
     * @param destination
     */
    public void addToQueue(ExecutionParam executeParams, SetupParam setupParams, String source, String destination) {

    }

    /**
     * Adds the task, with its parameters, source, and destination to the execution queue.
     *
     * @param executeParams
     * @param setupParams
     * @param source
     * @param destination
     */
    public void addToQueue(ExecutionParam executeParams, SetupParam setupParams, Point source, Point destination) {

    }

    /**
     * Adds the task to the execution queue.
     *
     * @param taskToAdd - actual task to add directly to the queue
     */
    public void addToQueue(IExecuteTask taskToAdd) {

    }

    /**
     * Clears all stages from the model.
     */
    public void clearAllTasks() {

    }

    /**
     * Executes all stages, in order.
     */
    public void executeAll() {

    }

    /**
     * For debug purposes, execute all stages at once.
     */
    public void debugExecuteAll() {
        taskModel.debugExecuteAll();
    }

    /**
     * Add a single well task to the execution queue.
     *
     * @param wellNum     - well number to move to
     * @param fluidAmount - amount of fluid to move
     */
    public void addSingleWellTask(int wellNum, int fluidAmount) {

    }

    /**
     * Gets the full execution of tasks to slap into GUI tree.
     *
     * @return IExecuteTask MultiTask of what is going to be executed, so we can visualize it
     */
    public IExecuteTask getTasks() {
        return null;
    }

    public void changeExecutionData(Object[] path, String newData) {

    }

    public void deleteExecutionTask(Object[] path) {

    }

    public void insertAfterSelected(Object[] path, IExecuteTask taskToAdd) {

    }

    /**
     * Get all premade and/or user saved task factories.
     */
    public Iterable<ITaskFactory> getTaskFactories() {
        return taskModel.getTaskFactories();
    }

    public DefaultTreeModel getTreeModel() {
        return taskModel.getTreeModel();
    }
}
