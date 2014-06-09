package model.tasks;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import model.tasks.PlateAdapter;
import model.tasks.SerialCommAdapter;
import model.tasks.basictasks.ASerialTask;
import model.tasks.basictasks.IExecuteTask;
import model.tasks.basictasks.MLDRTask;
import model.tasks.basictasks.MoveFromExternalTask;
import model.tasks.basictasks.MoveWellToWellTask;
import model.tasks.basictasks.MultiTask;

/**
 * Model that controls all creation and managing of tasks. Relays to plate model when wells need to be given tasks.
 * @author Christian
 *
 */
public class TaskModel {
	
	/**
	 * Adapter from the task model to plate model.
	 */
	private PlateAdapter plateModelAdapter;
	
	/**
	 * Adapter from the task model to main view.
	 */
	private ViewAdapter view;
	
	/**
	 * Adapter from the task model to serial model.
	 */
	private SerialCommAdapter serialModelAdapter;
	
	/**
	 * ArrayList of stages (MultiTasks) that contain everything that will be done in the experiment.
	 */
	private MultiTask taskQueue;
	
	private ArrayList<ASerialTask> decompiledTasks;
	
	private ITaskVisitor decompileVisitor;
	
	private ITaskVisitor drawVisitor;
	
	/**
	 * Constructor for TaskModel, takes in adapters to allow the TaskModel to talk to the plate and serial model.
	 * @param plateModelAdapter - adapter from the task model to plate model
	 * @param serialModelAdapter - adapter from the task model to serial model
	 */
	public TaskModel(ViewAdapter viewAdapter, PlateAdapter plateModelAdapter, SerialCommAdapter serialModelAdapter){
		this.plateModelAdapter = plateModelAdapter;
		this.serialModelAdapter = serialModelAdapter;
		this.view = viewAdapter;
		taskQueue = new MultiTask();
		decompiledTasks = new ArrayList<ASerialTask>();
		
		decompileVisitor = new DecompileVisitor();
		
		drawVisitor = new DrawVisitor();
	}

	/**
	 * Adds the task to the execution queue by making a composite task which: moves to the source well, (optional) mixes the well,
	 * lowers the nozzle, dispenses negative liquid, raises the nozzle, moves to the destination well, lowers, and dispenses positive
	 * liquid.
	 */
	public void addToQueue(ExecutionParam taskParams, SetupParam setupParams, String source, String destination) {
		
		//get the location of the well with specified number
		Point2D destinationPoint = plateModelAdapter.getLocationFromNumber(Integer.parseInt(destination));
		
		if (source.equals("EXTERNAL")){
			taskQueue.addTask(new MoveFromExternalTask(taskParams, destinationPoint));
		}
		else{
			Point2D sourcePoint = plateModelAdapter.getLocationFromNumber(Integer.parseInt(source));
			taskQueue.addTask(new MoveWellToWellTask(taskParams, sourcePoint, destinationPoint));
		}
		
		view.updateView();
	}
	
	/**
	 * Queue up a task based on points clicked on the screen (and so must be a well to well task).
	 */
	public void addToQueue(ExecutionParam executionParams, SetupParam setupParams, Point source, Point destination){
		Point2D destinationWell = plateModelAdapter.getLocationFromScreen(destination);
		Point2D sourceWell = plateModelAdapter.getLocationFromScreen(source);
		
		if (destinationWell == null | sourceWell == null){
			System.out.println("Did not click on a well.");
		}
		else{
			taskQueue.addTask(new MoveWellToWellTask(executionParams, sourceWell, destinationWell));
			view.updateView();
		}
	}
	
	/**
	 * Adds a MLDR task to the execution queue.
	 * @param wellNum - well number to go to
	 * @param fluidAmount - amount of fluid to move
	 */
	public void addToQueue(int wellNum, int fluidAmount) {
		Point2D wellLocation = plateModelAdapter.getLocationFromNumber(wellNum);
		MLDRTask taskToAdd = new MLDRTask(wellLocation, fluidAmount);
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
		Iterator<ASerialTask> iter = decompiledTasks.iterator();
		if (iter.hasNext()){
			iter.next().execute(plateModelAdapter.getArmState(), serialModelAdapter.getOutputStream());
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
		for (ASerialTask task : decompiledTasks){
			task.execute(plateModelAdapter.getArmState(), serialModelAdapter.getOutputStream());
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
}
