package model.tasks;

import java.util.Arrays;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import model.tasks.basictasks.IExecuteTask;
import model.tasks.basictasks.MultiTask;

public class TaskTreeModel extends DefaultTreeModel {
	
	private Vector<TreeModelListener> treeModelListeners = new Vector<TreeModelListener>();

	protected IExecuteTask rootTask;

    /**
     * Creates a tree in which any node can have children.
     *
     * @param root a TreeNode object that is the root of the tree
     * @see #DefaultTreeModel(javax.swing.tree.TreeNode, boolean)
     */
    public TaskTreeModel(TreeNode root) {
        super(root);
    }

	////////////////Fire events //////////////////////////////////////////////

	/**
	 * The only event raised by this model is TreeStructureChanged with the
	 * root as path, i.e. the whole tree has changed.
	 */
	protected void fireTreeStructureChanged(IExecuteTask oldRoot) {
		int len = treeModelListeners.size();
		TreeModelEvent e = new TreeModelEvent(this,
				new Object[] {oldRoot});
		for (TreeModelListener tml : treeModelListeners) {
			tml.treeStructureChanged(e);
		}
	}

}
