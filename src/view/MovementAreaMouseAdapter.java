package view;

import main.adapters.view.View2PlateAdapter;
import main.adapters.view.View2TaskAdapter;

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

    /**
     * @param plateModel adapter to plate model
     * @param taskModel adapter to task model
     */
    public MovementAreaMouseAdapter(View2PlateAdapter plateModel, View2TaskAdapter taskModel) {
        this.plateModel = plateModel;
        this.taskModel = taskModel;
    }

    /**
     * When a mouse is clicked, tell the plate model so that it may handle selecting wells.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        plateModel.mouseClicked(e);
    }

    /**
     * When mouse is pressed:
     * if it is a right click, display the popup menu
     * if it is a left click, save the locations of selected wells to start a click and drag
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * When mouse is released, if it was a left click, tell the model so.
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * When mouse is dragged, draw a line from selected wells to current location to visually assist
     * the user.
     */
    @Override
    public void mouseDragged(MouseEvent e) {

    }
}
