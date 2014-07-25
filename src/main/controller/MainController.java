package main.controller;

import java.awt.EventQueue;

import main.model.tasks.TaskModel;
import main.view.panels.MainPanel;
import main.model.externalcomm.ExternalCommModel;
import main.model.plate.PlateModel;
import main.model.serial.SerialModel;
import main.model.serialization.SerializationModel;

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
        externalCommModel.start(taskModel);
        plateModel.start(view, taskModel);
        serialModel.start(view, taskModel, plateModel);
        serializationModel.start(taskModel, plateModel);
        taskModel.start(view, plateModel, serialModel, serializationModel);
        view.start(plateModel, serializationModel, serialModel, taskModel);
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
