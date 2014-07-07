package model.tasks.basictasks;

import java.io.OutputStream;
import java.util.*;

import model.plate.objects.ArmState;
import model.tasks.taskvisitors.ITaskVisitor;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * Task made up of other tasks, useful to compartmentalize your experiment or save complex tasks for later use.
 */
public class MultiTask extends AExecuteTask {

	/* Auto generated serial ID to be abe to save the task. */
	private static final long serialVersionUID = 2695551386880973088L;
	
	/* ArrayList of tasks this MultiTask contains. */
	private ArrayList<IExecuteTask> taskList = new ArrayList<IExecuteTask>();

    /* Name of this task, also shows what the text file it is saved to will be. */
    public String name = "MultiTask";

    /* Empty constructor, does nothing. */
    public MultiTask() {
    }

    /* If user wants to add in tasks after the fact, they can still set name in constructor. */
    public MultiTask(String name) {
        this.name = name;
    }

	/* Creates a MultiTask out of some variable number of other tasks. */
	public MultiTask(IExecuteTask... taskArray){
		for (IExecuteTask task: taskArray){
            task.setParent(this);
			taskList.add(task);
		}
	}

    /* Creates a MultiTask out of some variable number of other tasks. */
    public MultiTask(String name, IExecuteTask... taskArray){
        this.name = name;
        for (IExecuteTask task: taskArray){
            task.setParent(this);
            taskList.add(task);
        }
    }

    /**
     * Calls all subtasks to execute themselves.
     * @param armState - current position of the arm, when this task is executed
     * @param outputStream - output stream tasks will execute through
     */
	public void execute(ArmState armState, OutputStream outputStream) {
		for (IExecuteTask task : taskList){
			task.execute(armState, outputStream);
		}
	}
	
	/**
	 * Calls the "Multi" case of the given algo.
	 * @param visitor The IPhraseVisitor algo to use.
	 * @param params vararg list of input parameters
	 * @return the result of running the Chord case of the visitor.
	 */
	@Override
	public Object executeVisitor(ITaskVisitor visitor, Object... params) {
		return visitor.caseAt("Multi", this, params);
	}

    /**
     * Whenever this task is loaded from JSON, go down the tree and set parents appropriately.
     */
    public void resetParents() {
        for (IExecuteTask child : taskList) {
            child.setParent(this);
            child.resetParents();
        }
    }

    /**
     * Appends input task to end of this multitask's list.
     */
    public void addTaskToEnd(IExecuteTask task) {
        taskList.add(task);
        task.setParent(this);
        task.resetParents();
    }

    /**
     * String representation of a MultiTask, useful for drawing to screen.
     */
    public String toString() {
        return name;
    }

    /* ---METHODS INHERITED FROM TREENODE--- */

    /**
     * Returns the children of the receiver as an <code>Enumeration</code>.
     */
    @Override
    public Enumeration children() {
        return Collections.enumeration(taskList);
    }

    /**
     * Returns true if the receiver allows children.
     */
    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    /**
     * @param childIndex index of the IExecuteTask to return
     */
    @Override
    public TreeNode getChildAt(int childIndex) {
        if (childIndex > taskList.size()){
            System.out.println("Trying to access a task out of bounds in this MultiTask.");
            return new NullTask();
        }
        else{
            return taskList.get(childIndex);
        }
    }

    /**
	 * @return number of tasks this MultiTask has.
	 */
	public int getChildCount() {
		return taskList.size();
	}

    /**
     * Returns the index of <code>node</code> in the receivers children.
     * If the receiver does not contain <code>node</code>, -1 will be
     * returned.
     *
     * @param node
     */
    @Override
    public int getIndex(TreeNode node) {
        return taskList.indexOf(node);
    }

    /**
     * Returns the parent <code>TreeNode</code> of the receiver.
     */
    @Override
    public TreeNode getParent() {
        return parent;
    }

    /**
     * Returns true if the receiver is a leaf.
     */
    @Override
    public boolean isLeaf() {
        return false;
    }

    /* ---METHODS INHERITED FROM MUTABLETREENODE--- */

    /**
     * Adds <code>child</code> to the receiver at <code>index</code>.
     * <code>child</code> will be messaged with <code>setParent</code>.
     *
     * @param child
     * @param index
     */
    @Override
    public void insert(MutableTreeNode child, int index) {
        taskList.add(index, (IExecuteTask) child);
    }

    /**
     * Removes the child at <code>index</code> from the receiver.
     *
     * @param index position of element to remove from this multitask
     */
    @Override
    public void remove(int index) {
        if (index > -1 && index < taskList.size())
            taskList.remove(index);
        else {
            System.out.println("Cannot remove task with index " + index + "from MultiTask " + name);
        }
    }

    /**
     * Removes <code>node</code> from the receiver. <code>setParent</code>
     * will be messaged on <code>node</code>.
     *
     * @param node node to remove from this multitask
     */
    @Override
    public void remove(MutableTreeNode node) {
        if (taskList.contains(node))
            taskList.remove(node);
        else System.out.println("Cannot remove task " + node + "from MultiTask " + name);
    }

    /**
     * Whenever somebody changes visualized multitask name, it changes the saved name in the model.
     *
     * @param object - new name of this multitask
     */
    @Override
    public void setUserObject(Object object) {
        this.name = (String) object;
    }
}
