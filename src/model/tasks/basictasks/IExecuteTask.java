package model.tasks.basictasks;

import java.io.OutputStream;
import java.io.Serializable;

import model.plate.objects.ArmState;
import model.tasks.taskvisitors.ITaskVisitor;

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
     * Sets this task (and its children, if it haves them) to be visible/hidden on the GUI.
     * @param isVisible true if task should be shown, false if not
     */
    public abstract void setVisibility(boolean isVisible);

    /**
     * @return true if this task is visible to the user, false otherwise
     */
    public boolean getVisibility();
}
