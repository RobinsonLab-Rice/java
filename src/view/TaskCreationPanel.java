package view;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import main.adapters.view.View2SerializationAdapter;
import main.adapters.view.View2TaskAdapter;
import model.plate.objects.PlateSpecifications;
import model.tasks.ITaskFactory;
import model.tasks.basictasks.*;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.event.*;
import java.io.IOException;

/**
 * Created by Christian on 6/13/2014.
 */
public class TaskCreationPanel extends JPanel {
    private JPanel taskCreatingPanel;
    private JComboBox<ITaskFactory> savedTasksCmb;
    private JButton moveToEditTreeBtn;
    private JButton debugExecuteBtn;
    private JButton executeAllBtn;
    private TaskTree taskTree;

    private View2TaskAdapter taskModel;
    private View2SerializationAdapter serializationModel;

    /* Constructor that initializes special component needs. */
    public TaskCreationPanel() {

        /* Get selected factory, make its task, and add that to the edit tree. */
        moveToEditTreeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IExecuteTask root = (IExecuteTask) taskTree.getModel().getRoot();
                IExecuteTask taskToAdd = savedTasksCmb.getItemAt(savedTasksCmb.getSelectedIndex()).make();
                taskToAdd.setParent(root);
                root.insert(taskToAdd, root.getChildCount());
                ((DefaultTreeModel)taskTree.getModel()).nodeStructureChanged(root);
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
    public void start(View2TaskAdapter taskModel, View2SerializationAdapter serializationModel){
        this.taskModel = taskModel;
        this.serializationModel = serializationModel;

        taskTree.setModel(this.taskModel.getTreeModel());

        /* Set the class to be used for right clicking on the jtree. */
        taskTree.addMouseListener(new TreeRightClickListener(savedTasksCmb, taskTree, taskModel, serializationModel));

        taskTree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    IExecuteTask toDelete = (IExecuteTask) taskTree.getSelectionPath().getLastPathComponent();
                    ((DefaultTreeModel)taskTree.getModel()).removeNodeFromParent(toDelete);
                }
            }
        });
        taskTree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "copy");
        taskTree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), "paste");
        taskTree.getActionMap().put("copy", TreeTransferHandler.getCopyAction());
        taskTree.getActionMap().put("paste", TreeTransferHandler.getPasteAction());

        //taskTree.model.setRoot(taskModel.getTreeModel());
        for (ITaskFactory factory : taskModel.getTaskFactories()) {
            savedTasksCmb.addItem(factory);
        }
    }

    public void createUIComponents(){
        this.taskTree = new TaskTree(new DefaultTreeModel(new NullTask()));
    }
}
