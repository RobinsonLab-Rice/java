package main.view;

import main.model.tasks.TaskModel;
import main.model.tasks.basictasks.ALeafTask;
import main.model.tasks.basictasks.IExecuteTask;
import main.model.tasks.basictasks.MultiTask;
import main.model.serialization.SaveType;
import main.model.serialization.SerializationModel;
import main.model.tasks.ITaskFactory;
import main.model.tasks.basictasks.NullTask;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
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
    public JTextField defaultPlate;
    public JTextField defaultDispense;
    private JComboBox<ITaskFactory> savedExperimentCmb;
    private JButton moveExperimentToTreeBtn;
    private JButton deleteTaskBtn;
    private JButton deleteExperimentBtn;
    private JButton generateBtn;
    private JTextField nTextField;
    private JTextField nTextField1;

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

        /* When generate button is pressed, package up relevant info and ship it off to task model. */
        generateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoopInfoDialog loopDialog = new LoopInfoDialog(generateBtn);
                LoopInfo info = loopDialog.showDialog();
                //send over: currently selected task, variable, start value, end value, increment value
                taskModel.loopGenerateTasks(savedTasksCmb.getItemAt(savedTasksCmb.getSelectedIndex()), info.variable,
                        info.start, info.end, info.inc);
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
        taskTree.addMouseListener(new TreeRightClickListener(this, savedTasksCmb, taskTree, taskModel, serializationModel));

        /* Add keyboard shortcuts to the JTree. */
        taskTree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    deleteNodes(taskTree.getSelectionPaths());
                }
            }
        });

        /* Define how the node text will be drawn on the tree. */
        taskTree.setCellRenderer(new DefaultTreeCellRenderer(){
            @Override
            public Component getTreeCellRendererComponent(JTree tree,Object value,
                                                          boolean sel,boolean expanded,boolean leaf,int row,boolean hasFocus){
                super.getTreeCellRendererComponent(tree,value,sel,expanded,leaf,row,hasFocus);
                IExecuteTask node = (IExecuteTask) value;
                //if node is not visible and is a leaf task, grey it out. never grey out multi tasks, too confusing
                if (!node.getVisibility() && node instanceof ALeafTask){
                    setForeground(Color.GRAY);
                }
                return this;
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
    public String getDefaultDispense() {
        return defaultDispense.getText();
    }

    /**
     * Delete nodes on specified path.
     */
    public void deleteNodes(TreePath[] paths) {
        for (TreePath path : paths) {
            MutableTreeNode toDelete = (MutableTreeNode) path.getLastPathComponent();

            if (toDelete.getParent() == null)   //no parent means it is the root
                ((DefaultTreeModel) taskTree.getModel()).setRoot(new MultiTask("Experiment Name"));
            else                                //for all other nodes, remove them from parent
                ((DefaultTreeModel) taskTree.getModel()).removeNodeFromParent(toDelete);
        }
        taskModel.repaint();
    }
}
