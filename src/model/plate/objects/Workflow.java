package model.plate.objects;

import model.plate.objects.Plate;
import model.tasks.basictasks.IExecuteTask;

/**
 * Wrapper class so we can serialize an entire workflow to JSON at once.
 * 
 * @author christianhenry
 *
 */
public class Workflow {
	
	public Iterable<Plate> plates;
	public IExecuteTask tasks;
	
	public Workflow(Iterable<Plate> plates, IExecuteTask tasks){
		this.plates = plates;
		this.tasks = tasks;
	}
}
