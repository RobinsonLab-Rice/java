package model.tasks.basictasks;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import model.plate.objects.ArmState;
import model.tasks.ITaskVisitor;

public class MultiTask implements IExecuteTask{

	/**
	 * Auto generated serial ID to be abe to save the task.
	 */
	private static final long serialVersionUID = 2695551386880973088L;
	
	/**
	 * ArrayList of tasks this MultiTask contains.
	 */
	private ArrayList<IExecuteTask> taskList = new ArrayList<IExecuteTask>();
	
	public MultiTask() {}

	/**
	 * Creates a MultiTask out of some variable number of other tasks.
	 */
	public MultiTask(IExecuteTask... taskArray){
		taskList = new ArrayList<IExecuteTask>();
		for (IExecuteTask task: taskArray){
			taskList.add(task);
		}
	}
	
	/**
	 * For this multi-task, take in the well that it belongs to, then execute all sub-tasks it contains.
	 * @param context - Well that this multi-task belongs to
	 */
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
	 * @return subtask corresponding to input value, or a NullTask if it did not exist.
	 */
	public IExecuteTask getSubtask(int taskPosition){
		if (taskPosition > taskList.size()){
			System.out.println("Trying to access a task out of bounds in this MultiTask.");
			return new NullTask();
		}
		else{
			return taskList.get(taskPosition);
		}
	}
	
	/**
	 * Calls the "Multi" case of the given algo.
	 * @param algo The IPhraseVisitor algo to use.
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
	 * Returns number of tasks this MultiTask has.
	 */
	public int getChildCount() {
		return taskList.size();
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
		return "MultiTask";
	}

	/**
	 * This is an abstract task, so this function will traverse down the path
	 * until it gets to the leaf task.
	 */
	@Override
	public void traverseOrModify(Object[] taskPath, String toChange) {
		Object[] reducedPath = Arrays.copyOfRange(taskPath, 1, taskPath.length);
		IExecuteTask taskToEnter = (IExecuteTask) reducedPath[0];
		for (IExecuteTask task : taskList) {
			if (task == taskToEnter) {
				task.traverseOrModify(reducedPath, toChange);
			}
		}
	}

	@Override
	public void traverseOrDelete(Object[] path) {
		//if we want to delete the first multitask, just delete all its children
		if (path.length == 1) {
			taskList.clear();
			return;
		}
		Object[] reducedPath = Arrays.copyOfRange(path, 1, path.length);
		IExecuteTask firstTask = (IExecuteTask) reducedPath[0];
		//if there's only one task left in the path, delete it from our ArrayList
		if (reducedPath.length == 1) {
			taskList.remove(firstTask);
		}
		//else, recurse into the first element of the list
		else {
			for (IExecuteTask task : taskList) {
				if (task == firstTask) {
					task.traverseOrDelete(reducedPath);
				}
			}
		}
	}

	@Override
	public void traverseOrInsert(Object[] path, IExecuteTask taskToAdd) {
		Object[] reducedPath = Arrays.copyOfRange(path, 1, path.length);
		IExecuteTask firstTask = (IExecuteTask) reducedPath[0];
		//if there's only one task left in the path, find it in the ArrayList and add taskToAdd after it
		if (reducedPath.length == 1) {
			int newTaskIndex = taskList.indexOf(firstTask) + 1;
			//if we're appending to the end of the list
			if (newTaskIndex == taskList.size() + 1) taskList.add(taskToAdd);
			else taskList.add(newTaskIndex, taskToAdd);
			
		}
		//else, recurse into the first element of the list
		else {
			for (IExecuteTask task : taskList) {
				if (task == firstTask) {
					task.traverseOrInsert(reducedPath, taskToAdd);
				}
			}
		}
	}
}
