package main.model.tasks.basictasks;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Abstract class, defines functionality for execute, which only the anonymous inner class will use. Thus, defines
 * the base functionality for execute for all other tasks to be nothing.
 * @author Christian
 */
public abstract class ALeafTask extends AExecuteTask {
	
	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = -8336180786535595266L;

    /**
     * Writes string to the serial output buffer. Handles exceptions and cases where no stream is selected.
     * @param string String to write to the buffer
     * @param stream Output stream to write to
     */
	protected void writeString(String string, OutputStream stream){
		if (stream == null){
			System.out.println("No stream selected, but would have sent: " + string);
		}
		else{
			System.out.println(string);
			for (int i = 0; i < string.length(); i++){
				try {
					stream.write(string.charAt(i));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

    /**
     * Leaf tasks don't have any children, nothing to do.
     */
    public void resetParents() {
        return;
    }

    /* ---METHODS INHERITED FROM TREENODE--- */

    /**
     * Adds <code>child</code> to the receiver at <code>index</code>.
     * <code>child</code> will be messaged with <code>setParent</code>.
     *
     * @param child
     * @param index
     */
    @Override
    public void insert(MutableTreeNode child, int index) {
        //cannot insert into a leaf
    }

    /**
     * Removes the child at <code>index</code> from the receiver.
     *
     * @param index
     */
    @Override
    public void remove(int index) {
        //cannot remove from a leaf
    }

    /**
     * Removes <code>node</code> from the receiver. <code>setParent</code>
     * will be messaged on <code>node</code>.
     *
     * @param node
     */
    @Override
    public void remove(MutableTreeNode node) {
        //cannot remove from a leaf
    }

    /* -----METHODS INHERITED FROM MUTABLETREENODE----- */

    /**
     * Returns the children of the receiver as an <code>Enumeration</code>.
     */
    @Override
    public Enumeration children() {
        return Collections.emptyEnumeration();
    }

    /**
     * @return false: leaf tasks, by definition, do not allow children.
     */
    @Override
    public boolean getAllowsChildren() {
        return false;
    }

    /**
     * Returns a NullTask, since leaf tasks do not have children.
     *
     * @param childIndex
     */
    @Override
    public TreeNode getChildAt(int childIndex) {
        return new NullTask();
    }
	
	/**
	 * @return 0: leaf tasks, by definition, don't have any children.
	 */
	public int getChildCount() {
		return 0;
	}

    /**
     * @return -1: leaf tasks do not have children
     */
    @Override
    public int getIndex(TreeNode node) {
        return -1;
    }

    /**
     * Returns the parent <code>TreeNode</code> of the receiver.
     */
    @Override
    public TreeNode getParent() {
        return parent;
    }

    /**
     * Returns true if the receiver is a leaf.
     */
    @Override
    public boolean isLeaf() {
        return true;
    }

    /**
     * Sets this task to be visible/hidden on the GUI.
     */
    @Override
    public void setVisibilityDown(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * Sets this task (and parent) to be visible/invisible.
     * @param isVisible true if task should be shown, false if not
     */
    public void setVisibilityUp(boolean isVisible) {
        this.isVisible = isVisible;
        if (isVisible) {
            parent.setVisibilityUp(true);
        }
    }

    /**
     * By default, do nothing for replacing things. This will be overwritten by objects that actually use it.
     */
    public void replaceAll(String variable, Object newValue) {
        return;
    }

    /**
     * By default, do nothing for replacing things. This will be overwritten by objects that actually use it.
     */
    public boolean replaceOne(String variable, Object newValue) {
        return false;
    }
}
