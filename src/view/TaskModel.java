package view;

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

public abstract class TaskModel extends DefaultTreeModel {
	
	private Vector<TreeModelListener> treeModelListeners = new Vector<TreeModelListener>();

	protected IExecuteTask rootTask;

    /**
     * Creates a tree in which any node can have children.
     *
     * @param root a TreeNode object that is the root of the tree
     * @see #DefaultTreeModel(javax.swing.tree.TreeNode, boolean)
     */
    public TaskModel(TreeNode root) {
        super(root);
    }

//	public TaskModel(IExecuteTask rootTask) {
//		this.rootTask = rootTask;
//	}
	
	public void setNewTask(IExecuteTask newRoot) {
		rootTask = newRoot;
		fireTreeStructureChanged(newRoot);
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

	////////////////TreeModel interface implementation ///////////////////////

	/**
	 * Adds a listener for the TreeModelEvent posted after the tree changes.
	 */
	public void addTreeModelListener(TreeModelListener l) {
		treeModelListeners.addElement(l);
	}

	/**
	 * Returns the child of parent at index in the parent's child array.
	 */
	public Object getChild(Object parent, int index) {
		IExecuteTask t = (IExecuteTask) parent;
		return t.getChild(index);
	}

	/**
	 * Returns the number of children of parent.
	 */
	public int getChildCount(Object parent) {
		IExecuteTask t = (IExecuteTask) parent;
		return t.getChildCount();
	}

	/**
	 * Returns the index of child in parent.
	 */
	public int getIndexOfChild(Object parent, Object child) {
		IExecuteTask p = (IExecuteTask) parent;
		IExecuteTask c = (IExecuteTask) child;
		//return p.getIndexOfChild((Person)child);
		return 0;
	}

	/**
	 * Returns the root of the tree.
	 */
	public Object getRoot() {
		return rootTask;
	}

	/**
	 * Returns true if node is a leaf.
	 */
	public boolean isLeaf(Object node) {
		IExecuteTask t = (IExecuteTask) node;
		return t.getChildCount() == 0;
	}

	/**
	 * Removes a listener previously added with addTreeModelListener().
	 */
	public void removeTreeModelListener(TreeModelListener l) {
		treeModelListeners.removeElement(l);
	}
	
	/**
	 * Messaged when the user has altered the value for the item
	 * identified by path to newValue.  Not used by this model.
	 */
	public abstract void valueForPathChanged(TreePath path, Object newValue);

}
