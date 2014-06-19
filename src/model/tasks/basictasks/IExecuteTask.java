package model.tasks.basictasks;

import java.io.OutputStream;
import java.io.Serializable;

import model.plate.objects.ArmState;
import model.tasks.ITaskVisitor;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * Defines what task the robot should perform when attached to a Well context object.
 * @author Christian
 *
 */
public interface IExecuteTask extends Serializable, TreeNode {
	
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
	 * Returns the number of sub-tasks this task has. This will be 0 if the task is a serial task, and if it's
	 * an abstract task it will vary.
	 * @return number of children tasks
	 */
	public int getChildCount();
	
	/**
	 * @return child at the particular index
	 */
	public IExecuteTask getChild(int index);
	
	/**
	 * Either take the path down this task, or change the value of the current task.
	 * @param taskPath - path to take down to the leaf
	 * @param toChange - what to change in the leaf
	 */
	public void traverseOrModify(Object[] taskPath, String toChange);
	
	public void traverseOrDelete(Object[] path);
	
	public void traverseOrInsert(Object[] path, IExecuteTask taskToAdd);
}
