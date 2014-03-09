package model.serial;

import java.util.ArrayList;

/**
 * Adapter to link the serial model to the view. All functions that the serial model needs the view
 * to perform are described here.
 * @author Christian
 *
 */
public interface ViewAdapter {

	/**
	 * Gives the view a set of ports to display to the user.
	 * @param ports
	 */
	public void addPortsToList(ArrayList<String> ports);
	
}
