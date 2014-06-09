package view;

import model.plate.objects.PlateSpecifications;
import model.serialization.SaveType;

/**
 * Adapter to link the view to the plate model. All functions that the view needs the model
 * to perform are described here.
 * @author Christian
 *
 */
public interface SerializationAdapter {
	
	/**
	 * Tells plate model to save given plate specifications.
	 * @param nickname - name to later refer to this specification as
	 * @param plateSpecs - object encompassing all datasheet information
	 */
	public void saveSpecs(String nickname, PlateSpecifications plateSpecs);

	/**
	 * Loads the previously saved PlateSpecifications with the given filename.
	 * @param nickname - name of the file specifications are actually saved in
	 */
	public PlateSpecifications loadSpecs(String nickname);
	
	/**
	 * Load workflow from a file.
	 */
	public void loadWorkflow(String filename);
	
	/**
	 * Save workflow to a file.
	 */
	public void saveWorkflow(String filename);
	
	/**
	 * Deletes previously saved data that matches the given filename and type.
	 * @param filename - name of the data to delete
	 */
	public void deleteData(String filename, SaveType type);
	
	/**
	 * Gets all data of a certain type that are saved.
	 * @return Iterable of each file name
	 */
	public Iterable<String> updateDataList(SaveType type);
	
}
