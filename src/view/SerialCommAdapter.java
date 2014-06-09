package view;

/**
 * Adapter to link the view to the serial model. All functions that the view needs the serial model
 * to perform are described here.
 * @author Christian
 *
 */
public interface SerialCommAdapter{

	/**
	 * When user wants to update ports they can choose from, get the serial model to send a new list.
	 */
	public void scanForPorts();
	
	/**
	 * Requests serial model to open and get connected to the currently selected port name.
	 * @param _portName - string name of port to connect to
	 */
	public void connectToPort(String _portName);
	
	/**
	 * Send text directly from GUI to Arduino.
	 * @param command - string to send the Arduino
	 */
	public void sendText(String command);

}
