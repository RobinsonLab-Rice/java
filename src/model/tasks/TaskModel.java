package model.tasks;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import main.adapters.tasks.Task2PlateAdapter;
import main.adapters.tasks.Task2SerialCommAdapter;
import main.adapters.tasks.Task2ViewAdapter;
import model.tasks.basictasks.*;
import model.tasks.basictasks.ALeafTask;

/**
 * Model that controls all creation and managing of tasks. Relays to plate model when wells need to be given tasks.
 * @author Christian
 *
 */
public class TaskModel {
	
	/**
	 * Adapter from the task model to plate model.
	 */
	private Task2PlateAdapter plateModel;
	
	/**
	 * Adapter from the task model to main view.
	 */
	private Task2ViewAdapter view;
	
	/**
	 * Adapter from the task model to serial model.
	 */
	private Task2SerialCommAdapter serialCommModel;
	
	/**
	 * ArrayList of stages (MultiTasks) that contain everything that will be done in the experiment.
	 */
	private MultiTask taskQueue;
	
	private ArrayList<ALeafTask> decompiledTasks;
	
	private ITaskVisitor decompileVisitor;
	
	private ITaskVisitor drawVisitor;
	
	/**
	 * Constructor for TaskModel, takes in adapters to allow the view and other models.
	 */
	public TaskModel(){
		taskQueue = new MultiTask();
		decompiledTasks = new ArrayList<ALeafTask>();
		decompileVisitor = new DecompileVisitor();
		drawVisitor = new DrawVisitor();
	}

    /* On initialization, connects to given adapters. */
    public void start(Task2ViewAdapter view, Task2PlateAdapter plateModel, Task2SerialCommAdapter serialModel) {
        this.view = view;
        this.plateModel = plateModel;
        this.serialCommModel = serialModel;
    }
	
	/**
	 * Adds a MLDR task to the execution queue.
	 * @param wellNum - well number to go to
	 * @param fluidAmount - amount of fluid to move
	 */
	public void addToQueue(int wellNum, int fluidAmount) {
		Point2D wellLocation = plateModel.getLocationFromNumber(wellNum);
		MultiTask taskToAdd = new MultiTask(new MoveTask(wellNum), new LowerTask(), new DispenseTask(fluidAmount), new RaiseTask());
		taskQueue.addTask(taskToAdd);
		view.updateView();
	}
	
	/**
	 * Adds a task (any task) to execution queue.
	 * @param taskToAdd - task to add (no preparation involved)
	 */
	public void addToQueue(IExecuteTask taskToAdd) {
		taskQueue.addTask(taskToAdd);
		view.updateView();
	}
	
	/**
	 * Called by the serial model when word has been received that the Arduino is ready for the next command.
	 */
	public void executeNext(){
		Iterator<ALeafTask> iter = decompiledTasks.iterator();
		if (iter.hasNext()){
			iter.next().execute(plateModel.getArmState(), serialCommModel.getOutputStream());
			iter.remove();
		}
		else{
			System.out.println("Done executing all tasks!");
		}
	}

	/**
	 * Removes all stages from the taskQueue, adding a new MultiTask to have something in it.
	 */
	public void clearAllTasks() {
		taskQueue.getSubtasks().clear();
		view.updateView();
	}

	/**
	 * Executes the tasks normally, ie by sending one command over at a time to Arduino
	 */
	public void executeAll() {
		//make sure we start on a clean slate
		decompiledTasks.clear();
		
		//decompile the specified stage and put the results in the decompiledTasks ArrayList
		taskQueue.executeVisitor(decompileVisitor, decompiledTasks);
		
		//execute the first one to start the chain!
		executeNext();
	}

	/**
	 * Executes all stages listed, in order.
	 */
	public void debugExecuteAll() {
		//make sure we start on a clean slate
		decompiledTasks.clear();
		
		//run the decompile visitor on all tasks
		taskQueue.executeVisitor(decompileVisitor, decompiledTasks);
		
		//execute them all at once by printing them out
		for (ALeafTask task : decompiledTasks){
			task.execute(plateModel.getArmState(), serialCommModel.getOutputStream());
		}
	}
	
	/**
	 * Draw all tasks by slapping the draw visitor onto them.
	 */
	public void drawTasks(Graphics g, double sF) {
		taskQueue.executeVisitor(drawVisitor, g, sF);
	}
	
	/**
	 * @return ArrayList of stages (MultiTasks) that contain everything that will be done in experiment
	 */
	public MultiTask getTasks(){
		return taskQueue;
	}
	
	/**
	 * @param taskQueue - ArrayList of stages (MultiTasks) that contain everything that will be done in experiment
	 */
	public void setTasks(MultiTask taskQueue){
		this.taskQueue = taskQueue;
		view.updateView();
		view.setTask(taskQueue);
	}

	public void changeExecutionData(Object[] path, String newData) {
		taskQueue.traverseOrModify(path, newData);
		view.updateView();
	}

	public void deleteExecutionTask(Object[] path) {
		taskQueue.traverseOrDelete(path);
		view.updateView();
	}

	public void insertAfterSelected(Object[] path, IExecuteTask taskToAdd) {
		taskQueue.traverseOrInsert(path, taskToAdd);
		view.updateView();
	}

    /**
     * Returns factories for both pre-made tasks and user defined ones.
     * @return
     */
    public Iterable<ITaskFactory> getTaskFactories() {
        return null;
    }
}
