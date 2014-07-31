package main.model.tasks.basictasks;

import main.model.plate.objects.ArmState;
import main.model.tasks.taskvisitors.ITaskVisitor;

import java.io.OutputStream;

/**
 * Created by Christian on 7/30/2014.
 */
public class PumpParamsTask extends ALeafTask {

    /**
     * Pump speed to set.
     */
    private int speed;

    /**
     * Pump acceleration to set.
     */
    private int acceleration;

    public PumpParamsTask(int speed, int acceleration) {
        this.speed = speed;
        this.acceleration = acceleration;
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
        String cmdString = "pumpParams(" + speed + "," + acceleration + ")";
        this.writeString(cmdString, outputStream);
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
        return visitor.caseAt("PumpParams", this, params);
    }

    /**
     * Parses input, setting first parameter to the speed, second to the acceleration.
     *
     * @param object
     */
    @Override
    public void setUserObject(Object object) {
        //parse the input, making sure they input 2 numbers separated by a comma
        String[] halves = ((String) object).split(",");
        if (halves.length != 2) {
            System.out.println("Did not input proper format for setting pump parameters!");
            return;
        }
        for (String half : halves) half.trim();

        speed = Integer.parseInt(halves[0]);
        acceleration = Integer.parseInt(halves[1]);
    }
}
