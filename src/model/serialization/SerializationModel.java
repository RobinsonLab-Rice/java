package model.serialization;

import java.awt.geom.Point2D;
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

import javax.swing.*;

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

    /* Saved copy of user settings, so we don't have to dig through the file system when each piece is wanted. */
    private UserSettings userSettings;
	
	/**
	 * On initialization, connects to given adapters.
	 */
	public void start(Serialization2TaskAdapter taskModel, Serialization2PlateAdapter plateModel){
        this.taskModel = taskModel;
        this.plateModel = plateModel;

        userSettings = (UserSettings) loadData("data/UserSettings" + ext);
	}
	
	/**
	 * Iterates through files in the appropriate data folder (depending on input SaveType), returning an iterable of each 
	 * piece of saved data in that folder.
	 * @return Iterable of previously saved data
	 */
	public Iterable<String> updateDataList(SaveType type, boolean returnFullName){
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
            if (returnFullName) returnList.add(dir + "/" + file);
            else returnList.add(FilenameUtils.removeExtension(file));
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
     * Uses JSON.io to read data from saved text files. Essentially a helper method for other accessors.
     * @param qualifiedPath - complete path to the data
     * @return de-serialized data (not yet cast to the correct object)
     */
    public Object loadData(String qualifiedPath) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(qualifiedPath);

            JsonReader jr = new JsonReader(fis);
            Object loaded = jr.readObject();

            fis.close();

            return loaded;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

	/**
	 * Loads workflow with given filename, giving it to the task and plate models to load back into the program.
	 * @param filename - name of file to load from, without folder or extension
	 */
	public void loadWorkflow(String filename) {
		Workflow loaded = (Workflow) loadData("data/workflow/" + filename + ext);
        plateModel.setPlateList(loaded.plates);
        taskModel.setTasks((MultiTask)loaded.tasks);
	}
	
	/**
	 * Loads plate with the given filename, returning it to the view to be put in correct fields.
	 * @param filename - name of file to load from, without folder or extension
	 */
	public PlateSpecifications loadPlate(String filename){
		return (PlateSpecifications) loadData("data/plates/" + filename + ext);
	}

    /**
     * Loads task with given filename.
     * @param filename - name of file to load from, without folder or extension
     * @return
     */
	public IExecuteTask loadTask(String filename) {
        IExecuteTask task = (IExecuteTask) loadData("data/tasks/" + filename + ext);
        task.resetParents();
        return task;
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
	 * Uses JSON to save the IExecuteTask to data/tasks/name. Only multitasks can be saved, so the
     * name to save the task as is just taken to be the name of the multitask.
     *
	 * @param task - incoming task to persist
	 */
	public void saveTask(IExecuteTask task) {
		String qualifiedName = "data/tasks/" + task.toString() + ext;
		saveItem(qualifiedName, task);
	}
	
	/**
	 * Uses JSON to save the PlateSpecifications to data/plates/filename
	 * @param plateSpecs - incoming plate specs to persist
	 * @param filename - name of file to save task to
     * @return whether or not the save was successful
	 */
	public boolean savePlate(String filename, PlateSpecifications plateSpecs){
		String qualifiedName = "data/plates/" + filename + ext;
		return saveItem(qualifiedName, plateSpecs);
	}
	
	/**
	 * Common method to save any item to specified location.
     * @return whether or not the save was successful
	 */
	public boolean saveItem(String qualifiedName, Object item){
		FileOutputStream fos = null;

        //if the item already exists, ask the user if they really want to continue
        if (loadData(qualifiedName) != null) {
            int result = JOptionPane.showOptionDialog(null,
                    "Saved item with that name already exists. Overwrite it?",
                    "Overwrite existing?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,     //do not use a custom Icon
                    new String[] {"Save Anyway", "Cancel"},  //the titles of buttons
                    "Save Anyway"); //default button title
            if (result != JOptionPane.YES_OPTION) {
                return false;
            }
        }

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
            return false;
		}
        return true;
	}

    /**
     * Gets the saved data (plates, tasks, workflow, etc) of type saveType.
     * @param saveType enum of different possible types of data
     * @return iterable of the saved data
     */
    public Iterable<Object> getSavedData(SaveType saveType) {
        ArrayList<Object> savedData = new ArrayList<Object>();
        //for each file name found in updateDataList, call loadData on that and get the resulting saved object
        for (String location : updateDataList(saveType, true)) {
            savedData.add(loadData(location));
        }
        return savedData;
    }

    /* Returns saved bounds that were loaded on program startup. */
    public Point2D getSavedBounds() {
        return userSettings.frameBounds;
    }
}
