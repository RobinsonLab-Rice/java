package view;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import model.tasks.basictasks.IExecuteTask;

class MyTreeModelListener implements TreeModelListener {
    public void treeNodesChanged(TreeModelEvent e) {
        IExecuteTask node;
        node = (IExecuteTask)(e.getTreePath().getLastPathComponent());
        System.out.println("The user has finished editing a node.");
    }
    public void treeNodesInserted(TreeModelEvent e) {
    }
    public void treeNodesRemoved(TreeModelEvent e) {
    }
    public void treeStructureChanged(TreeModelEvent e) {
    }
}
