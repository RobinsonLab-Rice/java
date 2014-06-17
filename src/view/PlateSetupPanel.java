package view;

import main.adapters.view.View2PlateAdapter;
import main.adapters.view.View2SerializationAdapter;
import model.plate.objects.PlateSpecifications;
import model.serialization.SaveType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    /* Adapters to back-end models. */
    private View2PlateAdapter plateModel;
    private View2SerializationAdapter serializationModel;

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
                PlateSpecifications specs = serializationModel.loadPlateSpecs(savedPlatesCmb.getSelectedItem().toString());
                plateModel.addPlate(numberingOrderCmb.getSelectedItem().toString(), xPosTxt.getText(), yPosTxt.getText(), specs);
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
    public void start(final View2PlateAdapter plateModel, final View2SerializationAdapter serializationModel) {
        this.plateModel = plateModel;
        this.serializationModel = serializationModel;

        Iterable<String> savedPlates = serializationModel.updateDataList(SaveType.PLATE_SPEC);
        MainPanel.GUIHelper.updateCmb(savedPlates, savedPlatesCmb);
    }

}
