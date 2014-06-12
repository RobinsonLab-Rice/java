package main.adapters.serial;

import model.plate.PlateModel;

/**
 * Adapter that allows the serial model to talk to the plate model through specified methods.
 * @author Christian
 *
 */
public class Serial2PlateAdapter {

    private PlateModel plateModel;

    /* Sets up model references necessary for this adapter. */
    public Serial2PlateAdapter(PlateModel plateModel) { this.plateModel = plateModel; }

    /**
	 * When the serial model receives word that the arm is calibrated, reset the arm position in plate model.
	 */
	public void resetArmPosition(){
        plateModel.setInternalPosition(0,0);
    }
}