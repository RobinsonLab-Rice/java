package main.view;

import main.model.serialization.SerializationModel;
import main.model.tasks.TaskModel;
import main.model.tasks.basictasks.AExecuteTask;
import main.model.tasks.basictasks.IExecuteTask;
import main.model.tasks.basictasks.MultiTask;
import main.util.Parser;
import main.view.dialogs.*;
import main.view.panels.MainPanel;
import main.view.panels.TaskCreationPanel;
import sun.java2d.pipe.SpanShapeRenderer;

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

    private TaskCreationPanel taskView;
    private TaskTree taskTree;
    private TaskModel taskModel;
    private SerializationModel serializationModel;
    private JComboBox savedTasksCmb;
    private JComboBox savedExpCmb;

    public TreeRightClickListener(TaskCreationPanel taskView, JComboBox savedTasksCmb, JComboBox savedExpCmb, TaskTree taskTree,
                                  TaskModel taskModel, SerializationModel serializationModel) {
        this.taskView = taskView;
        this.savedTasksCmb = savedTasksCmb;
        this.savedExpCmb = savedExpCmb;
        this.taskTree = taskTree;
        this.taskModel = taskModel;
        this.serializationModel = serializationModel;
    }

    public void mousePressed(MouseEvent e) {
        TreePath selPath = taskTree.getPathForLocation(e.getX(), e.getY());
        //if our click was a right click
        if (e.getButton() == 3 && selPath != null) {
            setSelectedOnRightClick(e);
            //pop up with a contextual menu of buttons
            doPop(e, selPath);
        }
    }

    private void doPop(MouseEvent e, final TreePath selPath) {
        JPopupMenu menu = new JPopupMenu() {

            private static final long serialVersionUID = -3142513178293086540L;

            JMenuItem saveTask, saveExperiment, delete, show, hide, replaceInc, replaceAll, loop;

            {
                saveTask = new JMenuItem("Save Task");
                saveExperiment = new JMenuItem("Save Experiment");
                delete = new JMenuItem("Delete");
                show = new JMenuItem("Show");
                hide = new JMenuItem("Hide");
                replaceInc = new JMenuItem("Replace: Incremental");
                replaceAll = new JMenuItem("Replace: All");
                loop = new JMenuItem("Loop Task");

                /* Only allow saving on multitasks. Saves it as the multitask's current name. */
                saveTask.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //get the selected node to later use it
                    MultiTask selected = (MultiTask) selPath.getLastPathComponent();

                    String s = SimpleDialogs.popSaveDialog(taskTree, selected.name);

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

                        String s = SimpleDialogs.popSaveDialog(taskTree, selected.name);

                        //if they didn't input anything or cancelled, do nothing and return out
                        if (s == null || s.equals("")) { }
                        //else, they did input something, attempt to save it
                        else {
                            selected.name = s;
                            serializationModel.saveExperiment(selected);
                        }

                        MainPanel.GUIHelper.updateCmb(taskModel.getExperimentFactories(), savedExpCmb);
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
                        selected.setVisibilityDown(true);
                        selected.setVisibilityUp(true);
                        ((DefaultTreeModel) taskTree.getModel()).nodeChanged(selected);
                        taskModel.repaint();
                    }
                });

                /* Hide task and all its children. */
                hide.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        AExecuteTask selected = (AExecuteTask) selPath.getLastPathComponent();
                        selected.setVisibilityDown(false);
                        ((DefaultTreeModel) taskTree.getModel()).nodeChanged(selected);
                        taskModel.repaint();
                    }
                });

                /* Incrementally replace variables in currently selected task. */
                replaceInc.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        LoopInfoDialog loopDialog = new LoopInfoDialog(loop);
                        LoopInfo info = loopDialog.showDialog();

                        //if increment is not an integer, stop execution and tell user
                        if (!Parser.isNumeric(info.inc)){
                            SimpleDialogs.popBadInput(replaceInc);
                            return;
                        }
                        //same if it is an int, but is 0 (infinite loop)
                        else if (Double.parseDouble(info.inc) <= 0) {
                            SimpleDialogs.popBadInput(replaceInc);
                            return;
                        }
                        //if all is good, continue
                        else {
                            //send over: currently selected task, variable, start value, end value, increment value
                            IExecuteTask selected = (IExecuteTask) selPath.getLastPathComponent();
                            taskModel.loopReplaceTasks(selected, info.variable, info.start, info.end, info.inc);
                            ((DefaultTreeModel) taskTree.getModel()).nodeStructureChanged(selected);
                            taskModel.repaint();
                        }
                    }
                });

                /* Replace all variables in currently selected task. */
                replaceAll.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ReplaceAllDialog replaceDialog = new ReplaceAllDialog(replaceAll);
                        ReplaceInfo info = replaceDialog.showDialog();
                        IExecuteTask selected = (IExecuteTask) selPath.getLastPathComponent();
                        taskModel.replaceAll(selected, info.toReplace, info.start);
                        ((DefaultTreeModel) taskTree.getModel()).nodeStructureChanged(selected);
                        taskModel.repaint();
                    }
                });

                loop.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        LoopInfoDialog loopDialog = new LoopInfoDialog(loop);
                        LoopInfo info = loopDialog.showDialog();
                        //if increment is not an integer, stop execution and tell user
                        if (!Parser.isNumeric(info.inc)){
                            SimpleDialogs.popBadInput(replaceInc);
                            return;
                        }
                        //same if it is an int, but is 0 (infinite loop)
                        else if (Double.parseDouble(info.inc) <= 0) {
                            SimpleDialogs.popBadInput(replaceInc);
                            return;
                        }
                        //if all is good, continue
                        else {
                            //send over: currently selected task, variable, start value, end value, increment value
                            taskModel.loopGenerateTasks((IExecuteTask) selPath.getLastPathComponent(), info.variable,
                                    info.start, info.end, info.inc);
                        }
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
                    add(replaceInc);
                    add(replaceAll);
                }
                add(loop);
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
}
