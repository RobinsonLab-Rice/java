package main.view.panels;

import main.model.tasks.TaskModel;
import main.model.plate.PlateModel;
import main.model.serial.SerialModel;
import main.model.serialization.SerializationModel;
import main.view.MovementAreaMouseAdapter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Christian on 6/11/2014.
 */
public class MainPanel extends JFrame{

    /* All fields that this panel contains. */
    private JPanel contentPane;
    private JPanel modelPane;
    private JTabbedPane tabbedPane;

    private PlateSetupPanel plateSetupPanel;
    private TaskCreationPanel taskCreationPanel;
    private ArduinoPanel arduinoPanel;

    /* Adapters to back-end models. */
    private PlateModel plateModel;
    private SerializationModel serializationModel;
    private SerialModel serialModel;
    private TaskModel taskModel;
    private Double defaultDispense;

    /* Constructor that initializes adapters so the GUI can talk to the back-end models. */
    public MainPanel() {
    }

    /* Called after bindings have completed, set up frame to actually be displayed. */
    public void start(PlateModel plateModel, SerializationModel serializationModel, SerialModel serialModel, TaskModel taskModel) {

        /* Set the adapters */
        this.plateModel = plateModel;
        this.serializationModel = serializationModel;
        this.serialModel = serialModel;
        this.taskModel = taskModel;

        /* Start the sub-views */
        plateSetupPanel.start(this, plateModel, serializationModel);
        taskCreationPanel.start(this, taskModel, serializationModel);
        arduinoPanel.start(this, serialModel, plateModel);


        /* Do necessary communication with backend models */
        plateModel.setBorderFrame(serializationModel.getSavedBounds(), serializationModel.getNozzleHomePos(), modelPane);

        modelPane.addMouseListener(new MovementAreaMouseAdapter(plateModel, taskModel));

        /* Configure this main panel to start up */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(this.contentPane);
        setBounds(100, 100, 1000, 600);
        ImageIcon icon = new ImageIcon("src/images/HappySquirtle.png");
        setIconImage(icon.getImage());
        setVisible(true);
    }

    /* When a request is made, update all windows. */
    public void update() {
        contentPane.repaint();
    }

    /* Create GUI components that need more than the default constructor. */
    private void createUIComponents() {

        /* Panel system will actually draw objects on.*/
		modelPane = new JPanel() {
			private static final long serialVersionUID = 6634648507431653549L;

			/**
			* Overridden paintComponent method to paint a model of the aperture on.
			* @param g The Graphics object to paint on.
			**/
			public void paintComponent(Graphics g) {
			    super.paintComponent(g);   	// Do everything normally done first, e.g. clear the screen.
                plateModel.paintAll(g);     // call back to the model to paint the aperture
			}
		};
    }

    /**
     * @return default amount to dispense, from task creation panel
     */
    public String getDefaultDispense() {
        return taskCreationPanel.getDefaultDispense();
    }

    /**
     * @return name of default plate, from task creation panel
     */
    public String getDefaultPlate() {
        return taskCreationPanel.defaultPlate.getText();
    }

    /**
     * @param name new name of default plate to load when making tasks
     */
    public void setDefaultPlate(String name) {
        taskCreationPanel.defaultPlate.setText(name);
    }

    /* Helper class that contains convenience methods for this and other views. */
    public static class GUIHelper {

        /* Update the given combobox with the iterable of strings. */
        public static void updateCmb(Iterable items, JComboBox cmb){
            cmb.removeAllItems();
    		for (Object item : items){
    			cmb.addItem(item);
    		}
        }
    }
}
