package view;

import main.adapters.view.View2SerializationAdapter;
import model.plate.objects.PlateSpecifications;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Christian on 6/12/2014.
 */
public class PlateEditingPanel extends JFrame {

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
    private JButton saveAsButton;
    private JTextField fileNameTxt;
    private JPanel contentPane;

    private View2SerializationAdapter serializationModel;

    public PlateEditingPanel() {

        /* When clear fields button is pressed, remove all entries from text fields. */
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

        /* When save button is pressed, send the info and file name off to the serialization model. */
        saveAsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serializationModel.saveSpecs(fileNameTxt.getText(), packagePlateSpecs());
            }
        });
    }

    public void start(View2SerializationAdapter serializationModel, String defaultPlate) {
        this.serializationModel = serializationModel;

        setContentPane(this.contentPane);
        setBounds(100, 100, 600, 600);
        pack();

        /* Get default plate specs and populate all the fields. */
        PlateSpecifications specs = serializationModel.loadPlateSpecs(defaultPlate);
        wellPosXTxt.setText(Double.toString(specs.getWellCorner().getX()));
        wellPosYTxt.setText(Double.toString(specs.getWellCorner().getY()));
        wellToWellDiameterTxt.setText(Double.toString(specs.getWellSpacing()));
        numRowsTxt.setText(Double.toString(specs.getNumRows()));
        numColsTxt.setText(Double.toString(specs.getNumCols()));
        widthTxt.setText(Double.toString(specs.getBorderDimensions().getY()));
        lengthTxt.setText(Double.toString(specs.getBorderDimensions().getX()));
        wellDiameterTxt.setText(Double.toString(specs.getWellDiameter()));
        wellVolumeTxt.setText(Double.toString(specs.getWellVolume()));
        wellHeightTxt.setText(Double.toString(specs.getWellDepth()));
        fileNameTxt.setText(defaultPlate);

        setVisible(true);
    }

    /**
     * Helper function that takes current spec fields and produces a new PlateSpecifications
     * object from them.
     * @return PlateSpecifications object with currently entered data
     */
	private PlateSpecifications packagePlateSpecs(){
		return new PlateSpecifications(
				Double.parseDouble(wellPosXTxt.getText()), Double.parseDouble(wellPosYTxt.getText()),
				Double.parseDouble(wellToWellDiameterTxt.getText()),
				Integer.parseInt(numRowsTxt.getText()),
				Integer.parseInt(numColsTxt.getText()),
				Double.parseDouble(widthTxt.getText()),
				Double.parseDouble(lengthTxt.getText()),
				Double.parseDouble(wellDiameterTxt.getText()),
				Double.parseDouble(wellVolumeTxt.getText()),
                Double.parseDouble(wellHeightTxt.getText()));
	}
}
