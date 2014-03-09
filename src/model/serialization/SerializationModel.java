package model.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import model.plate.objects.Plate;
import model.plate.objects.PlateSpecifications;
import model.tasks.basictasks.MultiTask;

import org.apache.commons.io.FilenameUtils;

/**
 * Model that handles all serialization (saving to and loading data from files).
 * @author christianhenry
 *
 */
public class SerializationModel {

	/**
	 * Adapter to access task model.
	 */
	private TaskAdapter taskModel;
	
	/**
	 * Adapter to access plate model.
	 */
	private PlateAdapter plateModel;
	
	/**
	 * Extension to save files with.
	 */
	private String ext = ".txt";
	
	/**
	 * On initialization, connects to given adapters.
	 */
	public SerializationModel(TaskAdapter taskModel, PlateAdapter plateModel){
		this.taskModel = taskModel;
		this.plateModel = plateModel;
	}
	
	/**
	 * Called from controller on startup.
	 */
	public void start(){
		
	}
	
	/**
	 * Saves the given plate specifications into a file in the plates data folder.
	 * @param nickname - name to later refer to this plate spec, for loading
	 * @param plateSpecs - object encompassing all datasheet information
	 */
	public void savePlate(String nickname, PlateSpecifications plateSpecs){
		String filename = "data/plates/" + nickname + ext;
		
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try {
			fos = new FileOutputStream(filename);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(plateSpecs);
			oos.close();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Loads plate with the given filename, returning it to the view to be put in correct fields.
	 * @param name - filename to look for the specifications
	 */
	public PlateSpecifications loadPlate(String name){
		PlateSpecifications specs = null;
		
		FileInputStream fis = null;
	    ObjectInputStream in = null;
	 
	    try {
	    	fis = new FileInputStream("data/plates/" + name + ext);
	    	in = new ObjectInputStream(fis);
	    	specs = (PlateSpecifications) in.readObject();
	    	in.close();
	    } 
	    catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	    return specs;
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
	 * Loads workflow with given filename, giving it to the task and plate models to load back into the program.
	 * @param filename - name of file to load from, without folder or extension
	 */
	public void loadWorkflow(String filename) {
		FileInputStream fis = null;
	    ObjectInputStream in = null;
	 
	    try {
	    	fis = new FileInputStream("data/workflow/" + filename + ext);
	    	in = new ObjectInputStream(fis);
	    	plateModel.setPlateList((ArrayList<Plate>) in.readObject());
	    	taskModel.setTasks((MultiTask) in.readObject());
	    	in.close();
	    } 
	    catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	}

	/**
	 * Get current plates and taskQueue from plate and task models and save it to the input file.
	 * @param filename - name of location to save the file, without folder or extension
	 */
	public void saveWorkflow(String filename) {
		String qualifiedName = "data/workflow/" + filename + ext;
		
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try {
			fos = new FileOutputStream(qualifiedName);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(plateModel.getPlateList());
			oos.writeObject(taskModel.getTasks());
			oos.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
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
}
