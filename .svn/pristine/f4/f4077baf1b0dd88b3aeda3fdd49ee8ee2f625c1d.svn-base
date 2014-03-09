package model.plate;

import model.plate.objects.Well;

public interface IWellCmd {
	
	/**
	  * The method run by the well's update method which is called when the well is contacted by the dispatcher.
	  * @param context The well that is calling this method.   The context under which the command is to be run.
	  * @param disp The WellDispatcher that sent the command out.
	  */
	public abstract void apply(Well context, WellDispatcher disp);

}
