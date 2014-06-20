package view;

import model.tasks.TaskTreeModel;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

public class TaskTree extends JTree {
	
	private static final long serialVersionUID = 7747201374454729420L;
	
	public TaskTreeModel model;
 
    public TaskTree(TaskTreeModel taskModel) {
        super(taskModel);
        model = taskModel;
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        Icon personIcon = null;
        renderer.setLeafIcon(personIcon);
        renderer.setClosedIcon(personIcon);
        renderer.setOpenIcon(personIcon);
        setCellRenderer(renderer);
    }
}