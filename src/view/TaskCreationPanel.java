package view;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import main.adapters.view.View2TaskAdapter;
import model.plate.objects.PlateSpecifications;
import model.tasks.ITaskFactory;
import model.tasks.basictasks.*;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        taskTree.addMouseListener(new TreeRightClickListener(taskTree, taskModel));
    }

    /* Perform necessary startup procedures (populating dropboxes, etc.) */
    public void start(View2TaskAdapter taskModel){
        this.taskModel = taskModel;
        taskTree.setModel(taskModel.getTreeModel());

        //taskTree.model.setRoot(taskModel.getTreeModel());
        for (ITaskFactory factory : taskModel.getTaskFactories()) {
            savedTasksCmb.addItem(factory);
        }
    }

    public void createUIComponents(){
        this.taskTree = new TaskTree(new DefaultTreeModel(new NullTask()));
    }
}
