package main.model.serialization;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.model.plate.PlateModel;
import main.model.tasks.TaskModel;
import main.model.tasks.basictasks.IExecuteTask;
import main.model.plate.objects.PlateSpecifications;

import main.view.dialogs.SimpleDialogs;
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
	private TaskModel taskModel;
	
	/**
	 * Adapter to access plate model.
	 */
	private PlateModel plateModel;
	
	/**
	 * Extension to save files with.
	 */
	private String ext = ".txt";

    /* Saved copy of user settings, so we don't have to dig through the file system when each piece is wanted. */
    private UserSettings userSettings;
	
	/**
	 * On initialization, connects to given adapters.
	 */
	public void start(TaskModel taskModel, PlateModel plateModel){
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
			case EXPERIMENT: dir = new File("data/experiments");    break;
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
            case TASK:       dataFile = new File("data/tasks/" + filename + ext);	    break;
			case PLATE_SPEC: dataFile = new File("data/plates/" + filename + ext);	    break;
			case EXPERIMENT: dataFile = new File("data/experiments/" + filename + ext);	break;
			default: System.out.println("Did not recognize the save data type!");	    return;
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
     * Loads task with given filename.
     * @param filename - name of file to load from, without folder or extension
     * @return
     */
    public IExecuteTask loadExperiment(String filename) {
        IExecuteTask task = (IExecuteTask) loadData("data/experiments/" + filename + ext);
        task.resetParents();
        return task;
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
     * Uses JSON to save the IExecuteTask (root of model) to data/experiments/name. Only multitasks can be saved,
     * and they are saved into a special folder that only saves/loads entire experiments (root of the task tree).
     * @param task - incoming root task to persist
     */
    public void saveExperiment(IExecuteTask task) {
        String qualifiedName = "data/experiments/" + task.toString() + ext;
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
        if (checkData(qualifiedName) == true) {
            int result = SimpleDialogs.popOverwriteDialog();
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
    public ArrayList<Object> getSavedData(SaveType saveType) {
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

    /**
     * Checks to see if item specified by qualifiedPath actually exists
     * @param qualifiedPath complete path of item to check
     * @return true if item does exist
     */
    public boolean checkData(String qualifiedPath) {
        File f = new File(qualifiedPath);
        if (f.exists() && !f.isDirectory()) {
            return true;
        }
        else return false;
    }
}
