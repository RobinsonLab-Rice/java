package view;

import main.adapters.view.View2TaskAdapter;
import model.plate.objects.PlateSpecifications;
import model.tasks.ITaskFactory;
import model.tasks.basictasks.MLDRTask;
import model.tasks.basictasks.MultiTask;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private TaskTree executionTree;
    private TaskTree editTree;

    private View2TaskAdapter taskModel;

    /* Constructor that initializes special component needs. */
    public TaskCreationPanel() {

        moveToEditTreeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* Get selected factory, make its task, and add that to the edit tree. */
                editTree.model.setNewTask(savedTasksCmb.getItemAt(savedTasksCmb.getSelectedIndex()).make());
            }
        });
    }

    /* Perform necessary startup procedures (populating dropboxes, etc.) */
    public void start(View2TaskAdapter taskModel){
        this.taskModel = taskModel;

        editTree = new TaskTree(new EditingModel(new MLDRTask(0,0)));
        executionTree = new TaskTree(new ExecutionModel((MultiTask) taskModel.getTasks(), taskModel));

        for (ITaskFactory factory : taskModel.getTaskFactories()) {
            savedTasksCmb.addItem(factory);
        }
    }
}
