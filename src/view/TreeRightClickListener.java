package view;

import main.adapters.view.View2SerializationAdapter;
import main.adapters.view.View2TaskAdapter;
import model.tasks.ITaskFactory;
import model.tasks.basictasks.IExecuteTask;
import model.tasks.basictasks.MultiTask;

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
    private View2SerializationAdapter serializationModel;
    private JComboBox savedTasksCmb;

    public TreeRightClickListener(JComboBox savedTasksCmb, TaskTree taskTree,
                                  View2TaskAdapter taskModel, View2SerializationAdapter serializationModel) {
        this.savedTasksCmb = savedTasksCmb;
        this.taskTree = taskTree;
        this.taskModel = taskModel;
        this.serializationModel = serializationModel;
    }

    public void mousePressed(MouseEvent e) {
        TreePath selPath = taskTree.getPathForLocation(e.getX(), e.getY());
        //if our click was a right click
        if (e.getButton() == 3) {
            setSelectedOnRightClick(e);
            //pop up with a contextual menu of buttons
            doPop(e, selPath);
        }
    }

    private void doPop(MouseEvent e, final TreePath selPath) {
        JPopupMenu menu = new JPopupMenu() {

            private static final long serialVersionUID = -3142513178293086540L;

            JMenuItem save
                    ,
                    delete;

            {
                save = new JMenuItem("Save");
                delete = new JMenuItem("Delete");

                /* Only allow saving on multitasks. Saves it as the multitask's current name. */
                save.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //save the task to file system
                        serializationModel.saveExecutionTask((IExecuteTask) selPath.getLastPathComponent());
                        //update the combobox of tasks so this is shown
//                        savedTasksCmb.removeAllItems();
//                        for (ITaskFactory factory : taskModel.getTaskFactories()){
//                            savedTasksCmb.addItem(factory);
//                        }
                        MainPanel.GUIHelper.updateCmb(taskModel.getTaskFactories(), savedTasksCmb);
                    }
                });

                /* Delete selected item from backend model (and tree). */
                delete.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    IExecuteTask toDelete = (IExecuteTask) taskTree.getSelectionPath().getLastPathComponent();
                    if (toDelete.getParent() == null)   //no parent means it is the root
                        ((DefaultTreeModel) taskTree.getModel()).setRoot(new MultiTask());
                    else                                //for all other nodes, remove them from parent
                        ((DefaultTreeModel) taskTree.getModel()).removeNodeFromParent(toDelete);
                    }
                });

                //make a different menu for multitasks and other tasks
                if (selPath.getLastPathComponent() instanceof MultiTask){
                    add(save);
                    add(delete);
                }
                else {
                    add(delete);
                }

            }
        };
        menu.show(e.getComponent(), e.getX(), e.getY());
    }

    /**
     * For a more intuitive GUI, select things on the tree when you right click it.
     * @param e mouse event
     */
    private void setSelectedOnRightClick(MouseEvent e) {
        TreePath path = taskTree.getPathForLocation(e.getX(), e.getY());
        if (path != null) {
            taskTree.setSelectionPath(path);
        } else {
            return;
        }
    }
}
