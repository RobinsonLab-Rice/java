package view;

import model.plate.PlateModel;
import model.serial.SerialModel;
import model.serialization.SerializationModel;
import model.tasks.TaskModel;

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

    /* Adapters to back-end models. */
    private PlateModel plateModel;
    private SerializationModel serializationModel;
    private SerialModel serialCommModel;
    private TaskModel taskModel;
    private Double defaultDispense;

    /* Constructor that initializes adapters so the GUI can talk to the back-end models. */
    public MainPanel() {
    }

    /* Called after bindings have completed, set up frame to actually be displayed. */
    public void start(PlateModel plateModel, SerializationModel serializationModel, SerialModel serialCommModel, TaskModel taskModel) {

        /* Set the adapters */
        this.plateModel = plateModel;
        this.serializationModel = serializationModel;
        this.serialCommModel = serialCommModel;
        this.taskModel = taskModel;

        /* Start the sub-views */
        plateSetupPanel.start(plateModel, serializationModel);
        taskCreationPanel.start(this, taskModel, serializationModel);

        /* Do necessary communication with backend models */
        plateModel.setBorderFrame(serializationModel.getSavedBounds(), modelPane);

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
    public Double getDefaultDispense() {
        return taskCreationPanel.getDefaultDispense();
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
