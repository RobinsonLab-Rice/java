package model.tasks;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import model.tasks.basictasks.IExecuteTask;
import model.tasks.basictasks.MultiTask;
import model.tasks.basictasks.NullTask;

import java.io.IOException;

/**
 * Factory that, given a task on creation, makes that task whenever make() is called.
 *
 * Created by Christian on 6/18/2014.
 */
public class TaskFactory implements ITaskFactory {

    private IExecuteTask toMake;

    /**
     * Constructor takes in type of task this factory will make.
     * @param toMake
     */
    public TaskFactory(IExecuteTask toMake) {
        this.toMake = toMake;
    }

    /**
     * Instantiate the specific IExecuteTask for which this factory is defined.
     *
     * @return An IExecuteTask instance.
     */
    @Override
    public IExecuteTask make() {
        //return a copy of the input task, using JSON for help
        try {
            return (IExecuteTask) JsonReader.jsonToJava(JsonWriter.objectToJson(toMake));
        } catch (IOException e) {
            System.out.println("Issue making deep copy of task in premade task factory.");
            e.printStackTrace();
            return new NullTask();
        }
    }

    /**
     * Print the task's clean name (shouldn't be seen as a factory).
     * @return the underlying task's class name, without package references
     */
    public String toString() {
        //display multitasks as their name, other tasks as their own class
        if (this.toMake instanceof MultiTask) return toMake.toString();
        else return toMake.getClass().getSimpleName();
    }
}
