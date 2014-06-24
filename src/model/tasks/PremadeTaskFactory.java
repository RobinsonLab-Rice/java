package model.tasks;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import model.tasks.basictasks.IExecuteTask;
import model.tasks.basictasks.NullTask;

import java.io.IOException;

/**
 * Created by Christian on 6/18/2014.
 */
public class PremadeTaskFactory implements ITaskFactory {

    private IExecuteTask toMake;

    /**
     * Constructor takes in type of task this factory will make.
     * @param toMake
     */
    public PremadeTaskFactory(IExecuteTask toMake) {
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

    public String toString() {
        return toMake.getClass().getSimpleName();
    }


}
