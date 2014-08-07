package main.model.tasks.basictasks;

import java.io.OutputStream;
import java.io.Serializable;

import main.model.plate.objects.ArmState;
import main.model.tasks.taskvisitors.ITaskVisitor;

import javax.swing.tree.MutableTreeNode;

/**
 * Defines what task the robot should perform when attached to a Well context object.
 * @author Christian
 *
 */
public interface IExecuteTask extends Serializable, MutableTreeNode {
	
	/**
	 * Executes everything this task contains. If it is a higher level task, this
	 * executes the sub-level tasks. If it is a lower task, it will actually participate
	 * in some form of serial communication with the Arduino.
	 * @param armState - current position of the arm, when this task is executed
	 * @param outputStream - ouput stream tasks will execute through
	 */
	public void execute(ArmState armState, OutputStream outputStream);
	
	/**
	   * Executes (accepts) the visitor, calling the case associated with this host's index value.
	   * @param visitor The visitor to execute
	   * @param params The input parameters supplied to the visitor when its appropriate case is called.
	   * @return The return value from executing the appropriate case on the visitor.
	   */
	public Object executeVisitor(ITaskVisitor visitor, Object... params);

    /**
     * Reset the parents of this tree of tasks, useful for reloading tasks from JSON.
     */
    public void resetParents();

    /**
     * Sets this task (and its children, if it has them) to be visible/hidden on the GUI.
     * @param isVisible true if task should be shown, false if not
     */
    public void setVisibilityDown(boolean isVisible);

    /**
     * Sets this task (and its parents, if it has them) to be visible/hidden on the GUI.
     * @param isVisible true if task should be shown, false if not
     */
    public void setVisibilityUp(boolean isVisible);

    /**
     * @return true if this task is visible to the user, false otherwise
     */
    public boolean getVisibility();

    /**
     * Replace variable in this task and all its children.
     * @param variable - if the task's "variable" matches this, change value
     * @param newValue - new value to change to
     */
    public void replaceAll(String variable, Object newValue);

    /**
     * Replace first occurence of variable in this task and its children, returning as soon as it does.
     * @param variable - if the task's "variable" matches this, change value
     * @param newValue - new value to change to
     * @return whether or not the value was replaced in this task
     */
    public boolean replaceOne(String variable, Object newValue);
}
