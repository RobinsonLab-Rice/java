package main.model.tasks;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import main.model.plate.PlateModel;
import main.model.plate.objects.Well;
import main.model.serial.SerialModel;
import main.model.serialization.SaveType;
import main.model.serialization.SerializationModel;
import main.model.tasks.basictasks.*;
import main.model.tasks.taskvisitors.DecompileVisitor;
import main.model.tasks.taskvisitors.DrawVisitor;
import main.model.tasks.taskvisitors.ITaskVisitor;
import main.util.Parser;
import main.view.panels.MainPanel;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

/**
 * Model that controls all creation and managing of tasks. Relays to plate model when wells need to be given tasks.
 * @author Christian
 *
 */
public class TaskModel {

    /**
     * Adapter from the task model to plate model.
     */
    private PlateModel plateModel;

    /**
     * Adapter from the task model to main view.
     */
    private MainPanel view;

    /**
     * Adapter from the task model to serial model.
     */
    private SerialModel serialCommModel;

    /**
     * Adapter from task model to serialization model.
     */
    private SerializationModel serializationModel;

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
    public TaskModel() {
        taskQueue = new DefaultTreeModel(new MultiTask("Experiment Name (triple click me to rename)"));
        decompiledTasks = new ArrayList<ALeafTask>();
        decompileVisitor = new DecompileVisitor();
        drawVisitor = new DrawVisitor();
    }

    /* On initialization, connects to given adapters. */
    public void start(MainPanel view, PlateModel plateModel,
                      SerialModel serialModel, SerializationModel serializationModel) {
        this.view = view;
        this.plateModel = plateModel;
        this.serialCommModel = serialModel;
        this.serializationModel = serializationModel;
    }

    /**
     * Called by the serial model when word has been received that the Arduino is ready for the next command.
     */
    public void executeNext() {
        Iterator<ALeafTask> iter = decompiledTasks.iterator();
        if (iter.hasNext()) {
            iter.next().execute(plateModel.getArmState(), serialCommModel.getOutputStream());
            iter.remove();
        } else {
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
        for (ALeafTask task : decompiledTasks) {
            task.execute(plateModel.getArmState(), serialCommModel.getOutputStream());
        }
    }

    /**
     * Draw all tasks by slapping the draw visitor onto them.
     */
    public void drawTasks(Graphics g, double sF) {
        ((IExecuteTask) taskQueue.getRoot()).executeVisitor(drawVisitor, g, sF, new Point2D.Double(0, 0), plateModel.getPlateList());
    }

    /**
     * @return ArrayList of stages (MultiTasks) that contain everything that will be done in experiment
     */
    public MultiTask getTasks() {
        return (MultiTask) taskQueue.getRoot();
    }

    /**
     * Returns factories for both pre-made tasks and user defined ones.
     *
     * @return
     */
    public Iterable<ITaskFactory> getTaskFactories() {
        ArrayList<ITaskFactory> factories = new ArrayList<ITaskFactory>();

        //add in premade tasks
        factories.add(new TaskFactory(new MoveToWellTask("Plate1", "n", "m")));
        factories.add(new TaskFactory(new MoveToLocTask("n", "m")));
        factories.add(new TaskFactory(new DispenseTask("n")));
        factories.add(new TaskFactory(new NozzleHeightTask("n")));
        factories.add(new TaskFactory(new MultiTask()));

        //add in user made tasks
        ArrayList<Object> savedTasks = serializationModel.getSavedData(SaveType.TASK);
        for (Object task : savedTasks) {
            //add task, making sure to cast it appropriately
            factories.add(new TaskFactory((IExecuteTask) task));
        }

        return factories;
    }

    public Iterable<ITaskFactory> getExperimentFactories() {
        ArrayList<ITaskFactory> factories = new ArrayList<ITaskFactory>();

        //add in user made tasks
        ArrayList<Object> savedTasks = serializationModel.getSavedData(SaveType.EXPERIMENT);
        for (Object task : savedTasks) {
            //add task, making sure to cast it appropriately
            factories.add(new TaskFactory((IExecuteTask) task));
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
     *
     * @param startWells      - variable number of wells to pick fluid up from
     * @param endWell         - single well to dispense fluid to
     * @param shouldReverse   - boolean for whether the direction of tasks should be reversed, resulting in a one-to-many task
     */
    public void makeMoveTask(ArrayList<Well> startWells, Well endWell, boolean shouldReverse) {

        //get default dispense amount, can defintitely be a variable (n, TOSET, etc.)
        String dispenseAmount = view.getDefaultDispense();

        MultiTask finalTask = null;
        //if we're just moving one well to the destination, we don't need to make an overarching multitask for it.
        if (startWells.size() == 1) {
            finalTask = makeSingleWellToWellTask(startWells.get(0), endWell, dispenseAmount);
        }
        //else, make a task for every movement and wrap it in a multitask one level up
        else {
            finalTask = new MultiTask("Move" + startWells.size() + "WellsTo1");
            //by default, go to each start well, withdraw, then dispense all in the end well
            for (int i = 0; i < startWells.size(); i++) {
                if (Parser.isNumeric(dispenseAmount)) {
                    String withdraw = String.valueOf(-1*Double.parseDouble(dispenseAmount));
                    finalTask.addTaskToEnd(makeSingleTransaction(startWells.get(i), withdraw, true));
                }
                else {
                    finalTask.addTaskToEnd(makeSingleTransaction(startWells.get(i), dispenseAmount, true));
                }
            }
            if (Parser.isNumeric(dispenseAmount)) {
                String total = String.valueOf(Double.parseDouble(dispenseAmount)*startWells.size());
                finalTask.addTaskToEnd(makeSingleTransaction(endWell, total, false));
            }
            else {
                finalTask.addTaskToEnd(makeSingleTransaction(endWell, "total", false));
            }

        }

        //if use requested to reverse the tasks, do that
        if (shouldReverse) {
        } //TODO: implement a reverse method

        //finally, stick this constructed task on the end and tell the view to update itself
        appendTaskToQueue(finalTask);
    }

    /**
     * Helper function for making a multitask that moves fluid amount from the start to end well.
     */
    public MultiTask makeSingleWellToWellTask(Well start, Well end, String amount) {
        return new MultiTask("MoveFluidWellToWell", makeSingleTransaction(start, amount, true), makeSingleTransaction(end, amount, false));
    }

    /**
     * Make a task that either withdraws or deposit to the specified well
     *
     * @param well   - well object to move to
     * @param amount - amount of fluid to move, withdraw for negative amounts, deposit for positive
     * @return multitask encapsulating these basic tasks
     */
    public MultiTask makeSingleTransaction(Well well, String amount, boolean isWithdraw) {
        String name;

        if (isWithdraw) {
            name = "MoveAndWithdraw";
            amount = "-" + amount;
        }
        else name = "MoveAndDeposit";

        return new MultiTask(name,
                new MoveToWellTask(well),
                new LowerTask(),
                new DispenseTask(amount),
                new RaiseTask()
        );
    }

    /**
     * Append this task to the end of the task tree.
     *
     * @param taskToAdd IExecuteTask to add
     */
    public void appendTaskToQueue(IExecuteTask taskToAdd) {
        ((MultiTask) taskQueue.getRoot()).addTaskToEnd(taskToAdd);
        taskQueue.nodeStructureChanged((MultiTask) taskQueue.getRoot());
    }

    /**
     * Sends an order to repaint, so that objects manipulating tasks can make a call to redraw them when finished.
     */
    public void repaint() {
        view.update();
    }

    /**
     * Generates a sequence of tasks based on input task, replacing the input one.
     *
     * @param taskToLoop task to repeat
     * @param variable   variable to be overwritten in task
     * @param startVal   start value of variable
     * @param endVal     end value of variable
     * @param incVal     how much to increment variable every time
     * @return true if tasks were successfully made and added, false otherwise
     */
    public boolean loopGenerateTasks(IExecuteTask taskToLoop, String variable, String startVal, String endVal, String incVal) {

        TaskFactory taskToMake = new TaskFactory(taskToLoop);

        MultiTask accTasks = new MultiTask("Looped" + taskToMake.toString());

        ArrayList loopVals = getLoopValues(startVal, endVal, incVal);
        if (loopVals == null) return false;

        //for each loop value we obtained, make a new task from the factory where we replace the all occurences of the
        //variable with the value
        for (Object value : loopVals) {
            IExecuteTask newTask = taskToMake.make();
            newTask.replaceAll(variable, value);
            accTasks.addTaskToEnd(newTask);
        }

        //insert this new node exactly where the old one was
        accTasks.setParent((MutableTreeNode) taskToLoop.getParent());
        taskQueue.insertNodeInto(accTasks, (MutableTreeNode) taskToLoop.getParent(), taskToLoop.getParent().getIndex(taskToLoop));
        ((MutableTreeNode) taskToLoop.getParent()).remove(taskToLoop);
        taskQueue.nodeStructureChanged(taskToLoop.getParent());

        return true;
    }

    /**
     * Replaces successive values of a variable in a task. Return value indicates whether or not task was successfully
     * changed. Changes taskToAlter as a side-effect.
     *
     * @param taskToAlter task to change
     * @param variable   variable to be overwritten in task
     * @param startVal   start value of variable
     * @param endVal     end value of variable
     * @param incVal     how much to increment variable every time
     * @return true if tasks were successfully made and added, false otherwise
     */
    public boolean loopReplaceTasks(IExecuteTask taskToAlter, String variable, String startVal, String endVal, String incVal) {

        ArrayList loopVals = getLoopValues(startVal, endVal, incVal);
        if (loopVals == null) return false;

        //for each loop value we obtained, make a new task from the factory where we replace the all occurences of the
        //variable with the value
        for (Object value : loopVals) {
            taskToAlter.replaceOne(variable, value);
        }

        return true;
    }

    /**
     * Gets loop values based on input strings. If the values are alphanumeric, gets list from plate model, otherwise
     * if they're all numeric just creates a simple list of doubles (as a string).
     * @param startVal start value of variable
     * @param endVal end value of variable
     * @param incVal how much to increment variable every time
     * @return
     */
    public ArrayList<String> getLoopValues(String startVal, String endVal, String incVal) {
        ArrayList loopVals = new ArrayList();
        //if start and end values are both well identifiers, populate arraylist with proper identifier list
        if (Parser.isIdentifier(startVal) && Parser.isIdentifier(endVal) && Parser.isInteger(incVal)) {
            loopVals = plateModel.getPlate(view.getDefaultPlate()).getWellsInRange(startVal, endVal, Integer.parseInt(incVal));
            if (loopVals == null) return null;
        }
        //else if values are letters, get all between them
        else if (startVal.matches("[A-Z]") && endVal.matches("[A-Z]") && Parser.isInteger(incVal)) {
            int start = (int) startVal.charAt(0);
            int end = (int) endVal.charAt(0);
            int inc = Integer.parseInt(incVal);

            while (start <= end) {
                loopVals.add(String.valueOf((char) start));
                start += inc;
            }
        }
        //else if values are integers, get all between them
        else if (Parser.isInteger(startVal) && Parser.isInteger(endVal) && Parser.isInteger(incVal)) {
            int start = Integer.parseInt(startVal);
            int end = Integer.parseInt(endVal);
            int inc = Integer.parseInt(incVal);

            while (start <= end) {
                loopVals.add(String.valueOf(start));
                start += inc;
            }
        }
        //else if all required values are numbers, fill arraylist with all them.
        else if (Parser.isNumeric(startVal) && Parser.isNumeric(endVal) && Parser.isNumeric(incVal)) {
            double start = Double.parseDouble(startVal);
            double end = Double.parseDouble(endVal);
            double inc = Double.parseDouble(incVal);

            while (start <= end) {
                loopVals.add(String.valueOf(start));
                start += inc;
            }
        }
        //else the values don't correspond to anything we know, don't do anything.
        else {
            return null;
        }
        return loopVals;
    }
}
