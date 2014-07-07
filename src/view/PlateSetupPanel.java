package view;

import model.plate.PlateModel;
import model.plate.objects.PlateSpecifications;
import model.serialization.SaveType;
import model.serialization.SerializationModel;

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

    /* Adapters to back-end models. */
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
            }
        });

        /* When make plate button is pressed, package the current plate info and ship it off to the plate model. */
        makePlateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlateSpecifications specs = serializationModel.loadPlate(savedPlatesCmb.getSelectedItem().toString());
                boolean result = plateModel.addPlate(plateNicknameTxt.getText(), numberingOrderCmb.getSelectedItem().toString(),
                        new Point2D.Double(Double.parseDouble(xPosTxt.getText()), Double.parseDouble(yPosTxt.getText())), specs);
                //if we could not add the plate, tell the user
                if (result == false) {
                    JOptionPane.showMessageDialog(makePlateBtn, "Possibly same name as previously made plate, or in same location.",
                            "Could not add plate", JOptionPane.ERROR_MESSAGE);
                }
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
    public void start(final PlateModel plateModel, final SerializationModel serializationModel) {
        this.plateModel = plateModel;
        this.serializationModel = serializationModel;

        Iterable<String> savedPlates = serializationModel.updateDataList(SaveType.PLATE_SPEC, false);
        MainPanel.GUIHelper.updateCmb(savedPlates, savedPlatesCmb);
    }

}
