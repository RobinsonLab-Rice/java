package main.adapters.serial;

import view.MainPanel;

import java.util.ArrayList;

/**
 * Adapter to link the serial model to the view. All functions that the serial model needs the view
 * to perform are described here.
 * @author Christian
 *
 */
public class Serial2ViewAdapter {

    private MainPanel view;

    /* Sets up model references necessary for this adapter. */
    public Serial2ViewAdapter(MainPanel view) { this.view = view; }

    /**
	 * Gives the view a set of ports to display to the user.
	 * @param ports
	 */
	public void addPortsToList(ArrayList<String> ports){
        //TODO: this
    }
	
}
