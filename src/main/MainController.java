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

/**
 * Controller for our MVC system. Makes instances of model and view
 * when the program is run, and connects them via their appropriate
 * adapters (which are concrete classes in their own sub-packages here).
 */
public class MainController {

	private ExternalCommModel externalCommModel;
	private PlateModel plateModel;
	private SerialModel serialModel;
	private TaskModel taskModel;
	private SerializationModel serializationModel;
    private MainPanel view;

	
	/* Makes controller that initializes models and the view, using adapters to link them together. */
	public MainController(){

        view = new MainPanel(new View2PlateAdapter(plateModel), new View2SerializationAdapter(serializationModel),
                new View2SerialCommAdapter(serialModel), new View2TaskAdapter(taskModel));

		externalCommModel = new ExternalCommModel(new ExternalComm2TaskAdapter(taskModel));

		plateModel = new PlateModel(new Plate2ViewAdapter(view), new Plate2TaskAdapter(taskModel));

		serialModel = new SerialModel(new Serial2ViewAdapter(view), new Serial2TaskAdapter(taskModel),
                new Serial2PlateAdapter(plateModel));
		
		taskModel = new TaskModel(new Task2ViewAdapter(view), new Task2PlateAdapter(plateModel),
                new Task2SerialCommAdapter(serialModel));

		serializationModel = new SerializationModel(new Serialization2TaskAdapter(taskModel), new Serialization2PlateAdapter(plateModel));
	}
	
	/**
	 * First thing called from main, initializes model and view, linking them together, 
	 * before controller backs away.
	 */
	public void start(){
		view.start();
		externalCommModel.start();
		plateModel.start();
		serialModel.start();
		serializationModel.start();
	}
	
	/**
	 * Launch the application, called right when program starts.
	 */
	public static void main(String[] args) {
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
