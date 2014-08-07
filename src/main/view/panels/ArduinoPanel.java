package main.view.panels;

import main.model.plate.PlateModel;
import main.model.serial.SerialModel;
import main.model.serialization.SerializationModel;
import main.model.tasks.TaskModel;
import main.view.dialogs.SimpleDialogs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel responsible for all control with Arduino.
 *
 * Created by Christian on 8/7/2014.
 */
public class ArduinoPanel extends JPanel {

    private SerialModel serialModel;
    private PlateModel plateModel;

    private JComboBox<String> serialPortsCmb;
    private JButton connectButton;
    private JTextField serialCommandTxt;
    private JButton sendButton;
    private JButton calibrateButton;
    private JButton scanForNewPortsButton;
    private JPanel arduinoPanel;
    private JButton disconnectButton;


    /**
     * Set up most buttons.
     */
    public ArduinoPanel() {

        /* Update the serialPortsCmb on button press. */
        scanForNewPortsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainPanel.GUIHelper.updateCmb(serialModel.scanForPorts(), serialPortsCmb);
            }
        });

        /* When connect button is pressed, make serial model connect to port name selected in the cmb. */
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (serialPortsCmb.getItemCount() == 0) {
                    SimpleDialogs.popNoSerialConnection(connectButton);
                }
                else {
                    serialModel.connectToPort(serialPortsCmb.getItemAt(serialPortsCmb.getSelectedIndex()));
                }

            }
        });

        /* When send button is pressed, send command as it is types over the serial model. */
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serialModel.sendText(serialCommandTxt.getText());
            }
        });

        /* On calibrate, send it to Arduino and reset the Java backend model too. */
        calibrateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serialModel.sendText("calibrate()");
                plateModel.calibrate();
            }
        });

        /* Disconnect from currently selected serial port. */
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serialModel.disconnectPort();
            }
        });
    }

    /**
     * Start method to set the models this will talk to.
     */
    public void start(MainPanel mainView, SerialModel serialModel, PlateModel plateModel) {
        this.serialModel = serialModel;
        this.plateModel = plateModel;

        //fill out ports on startup
        MainPanel.GUIHelper.updateCmb(serialModel.scanForPorts(), serialPortsCmb);

    }


}
