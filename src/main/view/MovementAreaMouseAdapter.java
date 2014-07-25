package main.view;

import main.model.plate.PlateModel;
import main.model.tasks.TaskModel;

import javax.swing.*;
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

    private PlateModel plateModel;
    private TaskModel taskModel;

    /* Used for tracking click and drags. */
    private MouseEvent startInfo;

    /**
     * @param plateModel adapter to plate model
     * @param taskModel adapter to task model
     */
    public MovementAreaMouseAdapter(PlateModel plateModel, TaskModel taskModel) {
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
     * When mouse is pressed, save start info for the mouse released to act on.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        plateModel.redraw();
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
        plateModel.redraw();
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
    public void doPopup(final MouseEvent e) {
        JPopupMenu menu = new JPopupMenu() {

            JMenuItem deletePlate, rotatePlate;

            {
                deletePlate = new JMenuItem("Delete Plate");
                rotatePlate = new JMenuItem("Rotate Plate");


                deletePlate.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        //delete plate we clicked on
                        plateModel.deletePlate(e.getPoint());
                    }
                });

                rotatePlate.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent a) {
                        //toggle rotation of plate we clicked on
                        plateModel.rotatePlate(e.getPoint());
                    }
                });

                add(deletePlate);
                add(rotatePlate);
            }
        };
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}
