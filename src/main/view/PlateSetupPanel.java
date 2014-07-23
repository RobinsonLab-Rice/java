package main.view;

import main.model.plate.PlateModel;
import main.model.plate.objects.PlateSpecifications;
import main.model.serialization.SaveType;
import main.model.serialization.SerializationModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

/**
 * GUI sub-panel that handles all input for plate creation.
 *
 * Created by Christian on 6/10/2014.
 */
public class PlateSetupPanel {

    /* All fields that this panel contains. */
    private JComboBox savedPlatesCmb;
    private JButton editListButton;
    private JTextField xPosTxt;
    private JTextField yPosTxt;
    private JComboBox numberingOrderCmb;
    private JButton makePlateBtn;
    private JButton clearAllPlatesBtn;
    private JPanel plateSetupPanel;
    private JTextField plateNicknameTxt;
    private JButton deleteSavedPlateBtn;

    /* Adapters to back-end models. */
    private MainPanel view;
    private PlateModel plateModel;
    private SerializationModel serializationModel;

    /* Constructor that initializes special component needs. */
    public PlateSetupPanel() {

        /* When edit list button is pressed, launch the edit sub-window. */
        editListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlateEditingPanel editFrame = new PlateEditingPanel();
                editFrame.start(serializationModel, savedPlatesCmb.getSelectedItem().toString());
                MainPanel.GUIHelper.updateCmb(serializationModel.updateDataList(SaveType.PLATE_SPEC, false), savedPlatesCmb);
            }
        });

        /* When make plate button is pressed, package the current plate info and ship it off to the plate model. */
        makePlateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlateSpecifications specs = serializationModel.loadPlate(savedPlatesCmb.getSelectedItem().toString());
                boolean result = plateModel.addPlate(plateNicknameTxt.getText(),
                        new Point2D.Double(Double.parseDouble(xPosTxt.getText()), Double.parseDouble(yPosTxt.getText())), specs);
                //if we could not add the plate, tell the user
                if (result == false) {
                    JOptionPane.showMessageDialog(makePlateBtn, "Possibly same name as previously made plate, or in same location.",
                            "Could not add plate", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    view.setDefaultPlate(plateNicknameTxt.getText());
                }
            }
        });

        /* When button to delete saved plate is pressed, do just that. */
        deleteSavedPlateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serializationModel.deleteData(savedPlatesCmb.getSelectedItem().toString(), SaveType.PLATE_SPEC);
                MainPanel.GUIHelper.updateCmb(serializationModel.updateDataList(SaveType.PLATE_SPEC, false), savedPlatesCmb);
            }
        });

        /* When clear plates button is pressed, remove all plates from the backend model containing them. */
        clearAllPlatesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plateModel.clearAllPlates();
            }
        });
    }

    /* Perform necessary startup procedures (populating dropboxes, etc.) */
    public void start(final MainPanel view, final PlateModel plateModel, final SerializationModel serializationModel) {
        this.view = view;
        this.plateModel = plateModel;
        this.serializationModel = serializationModel;

        MainPanel.GUIHelper.updateCmb(serializationModel.updateDataList(SaveType.PLATE_SPEC, false), savedPlatesCmb);
    }

}
