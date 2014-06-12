package main.adapters.view;


import model.serial.SerialModel;

/**
 * Created by Christian on 6/12/2014.
 */
public class View2SerialCommAdapter {

    private SerialModel serialModel;

    /* Sets up model references necessary for this adapter. */
    public View2SerialCommAdapter(SerialModel serialModel) {

    }

    /**
     * When user wants to update ports they can choose from, get the serial model to send a new list.
     * @return list of port names
     */
    public Iterable<String> scanForPorts() {
        return serialModel.scanForPorts();
    }

    /**
     * Requests serial model to open and get connected to the currently selected port name.
     *
     * @param portName - string name of port to connect to
     */
    public void connectToPort(String portName) {
        serialModel.connectToPort(portName);
    }

    /**
     * Send text directly from GUI to Arduino.
     *
     * @param command - string to send the Arduino
     */
    public void sendText(String command) {
        serialModel.sendText(command);
    }
}
