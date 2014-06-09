package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import model.serialization.SaveType;
import model.tasks.basictasks.DispenseTask;
import model.tasks.basictasks.IExecuteTask;
import model.tasks.basictasks.MLDRTask;
import model.tasks.basictasks.MoveTask;
import model.tasks.basictasks.MultiTask;
import model.tasks.basictasks.LowerTask;
import model.tasks.basictasks.RaiseTask;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.JTextField;

public class TaskCreationPanel extends JPanel {
	
	private static final long serialVersionUID = 1016253664207758803L;

	private TaskTree visualizationTree;
	
	private TaskTree editingTree;
	
	private TaskAdapter taskModelAdapter;
	
	private SerializationAdapter serializationAdapter;
	
	private JComboBox cmbSavedTasks;
	private JComboBox cmbWorkflows;
	private JTextField txtWorkflow;
	private JTextField txtTaskName;

	/**
	 * Create the panel.
	 */
	public TaskCreationPanel(final TaskAdapter taskModelAdapter, final SerializationAdapter serializationAdapter) {
		this.taskModelAdapter = taskModelAdapter;
		this.serializationAdapter = serializationAdapter;
		MultiTask startingTask = (MultiTask) taskModelAdapter.getTasks();
		visualizationTree = new TaskTree(new VisualizationModel(startingTask, taskModelAdapter));
		visualizationTree.setEditable(true);
		visualizationTree.model.addTreeModelListener(new MyTreeModelListener());
		
		//mouse adapter to handle right clicks on the task tree
		MouseAdapter ml = new MouseAdapter() {
			
		     public void mousePressed(MouseEvent e) {
		         int selRow = visualizationTree.getRowForLocation(e.getX(), e.getY());
		         TreePath selPath = visualizationTree.getPathForLocation(e.getX(), e.getY());
		         //if our click was a right click
		         if (e.getButton() == 3){
		        	 System.out.println("Right clicked on path " + selPath);
		        	 //pop up with a contextual menu of buttons
		        	 doPop(e, selPath);
		         }
		     }
		     
		     private void doPop(MouseEvent e, final TreePath selPath){
		    	 JPopupMenu menu = new JPopupMenu() {
		    		 
					private static final long serialVersionUID = -3142513178293086540L;
					
					JMenuItem save, delete, insert;
		    		 {
		    			 save = new JMenuItem("Save");
		    			 delete = new JMenuItem("Delete");
		    			 insert = new JMenuItem("Insert After");
		    			 
		    			 save.addActionListener(new ActionListener() {
		    				 public void actionPerformed(ActionEvent e) {
		    					 serializationAdapter.saveExecutionTask((IExecuteTask)selPath.getLastPathComponent(), txtTaskName.getText());
		    					 updateCmb(SaveType.TASK, cmbSavedTasks);
		    				 }
		    			 });
		    			 delete.addActionListener(new ActionListener() {
		    				 public void actionPerformed(ActionEvent e) {
		    					 taskModelAdapter.deleteExecutionTask(selPath.getPath());
		    					 visualizationTree.model.setNewTask(taskModelAdapter.getTasks());
		    				 }
		    			 });
		    			 insert.addActionListener(new ActionListener() {
		    				 public void actionPerformed(ActionEvent e) {
		    					 taskModelAdapter.insertAfterSelected(selPath.getPath(), (IExecuteTask) editingTree.model.getRoot());
		    					 visualizationTree.model.setNewTask(taskModelAdapter.getTasks());
		    				 }
		    			 });
		    			 
		    			 add(save);
		    			 add(delete);
		    			 add(insert);
		    		 }
		    	 };
		    	 menu.show(e.getComponent(), e.getX(), e.getY());
		     }
		};
		visualizationTree.addMouseListener(ml);
		
		MLDRTask editStartingTask = new MLDRTask(0,0);
		editingTree = new TaskTree(new EditingModel(editStartingTask));
		editingTree.setEditable(true);
		editingTree.model.addTreeModelListener(new MyTreeModelListener());
		
		setLayout(new MigLayout("", "[grow][grow]", "[][][][][grow][][][]"));
		
		JButton btnSaveWorkflow = new JButton("Save Workflow");
		btnSaveWorkflow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serializationAdapter.saveWorkflow(txtWorkflow.getText());
				updateCmb(SaveType.WORKFLOW, cmbWorkflows);
			}
		});
		
		txtWorkflow = new JTextField();
		add(txtWorkflow, "cell 0 0,growx");
		txtWorkflow.setColumns(10);
		
		cmbWorkflows = new JComboBox();
		add(cmbWorkflows, "cell 1 0,growx");
		add(btnSaveWorkflow, "cell 0 2,growx");
		
		JButton btnLoadWorkflow = new JButton("Load Workflow");
		btnLoadWorkflow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serializationAdapter.loadWorkflow((String)cmbWorkflows.getSelectedItem());
			}
		});
		add(btnLoadWorkflow, "cell 1 2,growx");
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane, "cell 0 4 2 1,grow");
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new MigLayout("", "[grow][grow]", "[][][grow][][]"));
		
		JLabel lblTaskToAdd = new JLabel("Task To Add");
		panel.add(lblTaskToAdd, "cell 0 0 1 2,alignx trailing");
		
		final JComboBox<String> cmbTasks = new JComboBox<String>();
		cmbTasks.setToolTipText("Basic tasks");
		cmbTasks.addItem("MultiTask");
		cmbTasks.addItem("MLDR Task");
		cmbTasks.addItem("Dispense");
		cmbTasks.addItem("Move");
		
		cmbTasks.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//when something is selected in this combobox, add a placeholder copy of the task to the
		    	//visualization tree
		        String selected = (String) cmbTasks.getSelectedItem();
		        if (selected == "MLDR Task") {
		        	editingTree.model.setNewTask(new MLDRTask(0,0));
		        }
		        else if (selected == "MultiTask") {
		        	editingTree.model.setNewTask(new MultiTask());
		        }
		        else if (selected == "Dispense") {
		        	editingTree.model.setNewTask(new DispenseTask(500));
		        }
		        else if (selected == "Move") {
		        	editingTree.model.setNewTask(new MoveTask(2));
		        }
		        else if (selected == "Lower") {
		        	editingTree.model.setNewTask(new LowerTask());
		        }
		        else if (selected == "Raise") {
		        	editingTree.model.setNewTask(new RaiseTask());
		        }
		        else {
		        	System.out.println("Could not recognize task selected in combobox.");
		        }
		    }
		});
		panel.add(cmbTasks, "cell 1 0,growx");
		
		cmbSavedTasks = new JComboBox();
		cmbSavedTasks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editingTree.model.setNewTask(serializationAdapter.loadTask((String)cmbSavedTasks.getSelectedItem()));
			}
		});
		cmbSavedTasks.setToolTipText("User made tasks");
		panel.add(cmbSavedTasks, "cell 1 1,growx");
		
		JScrollPane pnlEditingTree = new JScrollPane(editingTree);
		panel.add(pnlEditingTree, "cell 0 2 2 1,grow");
		
		JButton btnAppendToEnd = new JButton("Append To End");
		btnAppendToEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//add the task in editing view to the backend
				taskModelAdapter.addToQueue((IExecuteTask)editingTree.model.getRoot());
				//slap the new backend model into our visualization tree
				visualizationTree.model.setNewTask(taskModelAdapter.getTasks());
			}
		});
		
		JLabel lblNameToSave = new JLabel("Name to save task as");
		panel.add(lblNameToSave, "cell 0 3,alignx trailing");
		
		txtTaskName = new JTextField();
		panel.add(txtTaskName, "cell 1 3,growx");
		txtTaskName.setColumns(10);
		panel.add(btnAppendToEnd, "cell 0 4 2 1,growx");
		
		JScrollPane pnlTaskTree = new JScrollPane(visualizationTree);
		splitPane.setLeftComponent(pnlTaskTree);
		
		JButton btnExecuteSelected = new JButton("Execute Selected Sub-Tree");
		btnExecuteSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		add(btnExecuteSelected, "cell 0 5 2 1,growx");
		
		JButton btnExecuteAll = new JButton("Execute All");
		btnExecuteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				taskModelAdapter.executeAll();
			}
		});
		add(btnExecuteAll, "cell 0 6,growx");
		
		JButton btnDebugExecuteAll = new JButton("Debug Execute All");
		btnDebugExecuteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				taskModelAdapter.debugExecuteAll();
			}
		});
		add(btnDebugExecuteAll, "cell 1 6,growx");

	}
	
	public void start() {
		//update the task dropdown list
		updateCmb(SaveType.TASK, cmbSavedTasks);
		//update the workflow dropdown list
		updateCmb(SaveType.WORKFLOW, cmbWorkflows);
	}
	
	public void updateCmb(SaveType saveType, JComboBox cmb) {
		Iterable<String> filenames = serializationAdapter.updateDataList(saveType);
		cmb.removeAllItems();
		for (String filename : filenames){
			System.out.println("filename");
			cmb.addItem(filename);
		}
	}

	public void setTask(MultiTask taskQueue) {
		visualizationTree.model.setNewTask(taskQueue);
	}
}
