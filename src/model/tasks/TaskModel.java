package model.tasks;

import java.util.ArrayList;
import java.util.Iterator;

import main.adapters.tasks.Task2PlateAdapter;
import main.adapters.tasks.Task2SerialCommAdapter;
import main.adapters.tasks.Task2ViewAdapter;
import model.tasks.basictasks.*;
import model.tasks.basictasks.ALeafTask;
import model.tasks.taskvisitors.DecompileVisitor;
import model.tasks.taskvisitors.DrawVisitor;
import model.tasks.taskvisitors.ITaskVisitor;

import javax.swing.tree.DefaultTreeModel;

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
     * TreeModel that contains a multitask (the root of the tree). This multitask contains everything that will be
     * done in the experiment.
	 */
    private DefaultTreeModel taskQueue;
	
	private ArrayList<ALeafTask> decompiledTasks;
	
	private ITaskVisitor decompileVisitor;
	
	private ITaskVisitor drawVisitor;
	
	/**
	 * Constructor for TaskModel, takes in adapters to allow the view and other models.
	 */
	public TaskModel(){
		taskQueue = new DefaultTreeModel(new MultiTask(new MoveToWellTask("test", "2"), new MultiTask(new LowerTask(), new DispenseTask(100)), new MoveToWellTask("test", "4")));
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
	
//	/**
//	 * Adds a MLDR task to the execution queue.
//	 * @param wellNum - well number to go to
//	 * @param fluidAmount - amount of fluid to move
//	 */
//	public void addToQueue(int wellNum, int fluidAmount) {
//		Point2D wellLocation = plateModel.getLocationFromNumber(wellNum);
//		MultiTask taskToAdd = new MultiTask(new MoveTask(wellNum), new LowerTask(), new DispenseTask(fluidAmount), new RaiseTask());
//		taskQueue.addTask(taskToAdd);
//		view.updateView();
//	}
//
//	/**
//	 * Adds a task (any task) to execution queue.
//	 * @param taskToAdd - task to add (no preparation involved)
//	 */
//	public void addToQueue(IExecuteTask taskToAdd) {
//		taskQueue.addTask(taskToAdd);
//		view.updateView();
//	}
	
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

//	/**
//	 * Removes all stages from the taskQueue, adding a new MultiTask to have something in it.
//	 */
//	public void clearAllTasks() {
//		taskQueue.getSubtasks().clear();
//		view.updateView();
//	}

//	/**
//	 * Executes the tasks normally, ie by sending one command over at a time to Arduino
//	 */
//	public void executeAll() {
//		//make sure we start on a clean slate
//		decompiledTasks.clear();
//
//		//decompile the specified stage and put the results in the decompiledTasks ArrayList
//		taskQueue.executeVisitor(decompileVisitor, decompiledTasks);
//
//		//execute the first one to start the chain!
//		executeNext();
//	}

	/**
	 * Executes all stages listed, in order.
	 */
	public void debugExecuteAll() {
		//make sure we start on a clean slate
		decompiledTasks.clear();

		//run the decompile visitor on all tasks
        ((IExecuteTask) taskQueue.getRoot()).executeVisitor(decompileVisitor, decompiledTasks);

		//execute them all at once by printing them out
		for (ALeafTask task : decompiledTasks){
			task.execute(plateModel.getArmState(), serialCommModel.getOutputStream());
		}
	}
	
//	/**
//	 * Draw all tasks by slapping the draw visitor onto them.
//	 */
//	public void drawTasks(Graphics g, double sF) {
//		taskQueue.executeVisitor(drawVisitor, g, sF);
//	}
	
	/**
	 * @return ArrayList of stages (MultiTasks) that contain everything that will be done in experiment
	 */
	public MultiTask getTasks(){
        return (MultiTask) taskQueue.getRoot();
	}

    /**
     * Returns factories for both pre-made tasks and user defined ones.
     * @return
     */
    public Iterable<ITaskFactory> getTaskFactories() {
        ArrayList<ITaskFactory> factories = new ArrayList<>();
        factories.add(new PremadeTaskFactory(new MoveToWellTask("replaceMe", "0")));
        factories.add(new PremadeTaskFactory(new MoveToLocTask(0.0, 0.0)));
        factories.add(new PremadeTaskFactory(new DispenseTask(100)));
        factories.add(new PremadeTaskFactory(new NozzleHeightTask(1350.0)));

        return factories;
    }

    public DefaultTreeModel getTreeModel() {
        return this.taskQueue;
    }
}
