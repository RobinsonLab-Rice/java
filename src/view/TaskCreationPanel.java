package view;

import model.serialization.SaveType;
import model.serialization.SerializationModel;
import model.tasks.ITaskFactory;
import model.tasks.TaskModel;
import model.tasks.basictasks.*;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.*;

/**
 * Created by Christian on 6/13/2014.
 */
public class TaskCreationPanel extends JPanel {
    private JPanel taskCreatingPanel;
    private JComboBox<ITaskFactory> savedTasksCmb;
    private JButton moveTaskToTreeBtn;
    private JButton debugExecuteBtn;
    private JButton executeAllBtn;
    private TaskTree taskTree;
    private JTextField defaultPlate;
    private JTextField defaultDispense;
    private JComboBox<ITaskFactory> savedExperimentCmb;
    private JButton moveExperimentToTreeBtn;
    private JButton deleteTaskBtn;
    private JButton deleteExperimentBtn;

    private MainPanel mainView;
    private TaskModel taskModel;
    private SerializationModel serializationModel;

    /* Constructor that initializes special component needs. */
    public TaskCreationPanel() {

        /* Get selected factory, make its task, and add that to the execution tree. */
        moveTaskToTreeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IExecuteTask root = (IExecuteTask) taskTree.getModel().getRoot();
                IExecuteTask taskToAdd = savedTasksCmb.getItemAt(savedTasksCmb.getSelectedIndex()).make();
                taskToAdd.setParent(root);
                root.insert(taskToAdd, root.getChildCount());
                ((DefaultTreeModel)taskTree.getModel()).nodeStructureChanged(root);
            }
        });

        /* Get selected factory, make its task, and make that the new root of the execution tree. */
        moveExperimentToTreeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IExecuteTask newRoot = savedExperimentCmb.getItemAt(savedExperimentCmb.getSelectedIndex()).make();
                ((DefaultTreeModel) taskTree.getModel()).setRoot(newRoot);
            }
        });

        deleteTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (savedTasksCmb.getSelectedIndex() <= 4) {
                    JOptionPane.showMessageDialog(savedTasksCmb, "Can't delete a built-in task.");
                    return;
                }
                serializationModel.deleteData(savedTasksCmb.getItemAt(savedTasksCmb.getSelectedIndex()).toString(), SaveType.TASK);
                MainPanel.GUIHelper.updateCmb(taskModel.getTaskFactories(), savedTasksCmb);
            }
        });

        deleteExperimentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serializationModel.deleteData(savedExperimentCmb.getItemAt(savedExperimentCmb.getSelectedIndex()).toString(), SaveType.EXPERIMENT);
                MainPanel.GUIHelper.updateCmb(taskModel.getExperimentFactories(), savedExperimentCmb);
            }
        });

        /* Tell backend model to execute the task queue in debug mode. */
        debugExecuteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskModel.debugExecuteAll();
            }
        });
    }

    /* Perform necessary startup procedures (populating dropboxes, etc.) */
    public void start(MainPanel mainView, TaskModel taskModel, SerializationModel serializationModel){
        this.mainView = mainView;
        this.taskModel = taskModel;
        this.serializationModel = serializationModel;

        taskTree.setModel(this.taskModel.getTreeModel());

        /* Set the class to be used for right clicking on the jtree. */
        taskTree.addMouseListener(new TreeRightClickListener(savedTasksCmb, taskTree, taskModel, serializationModel));

        /* Add keyboard shortcuts to the JTree. */
        taskTree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    TreePath[] paths = taskTree.getSelectionPaths();
                    for (TreePath path : paths) {
                        MutableTreeNode toDelete = (MutableTreeNode) path.getLastPathComponent();

                        if (toDelete.getParent() == null)   //no parent means it is the root
                            ((DefaultTreeModel) taskTree.getModel()).setRoot(new MultiTask());
                        else                                //for all other nodes, remove them from parent
                            ((DefaultTreeModel) taskTree.getModel()).removeNodeFromParent(toDelete);
                    }
                }
            }
        });

        //TODO: fix this and get keyboard shortcuts for copy and paste, if desired
//        taskTree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "copy");
//        taskTree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), "paste");
//        taskTree.getActionMap().put("copy", TransferHandler.getCopyAction());
//        taskTree.getActionMap().put("paste", TransferHandler.getPasteAction());

        //populate the task and experiment comboboxes
        MainPanel.GUIHelper.updateCmb(taskModel.getTaskFactories(), savedTasksCmb);
        MainPanel.GUIHelper.updateCmb(taskModel.getExperimentFactories(), savedExperimentCmb);
    }

    public void createUIComponents(){
        this.taskTree = new TaskTree(new DefaultTreeModel(new NullTask()));
    }

    /**
     * @return default value to dispense when creating a dispense task
     */
    public double getDefaultDispense() {
        return Double.parseDouble(defaultDispense.getText());
    }

    /**
     * @return default plate to set when creating a move task
     */
    public String getDefaultPlate() {
        return defaultPlate.getText();
    }
}
