package main.model.tasks.basictasks;

import main.model.plate.objects.ArmState;
import main.model.serialization.SerializationModel;
import main.model.tasks.taskvisitors.ITaskVisitor;

import java.io.OutputStream;

/**
 * Task used to delay operation for some amount of milliseconds.
 *
 * Created by christianhenry on 8/6/14.
 */
public class DelayTask extends ALeafTask {

    /**
     * Amount of time to delay, in milliseconds.
     */
    private String time = "t";

    /**
     * @param time amount of time (in milliseconds) to delay when this executes
     */
    public DelayTask(String time) {
        this.time = time;
    }

    @Override
    public void execute(ArmState armState, OutputStream outputStream) {
        String cmdString = "delay(" + time + ")";
        this.writeString(cmdString, outputStream);
    }

    @Override
    public Object executeVisitor(ITaskVisitor visitor, Object... params) {
        return visitor.caseAt("Delay", this, params);
    }

    @Override
    public void setUserObject(Object object) {
        time = (String) object;
    }

    public String toString() {
        return "Delay: " + time + "ms";
    }

    /**
     * If variable matches the stored delay string, replace delay value with newValue.
     * @param variable - if the task's volume matches this, change volume to newValue
     * @param newValue - new value of dispense
     */
    public void replaceAll(String variable, Object newValue) {
        replaceOne(variable, newValue);
    }

    /**
     * If variable matches the stored dispense string, replace dispense value with newValue.
     * @param variable - if the task's volume matches this, change volume to newValue
     * @param newValue - new value of dispense
     */
    public boolean replaceOne(String variable, Object newValue) {
        if (time.equals(variable)) {
            time = (String) newValue;
            return true;
        }
        else {
            return false;
        }
    }
}
