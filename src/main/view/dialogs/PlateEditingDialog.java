package main.view.dialogs;

import main.model.plate.objects.PlateSpecifications;
import main.model.serialization.SerializationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dialog that pops up when user wants to edit a plate. Loads the selected item's data, letting the user edit this
 * information to overwrite the original file, or save to a new file.
 */
public class PlateEditingDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField wellPosXTxt;
    private JTextField wellPosYTxt;
    private JTextField wellToWellDiameterTxt;
    private JTextField numRowsTxt;
    private JTextField numColsTxt;
    private JTextField widthTxt;
    private JTextField lengthTxt;
    private JTextField wellDiameterTxt;
    private JTextField wellVolumeTxt;
    private JTextField wellHeightTxt;
    private JButton clearAllFieldsButton;
    private JTextField specName;

    private SerializationModel serializationModel;

    public PlateEditingDialog(Component parent, SerializationModel serializationModel, String defaultName) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        pack();
        setLocationRelativeTo(parent);

        this.serializationModel = serializationModel;

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        //clear all fields on clear button press
        clearAllFieldsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wellPosXTxt.setText("");
                wellPosYTxt.setText("");
                wellToWellDiameterTxt.setText("");
                numRowsTxt.setText("");
                numColsTxt.setText("");
                widthTxt.setText("");
                lengthTxt.setText("");
                wellDiameterTxt.setText("");
                wellVolumeTxt.setText("");
                wellHeightTxt.setText("");
            }
        });

        //get the current data from serialization model
        PlateSpecifications defaultSpecs = serializationModel.loadPlate(defaultName);

        //fill in default values of fields
        wellPosXTxt.setText(String.valueOf(defaultSpecs.getWellCorner().getX()));
        wellPosYTxt.setText(String.valueOf(defaultSpecs.getWellCorner().getY()));
        wellToWellDiameterTxt.setText(String.valueOf(defaultSpecs.getWellSpacing()));
        numRowsTxt.setText(String.valueOf(defaultSpecs.getNumRows()));
        numColsTxt.setText(String.valueOf(defaultSpecs.getNumCols()));
        widthTxt.setText(String.valueOf(defaultSpecs.getBorderDimensions().getX()));
        lengthTxt.setText(String.valueOf(defaultSpecs.getBorderDimensions().getY()));
        wellDiameterTxt.setText(String.valueOf(defaultSpecs.getWellDiameter()));
        wellVolumeTxt.setText(String.valueOf(defaultSpecs.getWellVolume()));
        wellHeightTxt.setText(String.valueOf(defaultSpecs.getWellDepth()));
        specName.setText(defaultName);
    }

    //When this dialog closes, package the spec data and save it to the backend model (telling user if that is unsuccessful).
    public void showDialog() {
        setVisible(true);
        return;
    }

    //When user hits save, we still need to: check if the save name is valid, and do a popup if it is not.
    private void onOK() {
        //if the path name does already exist, popup an overwrite dialog. else continue
        if (serializationModel.checkData("data/plates/" + specName.getText() + ".txt")){
            PlateSpecifications packagedData = null;
            try {
                packagedData = new PlateSpecifications(
                        Double.parseDouble(wellPosXTxt.getText()),
                        Double.parseDouble(wellPosYTxt.getText()),
                        Double.parseDouble(wellToWellDiameterTxt.getText()),
                        Integer.parseInt(numRowsTxt.getText()),
                        Integer.parseInt(numColsTxt.getText()),
                        Double.parseDouble(widthTxt.getText()),
                        Double.parseDouble(lengthTxt.getText()),
                        Double.parseDouble(wellDiameterTxt.getText()),
                        Double.parseDouble(wellVolumeTxt.getText()),
                        Double.parseDouble(wellHeightTxt.getText()));
            }
            catch (NumberFormatException e) {
                SimpleDialogs.popSaveUnsuccessful(buttonOK);
                return;
            }
            //if data was not successfully saved, tell the user.
            if (serializationModel.savePlate(specName.getText(), packagedData) == false) {
                SimpleDialogs.popSaveUnsuccessful(buttonOK);
            }
        }
        setVisible(false);
        dispose();
    }

    private void onCancel() {
        setVisible(false);
        dispose();
    }
}
