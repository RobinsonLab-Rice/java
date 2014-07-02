package main;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.util.ArrayList;

import main.adapters.externalcomm.ExternalComm2TaskAdapter;
import main.adapters.plate.Plate2TaskAdapter;
import main.adapters.plate.Plate2ViewAdapter;
import main.adapters.serial.Serial2PlateAdapter;
import main.adapters.serial.Serial2TaskAdapter;
import main.adapters.serial.Serial2ViewAdapter;
import main.adapters.serialization.Serialization2PlateAdapter;
import main.adapters.serialization.Serialization2TaskAdapter;
import main.adapters.tasks.Task2PlateAdapter;
import main.adapters.tasks.Task2SerialCommAdapter;
import main.adapters.tasks.Task2SerializationAdapter;
import main.adapters.tasks.Task2ViewAdapter;
import main.adapters.view.View2PlateAdapter;
import main.adapters.view.View2SerialCommAdapter;
import main.adapters.view.View2SerializationAdapter;
import main.adapters.view.View2TaskAdapter;
import model.externalcomm.ExternalCommModel;
import model.plate.PlateModel;
import model.serial.SerialModel;
import model.serialization.SerializationModel;
import model.tasks.TaskModel;
import view.MainPanel;

import javax.swing.*;

/**
 * Controller for our MVC system. Makes instances of model and view
 * when the program is run, and connects them via their appropriate
 * adapters (which are concrete classes in their own sub-packages here).
 */
public class MainController {

	public ExternalCommModel externalCommModel;
	public PlateModel plateModel;
    public SerialModel serialModel;
    public TaskModel taskModel;
    public SerializationModel serializationModel;
    public MainPanel view;

	
	/* Makes controller that initializes models and the view, using adapters to link them together. */
	public MainController(){

		externalCommModel = new ExternalCommModel();

		plateModel = new PlateModel();

		serialModel = new SerialModel();
		
		taskModel = new TaskModel();

		serializationModel = new SerializationModel();

        view = new MainPanel();
	}
	
	/**
	 * First thing called from main after models and views have been initialized. Since they are created, can now link
     * them together. Each start function takes in adapters to other models to accomplish that.
	 */
	public void start(){
		externalCommModel.start(new ExternalComm2TaskAdapter(taskModel));
		plateModel.start(new Plate2ViewAdapter(view), new Plate2TaskAdapter(taskModel));
		serialModel.start(new Serial2ViewAdapter(view), new Serial2TaskAdapter(taskModel),
                    new Serial2PlateAdapter(plateModel));
		serializationModel.start(new Serialization2TaskAdapter(taskModel), new Serialization2PlateAdapter(plateModel));
        taskModel.start(new Task2ViewAdapter(view), new Task2PlateAdapter(plateModel),
                    new Task2SerialCommAdapter(serialModel), new Task2SerializationAdapter(serializationModel));
        view.start( new View2PlateAdapter(plateModel), new View2SerializationAdapter(serializationModel),
                new View2SerialCommAdapter(serialModel), new View2TaskAdapter(taskModel));
	}
	
	/**
	 * Launch the application, called right when program starts.
	 */
	public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainController().start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
