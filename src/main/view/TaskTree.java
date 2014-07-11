package main.view;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

public class TaskTree extends JTree {

    private static final long serialVersionUID = 7747201374454729420L;

    public DefaultTreeModel model;

    public TaskTree(DefaultTreeModel taskModel) {
        super(taskModel);
        model = taskModel;
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        Icon personIcon = null;
        renderer.setLeafIcon(personIcon);
        renderer.setClosedIcon(personIcon);
        renderer.setOpenIcon(personIcon);
        setCellRenderer(renderer);
        setEditable(true);
        setDragEnabled(true);
        setDropMode(DropMode.INSERT);
        getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        setTransferHandler(new TreeTransferHandler());
    }
}