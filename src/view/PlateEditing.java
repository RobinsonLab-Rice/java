package view;

import main.adapters.view.View2SerializationAdapter;
import model.plate.objects.PlateSpecifications;

import javax.swing.*;

/**
 * Created by Christian on 6/12/2014.
 */
public class PlateEditing extends JFrame {

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

    public void start(View2SerializationAdapter serializationModel, String defaultPlate) {
        this.serializationModel = serializationModel;

        setContentPane(this.contentPane);
        setBounds(100, 100, 600, 600);
        pack();

        /* Get default plate specs and populate all the fields. */
        PlateSpecifications specs = serializationModel.loadSpecs(defaultPlate);
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
}
