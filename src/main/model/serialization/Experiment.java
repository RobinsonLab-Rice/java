package main.model.serialization;

import main.model.plate.objects.Plate;
import main.model.tasks.basictasks.IExecuteTask;

/**
 * Created by Christian on 8/7/2014.
 */
public class Experiment {

    public IExecuteTask task;

    public Iterable<Plate> plateList;

    public Experiment(IExecuteTask task, Iterable<Plate> plateList) {
        this.task = task;
        this.plateList = plateList;
    }
}
