package main.adapters.tasks;

import model.serial.SerialModel;

import java.io.OutputStream;

/**
 * Adapter that allows the task model to talk to the serial model through specified methods only.
 * @author Christian
 *
 */
public class Task2SerialCommAdapter {

    private SerialModel serialModel;

    /* Sets up model references necessary for this adapter. */
    public Task2SerialCommAdapter(SerialModel serialModel) { this.serialModel = serialModel; }

    /**
	 * @return the OutputStream being used to talk to the Arduino
	 */
	public OutputStream getOutputStream(){
        return serialModel.getOutputStream();
    }
}
