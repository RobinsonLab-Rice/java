package view;

import main.adapters.view.View2PlateAdapter;
import main.adapters.view.View2TaskAdapter;
import model.tasks.basictasks.IExecuteTask;
import model.tasks.basictasks.MultiTask;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse adapter for handling clicks and drags on the panel the plates and wells are shown in.
 *
 * Created by christianhenry on 7/1/14.
 */
public class MovementAreaMouseAdapter extends MouseAdapter {

    private View2PlateAdapter plateModel;
    private View2TaskAdapter taskModel;

    /* Used for tracking click and drags. */
    private MouseEvent startInfo;

    /**
     * @param plateModel adapter to plate model
     * @param taskModel adapter to task model
     */
    public MovementAreaMouseAdapter(View2PlateAdapter plateModel, View2TaskAdapter taskModel) {
        this.plateModel = plateModel;
        this.taskModel = taskModel;
    }

    /**
     * Don't do anything on mouse clicks, it will be handled with pressed/released events.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * When mouse is pressed:
     * if it is a right click, select the well clicked and display the popup menu
     * if it is a left click, save the locations of selected wells to start a click and drag
     */
    @Override
    public void mousePressed(MouseEvent e) {
        startInfo = e;
    }

    /**
     * When mouse is released, parse if it was a left or right click and act appropriately.
     */
    @Override
    public void mouseReleased(MouseEvent e) {

        plateModel.clickAndDrag(startInfo, e);

        // if click was a right click, make the popup window
        if (e.getButton() == 3) {
            doPopup(e);
        }
    }

    /**
     * When mouse is dragged, draw a line from selected wells to current location to visually assist
     * the user.
     */
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * Bring up the popup right click menu.
     */
    public void doPopup(MouseEvent e) {
        JPopupMenu menu = new JPopupMenu() {

            JMenuItem deletePlate, withdraw, deposit;

            {
                deletePlate = new JMenuItem("Delete Plate");
                withdraw = new JMenuItem("Withdraw from Selected");
                deposit = new JMenuItem("Deposit to Selected");
//
//                /* Only allow saving on multitasks. Saves it as the multitask's current name. */
//                save.addActionListener(new ActionListener() {
//                    public void actionPerformed(ActionEvent e) {
//                        //save the task to file system
//                        serializationModel.saveExecutionTask((IExecuteTask) selPath.getLastPathComponent());
//                        //update the combobox of tasks so this is shown
////                        savedTasksCmb.removeAllItems();
////                        for (ITaskFactory factory : taskModel.getTaskFactories()){
////                            savedTasksCmb.addItem(factory);
////                        }
//                        MainPanel.GUIHelper.updateCmb(taskModel.getTaskFactories(), savedTasksCmb);
//                    }
//                });
//
//                /* Delete selected item from backend model (and tree). */
//                delete.addActionListener(new ActionListener() {
//                    public void actionPerformed(ActionEvent e) {
//                        IExecuteTask toDelete = (IExecuteTask) taskTree.getSelectionPath().getLastPathComponent();
//                        if (toDelete.getParent() == null)   //no parent means it is the root
//                            ((DefaultTreeModel) taskTree.getModel()).setRoot(new MultiTask());
//                        else                                //for all other nodes, remove them from parent
//                            ((DefaultTreeModel) taskTree.getModel()).removeNodeFromParent(toDelete);
//                    }
//                });
//
//                //make a different menu for multitasks and other tasks
//                if (selPath.getLastPathComponent() instanceof MultiTask){
//                    add(save);
//                    add(delete);
//                }
//                else {
//                    add(delete);
//                }
                add(deletePlate);
                add(deposit);
                add(withdraw);
            }
        };
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}
