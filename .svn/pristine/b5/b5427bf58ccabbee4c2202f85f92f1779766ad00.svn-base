package model.tasks.basictasks;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.OutputStream;
import java.util.Arrays;

import model.plate.objects.ArmState;
import model.tasks.ExecutionParam;
import model.tasks.ITaskVisitor;

public class MoveWellToWellTask extends ADrawingTask{

	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = 1291954456957406343L;

	MultiTask wellTasks;
	
	Point2D source;
	
	Point2D destination;
	
	ExecutionParam executeParams;
	
	public MoveWellToWellTask(ExecutionParam executionParams, Point2D source, Point2D destination) {
		this.executeParams = executionParams;
		this.source = source;
		this.destination = destination;
		wellTasks = new MultiTask();
		wellTasks.addTask(getMldrTask(source, executionParams));
		wellTasks.addTask(getInverseMldrTask(destination, executionParams));
	}

	public void execute(ArmState armState, OutputStream outputStream) {
		wellTasks.execute(armState, outputStream);
	}

	/**
	 * Drawing for a task that moves fluid from one well to another. Draws an arrow from the center of the
	 * source well to the destination well.
	 */
	public void draw(Graphics g, double sF) {
		g.setColor(executeParams.taskColor);
		drawArrow(g, 	new Point((int) Math.round(source.getX()*sF), (int) Math.round(source.getY()*sF)),
						new Point((int) Math.round(destination.getX()*sF), (int) Math.round(destination.getY()*sF)));
	}

	/**
	 * Calls the "MoveWellToWell" case of the given algo.
	 * @param algo The IPhraseVisitor algo to use.
	 * @param params vararg list of input parameters
	 * @return the result of running the Chord case of the visitor.
	 */
	@Override
	public Object executeVisitor(ITaskVisitor visitor, Object... params) {
		return visitor.caseAt("MoveWellToWell", this, params);
	}
	
	/**
	 * @return MultiTask that this task wraps
	 */
	public MultiTask getTask(){
		return wellTasks;
	}
	
	/**
	 * TODO: same as MLDRtask
	 */
	public int getChildCount() {
		return wellTasks.getChildCount();
	}
	
	/**
	 * Get the MultiTask's subtasks when we need a specific one.
	 */
	public IExecuteTask getChild(int index) {
		return wellTasks.getChild(index);
	}

	/**
	 * This is an abstract task, so this function will traverse down the path
	 * until it gets to the leaf task.
	 */
	@Override
	public void traverseOrModify(Object[] taskPath, String toChange) {
		Object[] reducedPath = Arrays.copyOfRange(taskPath, 1, taskPath.length);
		IExecuteTask taskToEnter = (IExecuteTask) reducedPath[0];
		for (IExecuteTask task : wellTasks.getSubtasks()) {
			if (task == taskToEnter) {
				task.traverseOrModify(reducedPath, toChange);
			}
		}
	}

	@Override
	public void traverseOrDelete(Object[] path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void traverseOrInsert(Object[] path, IExecuteTask taskToAdd) {
		// TODO Auto-generated method stub
		
	}
}
