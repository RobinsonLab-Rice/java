package view;

import model.serialization.SerializationModel;
import model.tasks.TaskModel;
import model.tasks.basictasks.AExecuteTask;
import model.tasks.basictasks.IExecuteTask;
import model.tasks.basictasks.MultiTask;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
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

    private TaskCreationPanel taskView;
    private TaskTree taskTree;
    private TaskModel taskModel;
    private SerializationModel serializationModel;
    private JComboBox savedTasksCmb;

    public TreeRightClickListener(TaskCreationPanel taskView, JComboBox savedTasksCmb, TaskTree taskTree,
                                  TaskModel taskModel, SerializationModel serializationModel) {
        this.taskView = taskView;
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

            JMenuItem saveTask, saveExperiment, delete, show, hide;

            {
                saveTask = new JMenuItem("Save Task");
                saveExperiment = new JMenuItem("Save Experiment");
                delete = new JMenuItem("Delete");
                show = new JMenuItem("Show");
                hide = new JMenuItem("Hide");

                /* Only allow saving on multitasks. Saves it as the multitask's current name. */
                saveTask.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //get the selected node to later use it
                    MultiTask selected = (MultiTask) selPath.getLastPathComponent();

                    String s = popSaveDialog(selected);

                    //if they didn't input anything or cancelled, do nothing and return out
                    if (s == null || s.equals("")) { }
                    //else, they did input something, attempt to save it
                    else {
                        selected.name = s;
                        serializationModel.saveTask(selected);
                    }

                    MainPanel.GUIHelper.updateCmb(taskModel.getTaskFactories(), savedTasksCmb);
                    }
                });

                saveExperiment.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //get the selected node to later use it
                        MultiTask selected = (MultiTask) selPath.getLastPathComponent();

                        String s = popSaveDialog(selected);

                        //if they didn't input anything or cancelled, do nothing and return out
                        if (s == null || s.equals("")) { }
                        //else, they did input something, attempt to save it
                        else {
                            selected.name = s;
                            serializationModel.saveExperiment(selected);
                        }

                        MainPanel.GUIHelper.updateCmb(taskModel.getTaskFactories(), savedTasksCmb);
                    }
                });

                /* Delete selected items from backend model (and tree). */
                delete.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        taskView.deleteNodes(taskTree.getSelectionPaths());
                    }
                });

                /* Show task and all its children. */
                show.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        AExecuteTask selected = (AExecuteTask) selPath.getLastPathComponent();
                        selected.setVisibility(true);
                        ((DefaultTreeModel) taskTree.getModel()).nodeChanged(selected);
                        taskModel.repaint();
                    }
                });

                /* Hide task and all its children. */
                hide.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        AExecuteTask selected = (AExecuteTask) selPath.getLastPathComponent();
                        selected.setVisibility(false);
                        ((DefaultTreeModel) taskTree.getModel()).nodeChanged(selected);
                        taskModel.repaint();
                    }
                });

                //make a different menu for multitasks and other tasks
                if (selPath.getLastPathComponent() instanceof MultiTask){
                    //if we selected the root, add button for saving experiment
                    if (selPath.getLastPathComponent() == taskTree.getModel().getRoot()) {
                        add(saveExperiment);
                    }
                    //else, we selected a different multitask, add button for just saving regular tasks
                    else {
                        add(saveTask);
                    }
                }

                add(delete);
                add(show);
                add(hide);

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

    /**
     * Popup dialog for entering name of data to save.
     * @return String (could be null) that the user input
     */
    private String popSaveDialog(MultiTask selected) {
        return (String)JOptionPane.showInputDialog(
                taskTree,
                "Save as:",
                "Save Item",
                JOptionPane.YES_NO_OPTION,
                null,
                null,
                selected.name);
    }
}
