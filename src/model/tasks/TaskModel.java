package model.tasks;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import main.adapters.tasks.Task2PlateAdapter;
import main.adapters.tasks.Task2SerialCommAdapter;
import main.adapters.tasks.Task2SerializationAdapter;
import main.adapters.tasks.Task2ViewAdapter;
import model.plate.objects.Well;
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
     * Adapter from task model to serialization model.
     */
    private Task2SerializationAdapter serializationModel;
	
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
		taskQueue = new DefaultTreeModel(new MultiTask("Experiment Name"));
		decompiledTasks = new ArrayList<ALeafTask>();
		decompileVisitor = new DecompileVisitor();
		drawVisitor = new DrawVisitor();
	}

    /* On initialization, connects to given adapters. */
    public void start(Task2ViewAdapter view, Task2PlateAdapter plateModel,
                      Task2SerialCommAdapter serialModel, Task2SerializationAdapter serializationModel) {
        this.view = view;
        this.plateModel = plateModel;
        this.serialCommModel = serialModel;
        this.serializationModel = serializationModel;
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
	
	/**
	 * Draw all tasks by slapping the draw visitor onto them.
	 */
	public void drawTasks(Graphics g, double sF) {
        ((IExecuteTask) taskQueue.getRoot()).executeVisitor(drawVisitor, g, sF, new Point2D.Double(0, 0), plateModel.getPlates());
	}
	
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
        ArrayList<ITaskFactory> factories = new ArrayList<ITaskFactory>();

        //add in premade tasks
        factories.add(new TaskFactory(new MoveToWellTask("replaceMe", "0")));
        factories.add(new TaskFactory(new MoveToLocTask(0.0, 0.0)));
        factories.add(new TaskFactory(new DispenseTask(100)));
        factories.add(new TaskFactory(new NozzleHeightTask(1350.0)));
        factories.add(new TaskFactory(new MultiTask()));

        //add in user made tasks
        for (IExecuteTask task : serializationModel.getSavedTasks()){
            factories.add(new TaskFactory(task));
        }

        return factories;
    }

    /**
     * @return TreeModel that encompasses our current task heirarchy
     */
    public DefaultTreeModel getTreeModel() {
        return this.taskQueue;
    }

    /**
     * Make a multitask for moving input wells to end well. If dispenseAmounts is empty, it is assumed that we should use
     * the default amount set in the GUI.
     * @param startWells - variable number of wells to pick fluid up from
     * @param endWell - single well to dispense fluid to
     * @param dispenseAmounts - arraylist of amounts to dispense to each well, should be same size as startwells (unless empty)
     * @param shouldReverse - boolean for whether the direction of tasks should be reversed, resulting in a one-to-many task
     */
    public void makeMoveTask(ArrayList<Well> startWells, Well endWell, ArrayList<Double> dispenseAmounts, boolean shouldReverse) {
        //if dispense amount is empty, fill it to same length as startWells with default amount
        if (dispenseAmounts.size() == 0) {
            Double defaultAmount = view.getDefaultDispense();
            for (int i = 0; i < startWells.size(); i++) {
                dispenseAmounts.add(defaultAmount);
            }
        }

        MultiTask finalTask = null;
        //if we're just moving one well to the destination, we don't need to make an overarching multitask for it.
        if (startWells.size() == 1){
            finalTask = makeSingleWellToWellTask(startWells.get(0), endWell, dispenseAmounts.get(0));
        }
        //else, make a task for every movement and wrap it in a multitask one level up
        else {
            finalTask = new MultiTask("Move" + startWells.size() + "WellsTo1");
            //by default, go to each start well, withdraw, then dispense all in the end well
            double finalDeposit = 0;
            for (int i = 0; i < startWells.size(); i++) {
                finalTask.addTaskToEnd(makeSingleTransaction(startWells.get(i), -dispenseAmounts.get(i)));
                finalDeposit += dispenseAmounts.get(i);
                //finalTask.addTaskToEnd(makeSingleWellToWellTask(startWells.get(i), endWell, dispenseAmounts.get(i)));
            }
            finalTask.addTaskToEnd(makeSingleTransaction(endWell, finalDeposit));
        }

        //if use requested to reverse the tasks, do that
        if (shouldReverse) {} //TODO: implement a reverse method

        //finally, stick this constructed task on the end and tell the view to update itself
        ((MultiTask) taskQueue.getRoot()).addTaskToEnd(finalTask);
        taskQueue.nodeStructureChanged((MultiTask) taskQueue.getRoot());
    }

    /**
     * Helper function for making a multitask that moves fluid amount from the start to end well.
     */
    public MultiTask makeSingleWellToWellTask(Well start, Well end, Double amount) {
        return new MultiTask("MoveFluidWellToWell", makeSingleTransaction(start, -amount), makeSingleTransaction(end, amount));
    }

    /**
     * Make a task that either withdraws or deposit to the specified well
     * @param well - well object to move to
     * @param amount - amount of fluid to move, withdraw for negative amounts, deposit for positive
     * @return multitask encapsulating these basic tasks
     */
    public MultiTask makeSingleTransaction(Well well, Double amount) {
        String name;
        if (amount < 0) name = "MoveAndWithdraw";
        else name = "MoveAndDeposit";
        return new MultiTask(name,
                new MoveToWellTask(well),
                new LowerTask(),
                new DispenseTask(amount),
                new RaiseTask()
        );
    }
}
