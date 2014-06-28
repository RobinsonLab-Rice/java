package main.adapters.tasks;

import com.sun.prism.PixelFormat;
import model.serialization.SaveType;
import model.serialization.SerializationModel;
import model.tasks.basictasks.IExecuteTask;

import java.util.ArrayList;

/**
 * Created by christianhenry on 6/24/14.
 */
public class Task2SerializationAdapter {

    private SerializationModel serializationModel;

    public Task2SerializationAdapter(SerializationModel serializationModel) {
        this.serializationModel = serializationModel;
    }

    public Iterable<IExecuteTask> getSavedTasks() {
        ArrayList<IExecuteTask> taskArrayList = new ArrayList<>();
        for (Object task : serializationModel.getSavedData(SaveType.TASK)){
            taskArrayList.add((IExecuteTask) task);
        }
        return taskArrayList;
    }
}
