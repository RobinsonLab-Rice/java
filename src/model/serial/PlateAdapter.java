package model.serial;

/**
 * Adapter that allows the serial model to talk to the plate model through specified methods.
 * @author Christian
 *
 */
public interface PlateAdapter {

	/**
	 * When the serial model receives word that the arm is calibrated, reset the arm position in plate model.
	 */
	public void resetArmPosition();
}