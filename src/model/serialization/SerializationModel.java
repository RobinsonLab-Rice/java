package model.serialization;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.adapters.serialization.Serialization2PlateAdapter;
import main.adapters.serialization.Serialization2TaskAdapter;
import model.plate.objects.Workflow;
import model.plate.objects.PlateSpecifications;
import model.tasks.basictasks.IExecuteTask;
import model.tasks.basictasks.MultiTask;

import org.apache.commons.io.FilenameUtils;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;

/**
 * Model that handles all serialization (saving to and loading data from files).
 * @author christianhenry
 *
 */
public class SerializationModel {

	/**
	 * Adapter to access task model.
	 */
	private Serialization2TaskAdapter taskModel;
	
	/**
	 * Adapter to access plate model.
	 */
	private Serialization2PlateAdapter plateModel;
	
	/**
	 * Extension to save files with.
	 */
	private String ext = ".txt";
	
	/**
	 * On initialization, connects to given adapters.
	 */
	public SerializationModel(Serialization2TaskAdapter taskModel, Serialization2PlateAdapter plateModel){
		this.taskModel = taskModel;
		this.plateModel = plateModel;
	}
	
	/**
	 * Called from controller on startup.
	 */
	public void start(){
		
	}
	
	/**
	 * Iterates through files in the appropriate data folder (depending on input SaveType), returning an iterable of each 
	 * piece of saved data in that folder.
	 * @return Iterable of previously saved data
	 */
	public Iterable<String> updateDataList(SaveType type){
		//define our filter
		FilenameFilter filter = new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				return (name.endsWith(ext));
			}
		};
		
		//get the correct directory based on input SaveType
		File dir = null;
		switch (type){
			case PLATE_SPEC: dir = new File("data/plates"); 		break;
			case WORKFLOW: dir = new File("data/workflow"); 		break;
			case TASK: dir = new File("data/tasks");				break;
			default: System.out.println("Not a valid save type."); 	break;
		}
 
		//if we can't find plate specification directory, say so
		if (dir.isDirectory() == false) {
			System.out.println("Directory does not exist.");
			return new ArrayList<String>();
		}
 
		//list all the files and filter by the extension
		String[] list = dir.list(filter);
 
		//if no files exist
		if (list.length == 0) {
			System.out.println("no files end with : " + ext);
			return new ArrayList<String>();
		}
 
		ArrayList<String> returnList = new ArrayList<String>();
		
		//add every file in our list to the returned ArrayList
		for (String file : list) {
			returnList.add(FilenameUtils.removeExtension(file));
		}
		
		return returnList;
	}
	
	/**
	 * Delete piece of data with given filename from appropriate folder (taken from input SaveType).
	 * @param filename - name of the file to delete, without folders or extension
	 * @param type - enum for the different types of data that can be saved
	 */
	public void deleteData(String filename, SaveType type) {
		File dataFile = null;
		switch (type){
			case PLATE_SPEC: dataFile = new File("data/plates/" + filename + ext);	break;
			case WORKFLOW: dataFile = new File("data/workflow/" + filename + ext);	break;
			default: System.out.println("Did not recognize the save data type!");	break;
		}
		dataFile.delete();
	}

	/**
	 * Loads workflow with given filename, giving it to the task and plate models to load back into the program.
	 * @param filename - name of file to load from, without folder or extension
	 */
	public void loadWorkflow(String filename) {
//		FileInputStream fis = null;
//	    ObjectInputStream in = null;
//	 
//	    try {
//	    	fis = new FileInputStream("data/workflow/" + filename + ext);
//	    	in = new ObjectInputStream(fis);
//	    	plateModel.setPlateList((ArrayList<Plate>) in.readObject());
//	    	taskModel.setTasks((MultiTask) in.readObject());
//	    	in.close();
//	    } 
//	    catch (Exception ex) {
//	    	ex.printStackTrace();
//	    }
		
		FileInputStream fis = null;
	    ObjectInputStream in = null;
	 
	    try {
	    	fis = new FileInputStream("data/workflow/" + filename + ext);
	    	in = new ObjectInputStream(fis);
	    	
	    	JsonReader jr = new JsonReader(in);
	    	Workflow toLoad = (Workflow) jr.readObject();
	    	plateModel.setPlateList(toLoad.plates);
	    	taskModel.setTasks((MultiTask)toLoad.tasks);
	    	
	    	in.close();
	    } 
	    catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	}
	
	/**
	 * Loads plate with the given filename, returning it to the view to be put in correct fields.
	 * @param name - filename to look for the specifications
	 */
	public PlateSpecifications loadPlate(String filename){
//		PlateSpecifications specs = null;
//		
//		FileInputStream fis = null;
//	    ObjectInputStream in = null;
//	 
//	    try {
//	    	fis = new FileInputStream("data/plates/" + name + ext);
//	    	in = new ObjectInputStream(fis);
//	    	specs = (PlateSpecifications) in.readObject();
//	    	in.close();
//	    } 
//	    catch (Exception ex) {
//	    	ex.printStackTrace();
//	    }
//	    return specs;
		
		FileInputStream fis = null;
	    //ObjectInputStream in = null;
	    
	    PlateSpecifications specs = null;
	 
	    try {
	    	fis = new FileInputStream("data/plates/" + filename + ext);
	    	
	    	JsonReader jr = new JsonReader(fis);
	    	specs = (PlateSpecifications) jr.readObject();
	    	
	    	fis.close();
	    	
	    	return specs;
	    } 
	    catch (Exception ex) {
	    	ex.printStackTrace();
	    	return null;
	    }
	}
	
	public IExecuteTask loadTask(String filename) {
		String qualifiedName = "data/tasks/" + filename + ext;
		
		FileInputStream fis = null;
	    //ObjectInputStream in = null;
	    
	    String json = "";
	 
	    try {
	    	fis = new FileInputStream("data/tasks/" + filename + ext);
	    	//in = new ObjectInputStream(fis);
	    	
	    	JsonReader jr = new JsonReader(fis);
	    	IExecuteTask task = (IExecuteTask) jr.readObject();
	    	
	    	fis.close();
	    	
	    	return task;
	    } 
	    catch (Exception ex) {
	    	ex.printStackTrace();
	    	return null;
	    }
	}
	
	/**
	 * Get current plates and taskQueue from plate and task models and save it to the input file.
	 * @param filename - name of location to save the file, without folder or extension
	 */
	public void saveWorkflow(String filename) {
		String qualifiedName = "data/workflow/" + filename + ext;
		saveItem(qualifiedName, new Workflow(plateModel.getPlateList(), taskModel.getTasks()));
	}
	
	/**
	 * Uses JSON to save the IExecuteTask to data/tasks/filename
	 * @param task - incoming task to persist
	 * @param filename - name of file to save task to
	 */
	public void saveTask(IExecuteTask task, String filename) {
		String qualifiedName = "data/tasks/" + filename + ext;
		saveItem(qualifiedName, task);
	}
	
	/**
	 * Uses JSON to save the PlateSpecifications to data/plates/filename
	 * @param plateSpecs - incoming plate specs to persist
	 * @param filename - name of file to save task to
	 */
	public void savePlate(String filename, PlateSpecifications plateSpecs){
		String qualifiedName = "data/plates/" + filename + ext;
		saveItem(qualifiedName, plateSpecs);
	}
	
	/**
	 * Common method to save any item to specified location.
	 */
	public void saveItem(String qualifiedName, Object item){
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(qualifiedName);
			Map args = new HashMap();
			args.put(JsonWriter.PRETTY_PRINT, true);
			JsonWriter jw = new JsonWriter(fos, args);
			jw.write(item);
			jw.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
