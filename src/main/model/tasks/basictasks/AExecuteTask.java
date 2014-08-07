package main.model.tasks.basictasks;

import javax.swing.tree.MutableTreeNode;

/**
 * Abstract class pulling out the common code between ALeafTask and MultiTasks.
 *
 * Created by Christian on 6/30/2014.
 */
public abstract class AExecuteTask implements IExecuteTask {

    /**
     * This task's parent, transient so that we don't save specific program references.
     */
    protected transient IExecuteTask parent;

    protected transient boolean isVisible = true;

    /**
     * Sets the parent of the receiver to <code>newParent</code>. Inherited from MutableTreeNode.
     *
     * @param newParent new node to set parent to
     */
    @Override
    public void setParent(MutableTreeNode newParent) {
        this.parent = (IExecuteTask) newParent;
    }

    /**
     * Removes the receiver from its parent. Inherited from MutableTreeNode.
     */
    @Override
    public void removeFromParent() {
        parent.remove(this);
    }

    /**
     * @return true if this task is visible to the user, false otherwise
     */
    public boolean getVisibility() {
        return isVisible;
    }
}
