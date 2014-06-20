package model.tasks.basictasks;

import javax.swing.tree.TreeNode;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Abstract class, defines functionality for execute, which only the anonymous inner class will use. Thus, defines
 * the base functionality for execute for all other tasks to be nothing.
 * @author Christian
 */
public abstract class ALeafTask implements IExecuteTask {
	
	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = -8336180786535595266L;

    private IExecuteTask parent;

	/**
	 * Writes string to the serial output buffer.
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
	 * Serial tasks, by definition, don't have any children.
	 */
	public int getChildCount() {
		return 0;
	}
	
	/**
	 * A serial task does not have a child, so this will never be called.
	 */
	public IExecuteTask getChild(int index) {
		return null;
	}
	
	/**
	 * The parent of this task should be deleting it, not the task itself.
	 */
	@Override
	public void traverseOrDelete(Object[] path) {
		System.out.println("Something went wrong, a serial task is trying to delete itself.");
	}

	/**
	 * Only abstract tasks can be inserted into, should never get here.
	 */
	@Override
	public void traverseOrInsert(Object[] path, IExecuteTask taskToAdd) {
		System.out.println("Something went wrong, we are inserting into a Serial task.");
	}

    /**
     * Returns the parent <code>TreeNode</code> of the receiver.
     */
    @Override
    public TreeNode getParent() {
        return parent;
    }

    /**
     * Returns the index of <code>node</code> in the receivers children.
     * If the receiver does not contain <code>node</code>, -1 will be
     * returned.
     *
     * @param node
     */
    @Override
    public int getIndex(TreeNode node) {
        return -1;
    }

    /**
     * Returns true if the receiver allows children.
     */
    @Override
    public boolean getAllowsChildren() {
        return false;
    }

    /**
     * Returns true if the receiver is a leaf.
     */
    @Override
    public boolean isLeaf() {
        return true;
    }

    /**
     * Returns the children of the receiver as an <code>Enumeration</code>.
     */
    @Override
    public Enumeration children() {
        return null;
    }

    /**
     * Returns the child <code>TreeNode</code> at index
     * <code>childIndex</code>.
     *
     * @param childIndex
     */
    @Override
    public TreeNode getChildAt(int childIndex) {
        return null;
    }

    public void setParent(IExecuteTask parent) {
        this.parent = parent;
    }
	
}
