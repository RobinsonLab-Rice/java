package model.tasks.basictasks;

import java.io.OutputStream;
import java.util.*;

import model.plate.objects.ArmState;
import model.tasks.taskvisitors.ITaskVisitor;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class MultiTask implements IExecuteTask {

	/**
	 * Auto generated serial ID to be abe to save the task.
	 */
	private static final long serialVersionUID = 2695551386880973088L;
	
	/**
	 * ArrayList of tasks this MultiTask contains.
	 */
	private ArrayList<IExecuteTask> taskList = new ArrayList<IExecuteTask>();
	
	public MultiTask() {}

    private MutableTreeNode parent;

    private String name = "MultiTask";

	/**
	 * Creates a MultiTask out of some variable number of other tasks.
	 */
	public MultiTask(IExecuteTask... taskArray){
		taskList = new ArrayList<IExecuteTask>();
		for (IExecuteTask task: taskArray){
            task.setParent(this);
			taskList.add(task);
		}
	}

	public void execute(ArmState armState, OutputStream outputStream) {
		for (IExecuteTask task : taskList){
			task.execute(armState, outputStream);
		}
	}
	
	/**
	 * Adds the input task to the end of this MultiTask.
	 */
	public void addTask(IExecuteTask taskToAdd){
		this.taskList.add(taskToAdd);
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
	 * @return subtasks of this multi task
	 */
	public ArrayList<IExecuteTask> getSubtasks(){
		return taskList;
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
	 * Returns number of tasks this MultiTask has.
	 */
	public int getChildCount() {
		return taskList.size();
	}

    /**
     * Returns the parent <code>TreeNode</code> of the receiver.
     */
    @Override
    public TreeNode getParent() {
        return parent;
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
     * Returns true if the receiver allows children.
     */
    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    /**
     * Returns true if the receiver is a leaf.
     */
    @Override
    public boolean isLeaf() {
        return false;
    }

    /**
     * Returns the children of the receiver as an <code>Enumeration</code>.
     */
    @Override
    public Enumeration children() {
        return Collections.enumeration(taskList);
    }

    /**
	 * Get the subtask at the specified index.
	 */
	public IExecuteTask getChild(int index) {
		return taskList.get(index);
	}
	
	/**
	 * String representation of a MultiTask, useful for drawing to screen.
	 */
	public String toString() {
		return name;
	}

    /*-----METHODS INHERITED FROM MUTABLETREENODE-----*/

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
     * @param index
     */
    @Override
    public void remove(int index) {
        taskList.remove(index);
    }

    /**
     * Removes <code>node</code> from the receiver. <code>setParent</code>
     * will be messaged on <code>node</code>.
     *
     * @param node
     */
    @Override
    public void remove(MutableTreeNode node) {
        taskList.remove(node);
    }

    /**
     * Whenever somebody changes visualized name, it changes the saved name in the model.
     *
     * @param object - new name of this multitask
     */
    @Override
    public void setUserObject(Object object) {
        this.name = (String) object;
    }

    /**
     * Removes the receiver from its parent.
     */
    @Override
    public void removeFromParent() {
        parent.remove(this);
    }

    /**
     * Sets the parent of the receiver to <code>newParent</code>.
     *
     * @param newParent
     */
    @Override
    public void setParent(MutableTreeNode newParent) {
        this.parent = newParent;
    }
}
