package view;

import javax.swing.tree.TreePath;

import main.adapters.view.View2TaskAdapter;
import model.tasks.basictasks.IExecuteTask;

public class ExecutionModel extends TaskModel {

    View2TaskAdapter taskAdapter;

	public ExecutionModel(IExecuteTask rootTask, View2TaskAdapter taskAdapter) {
		super(rootTask);
		this.taskAdapter = taskAdapter;
	}
	
	/**
	 * Messaged when the user has altered the value for the item
	 * identified by path to newValue.  Not used by this model.
	 */
	public void valueForPathChanged(TreePath path, Object newValue) {
		System.out.println("*** valueForPathChanged in visualization tree: " + path + " --> " + newValue);
		taskAdapter.changeExecutionData(path.getPath(), (String) newValue);
		fireTreeStructureChanged(taskAdapter.getTasks());
	}

}
