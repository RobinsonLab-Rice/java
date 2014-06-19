package view;

import main.adapters.view.View2TaskAdapter;
import model.plate.objects.PlateSpecifications;
import model.tasks.ITaskFactory;
import model.tasks.basictasks.*;

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

    private View2TaskAdapter taskModel;

    /* Constructor that initializes special component needs. */
    public TaskCreationPanel() {

        moveToEditTreeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* Get selected factory, make its task, and add that to the edit tree. */
                taskTree.model.setNewTask(savedTasksCmb.getItemAt(savedTasksCmb.getSelectedIndex()).make());
            }
        });
    }

    /* Perform necessary startup procedures (populating dropboxes, etc.) */
    public void start(View2TaskAdapter taskModel){
        this.taskModel = taskModel;



        //taskTree = new TaskTree(new ExecutionModel(test, taskModel));

//        for (ITaskFactory factory : taskModel.getTaskFactories()) {
//            savedTasksCmb.addItem(factory);
//        }
    }

    public void createUIComponents(){
        MoveTask testMove = new MoveTask(2);
        MultiTask test = new MultiTask(new MultiTask(testMove, new DispenseTask(100)), new MoveTask(3), new LowerTask());
        taskTree = new TaskTree(new ExecutionModel(test, taskModel));

        testMove.changeData(4);
        taskTree.model.nodeChanged(testMove);
    }
}
