package model.tasks.basictasks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.OutputStream;

import model.plate.objects.ArmState;
import model.tasks.ITaskVisitor;

public class MLDRTask extends ADrawingTask {

	private static final long serialVersionUID = 6773359703734922374L;
	
	MultiTask taskToDo;
	
	private boolean isDispensing;
	private int fluidAmount;
	private Point2D wellLoc;
	
	/**
	 * @param wellLoc - location of the well, in cm
	 * @param fluidAmount - amount of fluid to move
	 */
	public MLDRTask(Point2D wellLoc, int fluidAmount) {
		isDispensing = fluidAmount > 0;
		this.fluidAmount = fluidAmount;
		this.wellLoc = wellLoc;
		
		MoveTask moveTask = new MoveTask(wellLoc);
		LowerTask lowerTask = new LowerTask();
		DispenseTask dispenseTask = new DispenseTask(fluidAmount);
		RaiseTask raiseTask = new RaiseTask();
		
		taskToDo = new MultiTask(moveTask, lowerTask, dispenseTask, raiseTask);
		
		//if we are withdrawing fluid, we need to immediately prep the pump after withdrawing
		if (fluidAmount < 0)
			taskToDo = new MultiTask(moveTask, lowerTask, dispenseTask, new DispenseTask(750), raiseTask);
			
		//taskToDo = new MultiTask(moveTask);
	}
	
	@Override
	public void execute(ArmState armState, OutputStream outputStream) {
		taskToDo.execute(armState, outputStream);
	}

	/**
	 * Calls the "MLDR" case of the given algo.
	 * @param algo The IPhraseVisitor algo to use.
	 * @param params vararg list of input parameters
	 * @return the result of running the Chord case of the visitor.
	 */
	@Override
	public Object executeVisitor(ITaskVisitor visitor, Object... params) {
		return visitor.caseAt("MLDR", this, params);
	}
	
	@Override
	public void draw(Graphics g, double sF) {
		g.setColor(Color.BLACK);
		//if we're dispensing, draw an X
		if (isDispensing) {
			int xFactor = Math.abs(fluidAmount / 200);
			Point destPoint = new Point((int) Math.round(wellLoc.getX()*sF), (int) Math.round(wellLoc.getY()*sF));
			g.drawLine(destPoint.x - (int)(xFactor*sF), destPoint.y - (int)(xFactor*sF), destPoint.x + (int)(xFactor*sF), destPoint.y + (int)(xFactor*sF));
			g.drawLine(destPoint.x - (int)(xFactor*sF), destPoint.y + (int)(xFactor*sF), destPoint.x + (int)(xFactor*sF), destPoint.y - (int)(xFactor*sF));
		}
		//if we're withdrawing, draw an O
		else {
			int xFactor = Math.abs(fluidAmount / 200);
			Point destPoint = new Point((int) Math.round(wellLoc.getX()*sF), (int) Math.round(wellLoc.getY()*sF));
			g.drawOval(destPoint.x - (int)(xFactor*sF), destPoint.y - (int)(xFactor*sF), (int)(xFactor*sF*2), (int)(xFactor*sF*2));
		}
	}
	
	/**
	 * @return MultiTask that this task wraps
	 */
	public MultiTask getTask(){
		return taskToDo;
	}

}
