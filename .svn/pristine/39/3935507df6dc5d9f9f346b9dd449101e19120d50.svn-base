package model.tasks.basictasks;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.OutputStream;
import java.util.Arrays;

import model.plate.objects.ArmState;
import model.tasks.ExecutionParam;
import model.tasks.ITaskVisitor;

public class MoveFromExternalTask extends ADrawingTask{

	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = -3821968806359977385L;

	IExecuteTask taskToDo;
	
	Point2D destination;
	
	ExecutionParam executeParams;

	public MoveFromExternalTask(ExecutionParam executeParams, Point2D destination){
		this.destination = destination;
		this.executeParams = executeParams;
		taskToDo = getMldrTask(destination, executeParams);
	}
	
	public void execute(ArmState armState, OutputStream outputStream) {
		taskToDo.execute(armState, outputStream);
	}

	/**
	 * Drawing for a task that moves fluid from an external source to a well. For now, draws an X on wells that will
	 * be filled with an external source.
	 */
	public void draw(Graphics g, double sF) {
		g.setColor(executeParams.taskColor);
		int xFactor = 1;
		Point destPoint = new Point((int) Math.round(destination.getX()*sF), (int) Math.round(destination.getY()*sF));
		g.drawLine(destPoint.x - (int)(xFactor*sF), destPoint.y - (int)(xFactor*sF), destPoint.x + (int)(xFactor*sF), destPoint.y + (int)(xFactor*sF));
		g.drawLine(destPoint.x - (int)(xFactor*sF), destPoint.y + (int)(xFactor*sF), destPoint.x + (int)(xFactor*sF), destPoint.y - (int)(xFactor*sF));
	}
	
	/**
	 * Calls the "MoveFromExternal" case of the given algo.
	 * @param algo The IPhraseVisitor algo to use.
	 * @param params vararg list of input parameters
	 * @return the result of running the Chord case of the visitor.
	 */
	@Override
	public Object executeVisitor(ITaskVisitor visitor, Object... params) {
		return visitor.caseAt("MoveFromExternal", this, params);
	}
	
	/**
	 * @return underlying IExecuteTask that this task wraps
	 */
	public IExecuteTask getTask(){
		return taskToDo;
	}
	
	/**
	 * TODO: same as MLDRTask
	 */
	public int getChildCount() {
		return taskToDo.getChildCount();
	}
	
	/**
	 * Get the MultiTask's subtasks when we need a specific one.
	 */
	public IExecuteTask getChild(int index) {
		return taskToDo.getChild(index);
	}

	/**
	 * Never doing this now, needs to be reconsidered.
	 */
	@Override
	public void traverseOrModify(Object[] taskPath, String toChange) {
//		Object[] reducedPath = Arrays.copyOfRange(taskPath, 1, taskPath.length);
//		IExecuteTask taskToEnter = (IExecuteTask) reducedPath[0];
//		for (IExecuteTask task : task) {
//			if (task == taskToEnter) {
//				task.traverseOrModify(reducedPath, toChange);
//			}
//		}
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
