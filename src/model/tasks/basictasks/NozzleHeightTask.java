package model.tasks.basictasks;

import java.io.OutputStream;

import model.plate.objects.ArmState;
import model.tasks.ITaskVisitor;

/**
 * Lower task, tells the nozzle servo to lower by the specified amount.
 * @author Christian
 */
public class NozzleHeightTask extends ALeafTask {

	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = -1956176933458574472L;
	
	private double heightToSet;
	
	/**
	 * Constructor, just sets the appropriate volume.
	 * @param heightToSet - height (in cm) nozzle should be set to
	 */
	public NozzleHeightTask(Double heightToSet){
		this.heightToSet = heightToSet;
	}

	/**
	 * When a nozzle height task is executed, it just sends a message to the microcontroller telling it to
	 * move to a certain height, which is saved to this task in its constructor.
	 */
	public void execute(ArmState armState, OutputStream outputStream) {
		String cmdString = "";
		if (heightToSet > 0){
			cmdString = "nozzleHeight(" + "1250" + ")";
		}
		else if (heightToSet < 0){
			cmdString = "nozzleHeight(" + "1500" + ")";
		}
		this.writeString(cmdString, outputStream);
	}
	
	/**
	 * Calls the "NozzleHeight" case of the given algo.
	 * @param algo The IPhraseVisitor algo to use.
	 * @param params vararg list of input parameters
	 * @return the result of running the Chord case of the visitor.
	 */
	@Override
	public Object executeVisitor(ITaskVisitor visitor, Object... params) {
		return visitor.caseAt("NozzleHeight", this, params);
	}

    /**
     * When somebody changes text on JTree, check if the data is correct and, if it is, set this task's parameters
     * appropriately.
     *
     * @param object - parameters (as a String)
     */
    @Override
    public void setUserObject(Object object) {
        //if object can't be converted to double, throw an exception
        try {
            this.heightToSet = Double.parseDouble((String) object);
        } catch (NumberFormatException e) {
            System.out.println("Tried to change nozzleHeight something that wasn't a double.");
            e.printStackTrace();
        }
    }
}