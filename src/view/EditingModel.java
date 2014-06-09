package view;

import javax.swing.tree.TreePath;

import model.tasks.basictasks.IExecuteTask;

public class EditingModel extends TaskModel{

	public EditingModel(IExecuteTask rootTask) {
		super(rootTask);
	}
	
	/**
	 * Messaged when the user has altered the value for the item
	 * identified by path to newValue.  Not used by this model.
	 */
	public void valueForPathChanged(TreePath path, Object newValue) {
		System.out.println("*** valueForPathChanged in editing tree: " + path + " --> " + newValue);
		rootTask.traverseOrModify(path.getPath(), (String) newValue);
		fireTreeStructureChanged(rootTask);
	}

}
