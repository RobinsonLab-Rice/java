package main.adapters.plate;

import model.plate.objects.Well;
import model.tasks.TaskModel;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Adapter used by plate model to talk to serial model.
 * @author Christian
 *
 */
public class Plate2TaskAdapter {

    private TaskModel taskModel;

    /* Sets up model references necessary for this adapter. */
    public Plate2TaskAdapter(TaskModel taskModel) {
        this.taskModel = taskModel;
    }

    /**
	 * Tells the serial model to draw/paint all its tasks.
	 * @param g graphics object to draw with
	 * @param sF cm to pixel scale factor to draw objects at
	 */
	public void drawTasks(Graphics g, double sF){
        taskModel.drawTasks(g, sF);
    }

    /**
     * Make a task moving specified fluid amounts from startWells to endWell.
     * @param startWells - variable number of wells to pick fluid up from
     * @param endWell - single well to dispense fluid to
     * @param dispenseAmounts - arraylist of amounts to dispense to each well, should be same size as startwells (unless empty)
     * @param shouldReverse - boolean for whether the direction of tasks should be reversed, resulting in a one-to-many task
     */
    public void makeMoveTask(ArrayList<Well> startWells, Well endWell, ArrayList<Double> dispenseAmounts, boolean shouldReverse) {
        taskModel.makeMoveTask(startWells, endWell, dispenseAmounts, shouldReverse);
    }
}
