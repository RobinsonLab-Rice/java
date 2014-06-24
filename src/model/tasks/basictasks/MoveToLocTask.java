package model.tasks.basictasks;

import model.plate.objects.ArmState;
import model.tasks.taskvisitors.ITaskVisitor;

import java.awt.geom.Point2D;
import java.io.OutputStream;
import java.math.BigDecimal;

/**
 * Move task, tells the arm to move to specified (absolute) location.
 *
 * Created by Christian on 6/23/2014.
 */
public class MoveToLocTask extends ALeafTask {

    /**
     * Auto generated serial ID.
     */
    private static final long serialVersionUID = 8216224355050783969L;

    /**
     * Location to move to (absolute relative to origin).
     */
    private Point2D destination;

    public MoveToLocTask(double x, double y) {
        destination = new Point2D.Double(x, y);
    }

    /**
     * Executes everything this task contains. If it is a higher level task, this
     * executes the sub-level tasks. If it is a lower task, it will actually participate
     * in some form of serial communication with the Arduino.
     *
     * @param armState     - current position of the arm, when this task is executed
     * @param outputStream - ouput stream tasks will execute through
     */
    @Override
    public void execute(ArmState armState, OutputStream outputStream) {
        double xCmToMove = armState.getX() - destination.getX();
        double yCmToMove = armState.getY() - destination.getY();

        //round this result using BigDecimals to send over only 2 decimal places
        xCmToMove = (new BigDecimal(xCmToMove).setScale(2, BigDecimal.ROUND_HALF_DOWN)).doubleValue();
        yCmToMove = (new BigDecimal(yCmToMove).setScale(2, BigDecimal.ROUND_HALF_DOWN)).doubleValue();

        //construct and send the command
        String cmdString = "move(" + xCmToMove + "," + yCmToMove + ")";
        this.writeString(cmdString, outputStream);

        //update the arm location, making sure to only save it to 2 decimal places.
        BigDecimal roundedX = new BigDecimal(armState.getX() - xCmToMove).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal roundedY = new BigDecimal(armState.getY() - yCmToMove).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        armState.setLocation(roundedX.doubleValue(), roundedY.doubleValue());
    }

    /**
     * Executes (accepts) the visitor, calling the case associated with this host's index value.
     *
     * @param visitor The visitor to execute
     * @param params  The input parameters supplied to the visitor when its appropriate case is called.
     * @return The return value from executing the appropriate case on the visitor.
     */
    @Override
    public Object executeVisitor(ITaskVisitor visitor, Object... params) {
        return visitor.caseAt("MoveToLoc", this, params);
    }

    /**
     * When somebody changes text on JTree, check if the data is correct and, if it is, set this task's parameters
     * appropriately.
     *
     * @param object - parameters (as a String), should be roughly in the form: x, y
     */
    @Override
    public void setUserObject(Object object) {
        //parse the input, making sure they input 2 numbers separated by a comma
        String[] halves = ((String) object).split(",");
        if (halves.length != 2) {
            System.out.println("Did not input proper format for moving to a location.");
            return;
        }
        for (String half : halves) half.trim();
        try {
            destination = new Point2D.Double(Double.valueOf(halves[0]), Double.valueOf(halves[1]));
        } catch (NumberFormatException e) {
            System.out.println("Did not input proper format for moving to a location.");
            e.printStackTrace();
        }
    }

    /**
     * @return how to print this task, and how to draw it to the tree on GUI
     */
    public String toString() {
        return "Move to location: x = " + destination.getX() + ", y = " + destination.getY();
    }
}
