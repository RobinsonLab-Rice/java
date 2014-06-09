package main;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.tree.TreePath;

import view.MainGUI;
import model.externalcomm.ExternalCommModel;
import model.plate.PlateModel;
import model.plate.objects.ArmState;
import model.plate.objects.Plate;
import model.plate.objects.PlateNumbering;
import model.plate.objects.PlateSpecifications;
import model.serial.SerialModel;
import model.serialization.SaveType;
import model.serialization.SerializationModel;
import model.tasks.ExecutionParam;
import model.tasks.SetupParam;
import model.tasks.ITaskFactory;
import model.tasks.TaskModel;
import model.tasks.basictasks.IExecuteTask;
import model.tasks.basictasks.MultiTask;

//WHATS UP CHRISTIAN AND RAHUL DONT DO DRUGS STAY IN SCHOOL BE COOL WATCH OUT FOR KARL
/**
 * Controller for our M-V-C system. Makes instances of model and view
 * when the program is run, and connects them via their appropriate
 * adapters (initialized through anonymous inner classes).
 */
public class MainController {

	private ExternalCommModel externalCommModel;
	private PlateModel plateModel;
	private SerialModel serialModel;
	private TaskModel taskModel;
	private SerializationModel serializationModel;
	private MainGUI<ITaskFactory> view;
	
	/**
	 * Makes controller that initializes both models and the view, using anonymous inner classes to link them together. 
	 * For each adapter, description for each method lies in its interface.
	 */
	public MainController(){
		externalCommModel = new ExternalCommModel(new model.externalcomm.TaskAdapter(){

			@Override
			public void appendTaskToQueue(IExecuteTask taskToAdd) {
				taskModel.addToQueue(taskToAdd);
			}
			
		});
		
		/**
		 * Initializes plate model. Functions described here are ones that are called from the plate model 
		 * to invoke some function in the view.
		 */
		plateModel = new PlateModel(new model.plate.ViewAdapter() {
			@Override
			public void updateView(){
				view.update();
			}
		},	new model.plate.TaskAdapter(){
			@Override
			public void drawTasks(Graphics g, double sF) {
				taskModel.drawTasks(g, sF);
			}
		});
		
		/**
		 * Initializes serial model. Functions described here are ones that are called from the serial model 
		 * to invoke some function in the view.
		 */
		serialModel = new SerialModel(new model.serial.ViewAdapter() {
			@Override
			public void addPortsToList(ArrayList<String> ports){
				view.addPortsToList(ports);}
			}, 
			new model.serial.TaskAdapter(){
			@Override
			public void executeNext(){
				taskModel.executeNext();}
			}, new model.serial.PlateAdapter() {
				@Override
				public void resetArmPosition() {
					plateModel.setInternalPosition(0.0, 0.0);
				}
			}
		);
		
		taskModel = new TaskModel(new model.tasks.ViewAdapter(){

			@Override
			public void updateView() {
				view.update();
			}

			@Override
			public void setTask(MultiTask taskQueue) {
				view.setTask(taskQueue);
			}
		}, new model.tasks.PlateAdapter(){

			@Override
			public Point2D getLocationFromNumber(int wellNumber) {
				return plateModel.getLocationFromNumber(wellNumber);
			}

			@Override
			public ArmState getArmState() {
				return plateModel.getArmState();
			}

			@Override
			public Point2D getLocationFromScreen(Point point) {
				return plateModel.getLocationFromScreen(point);
			}
			
		}, new model.tasks.SerialCommAdapter(){

			@Override
			public OutputStream getOutputStream() {
				return serialModel.getOutputStream();
			}
			
		});
		
		/**
		 * Initializes view. Functions described here
		 * are ones that are called from the view to invoke some function in the model.
		 */
		view = new MainGUI<ITaskFactory>(new view.PlateAdapter() {
			@Override
			public void addPlate(PlateNumbering numberingOrder, Point2D _platePos, PlateSpecifications _specs){
				plateModel.addPlate(numberingOrder, _platePos, _specs);
			}
			@Override
			public void paint(Graphics g){
				plateModel.paintAll(g);
			}
			@Override
			public void setFrame(Point2D borderSize, Component canvas) {
				plateModel.setBorderFrame(borderSize, canvas);
			}
			@Override
			public void clearAllPlates(){
				plateModel.clearAllPlates();
			}
        }, new view.SerialCommAdapter(){
        	@Override
        	public void connectToPort(String _portName){
        		serialModel.connectToPort(_portName);
        	}
			@Override
			public void scanForPorts() {
				serialModel.scanForPorts();
			}
			@Override
			public void sendText(String command) {
				serialModel.sendText(command);
			}
        }, new view.TaskAdapter<ITaskFactory>(){
        	
			@Override
			public void addToQueue(ExecutionParam executeParams, SetupParam setupParams, String source, String destination) {
				taskModel.addToQueue(executeParams, setupParams, source, destination);
			}
			
			@Override
			public void addToQueue(ExecutionParam executeParams, SetupParam setupParams, Point source, Point destination) {
				taskModel.addToQueue(executeParams, setupParams, source, destination);
			}
			
			@Override
			public void addToQueue(IExecuteTask taskToAdd) {
				taskModel.addToQueue(taskToAdd);
			}
			
			@Override
			public void clearAllTasks(){
				taskModel.clearAllTasks();
			}

			@Override
			public void addSingleWellTask(int wellNum, int fluidAmount) {
				taskModel.addToQueue(wellNum, fluidAmount);
			}

			@Override
			public IExecuteTask getTasks() {
				return taskModel.getTasks();
			}

			@Override
			public void executeAll() {
				taskModel.executeAll();
			}

			@Override
			public void debugExecuteAll() {
				taskModel.debugExecuteAll();
			}

			@Override
			public void changeExecutionData(Object[] path, String newData) {
				taskModel.changeExecutionData(path, newData);
			}

			@Override
			public void deleteExecutionTask(Object[] path) {
				taskModel.deleteExecutionTask(path);
			}

			@Override
			public void insertAfterSelected(Object[] path, IExecuteTask taskToAdd) {
				taskModel.insertAfterSelected(path, taskToAdd);
			}
		
        }, new view.SerializationAdapter(){
        	@Override
			public void saveSpecs(String nickname, PlateSpecifications plateSpecs) {
				serializationModel.savePlate(nickname, plateSpecs);
			}
			@Override
			public PlateSpecifications loadSpecs(String nickname) {
				return serializationModel.loadPlate(nickname);
			}
			@Override
			public void loadWorkflow(String filename) {
				serializationModel.loadWorkflow(filename);
			}
			@Override
			public void saveWorkflow(String filename) {
				serializationModel.saveWorkflow(filename);
			}
			@Override
			public void deleteData(String filename, SaveType type) {
				serializationModel.deleteData(filename, type);
			}
			@Override
			public Iterable<String> updateDataList(SaveType type) {
				return serializationModel.updateDataList(type);
			}
			@Override
			public void saveExecutionTask(IExecuteTask task, String filename) {
				serializationModel.saveTask(task, filename);
			}
			@Override
			public IExecuteTask loadTask(String filename) {
				return serializationModel.loadTask(filename);
			}
        });
		
		serializationModel = new SerializationModel(new model.serialization.TaskAdapter(){
			@Override
			public MultiTask getTasks() {
				return taskModel.getTasks();
			}
			@Override
			public void setTasks(MultiTask taskQueue) {
				taskModel.setTasks(taskQueue);
			}
		}, new model.serialization.PlateAdapter(){
			@Override
			public Iterable<Plate> getPlateList() {
				return plateModel.getPlateList();
			}
			@Override
			public void setPlateList(Iterable<Plate> plates) {
				plateModel.setPlateList(plates);
			}
		});
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
