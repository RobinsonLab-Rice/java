package main.view.panels;

import main.model.tasks.TaskModel;
import main.model.tasks.basictasks.ALeafTask;
import main.model.tasks.basictasks.IExecuteTask;
import main.model.tasks.basictasks.MultiTask;
import main.model.serialization.SaveType;
import main.model.serialization.SerializationModel;
import main.model.tasks.ITaskFactory;
import main.model.tasks.basictasks.NullTask;
import main.view.CustomTreeUI;
import main.view.dialogs.LoopInfo;
import main.view.TaskTree;
import main.view.TreeRightClickListener;
import main.view.dialogs.LoopInfoDialog;

import javax.swing.*;
import javax.swing.plaf.TreeUI;
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
    private JComboBox<ITaskFactory> basicTasksCmb;
    private JComboBox<ITaskFactory> savedTasksCmb;
    private JButton debugExecuteBtn;
    private JButton executeAllBtn;
    private TaskTree taskTree;
    public JTextField defaultPlate;
    public JTextField defaultDispense;
    private JComboBox<ITaskFactory> savedExperimentCmb;
    private JButton moveExperimentToTreeBtn;
    private JButton deleteTaskBtn;
    private JButton deleteExperimentBtn;
    private JTextField hTextField;
    private JScrollPane taskTreeScrollPane;
    private JButton loadBasicTaskBtn;
    private JButton loadSavedTaskBtn;

    private MainPanel mainView;
    private TaskModel taskModel;
    private SerializationModel serializationModel;

    /* Constructor that initializes special component needs. */
    public TaskCreationPanel() {

        /* Get selected factory, make its task, and add that to the execution tree. */
        loadBasicTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskModel.appendTaskToQueue(basicTasksCmb.getItemAt(basicTasksCmb.getSelectedIndex()).make());
            }
        });

        /* Get selected factory, make its task, and add that to the execution tree. */
        loadSavedTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskModel.appendTaskToQueue(savedTasksCmb.getItemAt(savedTasksCmb.getSelectedIndex()).make());
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
                if (savedTasksCmb.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(savedTasksCmb, "No task to delete!");
                    return;
                }
                serializationModel.deleteData(savedTasksCmb.getItemAt(savedTasksCmb.getSelectedIndex()).toString(), SaveType.TASK);
                MainPanel.GUIHelper.updateCmb(taskModel.getSavedTaskFactories(), savedTasksCmb);
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

        //TreeUI toCheck = taskTree.getUI();
        taskTree.setUI(new CustomTreeUI(taskTreeScrollPane));

        /* Set the class to be used for right clicking on the jtree. */
        taskTree.addMouseListener(new TreeRightClickListener(this, savedTasksCmb, savedExperimentCmb, taskTree, taskModel, serializationModel));

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
                //if node is not visible grey it out
                if (!node.getVisibility()){
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
        MainPanel.GUIHelper.updateCmb(taskModel.getBasicTaskFactories(), basicTasksCmb);
        MainPanel.GUIHelper.updateCmb(taskModel.getSavedTaskFactories(), savedTasksCmb);
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
