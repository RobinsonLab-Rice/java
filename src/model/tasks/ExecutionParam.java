package model.tasks;

import java.awt.Color;
import java.io.Serializable;

public class ExecutionParam implements Serializable{

	/**
	 * Auto generated serial ID to be able to save tasks to files.
	 */
	private static final long serialVersionUID = 3108759591025603001L;

	public double fluidAmount;
	
	public double lowerAmount;
	
	public double delay;
	
	public Color taskColor;
	
	public ExecutionParam(String fluidAmount, String delay, Color taskColor){
		this.fluidAmount = Double.parseDouble(fluidAmount);
		this.lowerAmount = 1;
		this.delay = Double.parseDouble(delay);
		this.taskColor = taskColor;
	}
}
