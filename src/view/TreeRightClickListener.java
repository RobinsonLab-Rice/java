package view;

import main.adapters.view.View2TaskAdapter;
import model.tasks.basictasks.IExecuteTask;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse adapter to handle right clicks on the task tree.
 * <p/>
 * Created by Christian on 6/20/2014.
 */
public class TreeRightClickListener extends MouseAdapter {

    private TaskTree taskTree;
    private View2TaskAdapter taskModel;

    public TreeRightClickListener(TaskTree taskTree, View2TaskAdapter taskModel) {
        this.taskTree = taskTree;
        this.taskModel = taskModel;
    }

    public void mousePressed(MouseEvent e) {
        TreePath selPath = taskTree.getPathForLocation(e.getX(), e.getY());
        //if our click was a right click
        if (e.getButton() == 3) {
            System.out.println("Right clicked on path " + selPath);
            //pop up with a contextual menu of buttons
            doPop(e, selPath);
        }
    }

    private void doPop(MouseEvent e, final TreePath selPath) {
        JPopupMenu menu = new JPopupMenu() {

            private static final long serialVersionUID = -3142513178293086540L;

            JMenuItem save
                    ,
                    delete
                    ,
                    insert;

            {
                save = new JMenuItem("Save");
                delete = new JMenuItem("Delete");
                insert = new JMenuItem("Insert After");

                save.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
//                        IView2SerializationAdapter.saveExecutionTask((IExecuteTask)selPath.getLastPathComponent(), txtTaskName.getText());
//                        updateCmb(SaveType.TASK, cmbSavedTasks);
                    }
                });

                /* Delete selected item from backend model (and tree). */
                delete.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ((DefaultTreeModel)taskTree.getModel()).removeNodeFromParent((IExecuteTask) selPath.getLastPathComponent());
                    }
                });
                insert.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //                         taskModelAdapter.insertAfterSelected(selPath.getPath(), (IExecuteTask) editingTree.model.getRoot());
                        //                         visualizationTree.model.setNewTask(taskModelAdapter.getTasks());
                    }
                });

                add(save);
                add(delete);
                add(insert);
            }
        };
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}
