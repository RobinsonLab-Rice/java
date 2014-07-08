package model.tasks.taskvisitors;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import model.plate.objects.Plate;
import model.tasks.basictasks.*;

import javax.imageio.ImageIO;

/**
 * Visitor that handles all drawing of tasks. Params passed in is an array containing the graphics to paint on, scale
 * factor to draw at, Point2D location of current (virtual) arm location at the execution of this task, and current
 * ArrayList of plates (in that order).
 *
 * @author Christian
 *
 */
public class DrawVisitor extends ATaskVisitor {

    private BufferedImage dispenseIcon;
    private BufferedImage withdrawIcon;

	public DrawVisitor(){
        //load up the right icons
        try {
            dispenseIcon = ImageIO.read(new File("src/images/dispense_icon.png"));
            withdrawIcon = ImageIO.read(new File("src/images/withdraw_icon.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

		addCmd("Multi", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				MultiTask multiHost = (MultiTask) host;

                //stop immedietely if this task should not be drawn
                if (!multiHost.getVisibility()) return params[2];

				ArrayList<IExecuteTask> subtasks = Collections.list(multiHost.children());
				for (IExecuteTask task : subtasks){
					Point2D result = (Point2D) task.executeVisitor(DrawVisitor.this, params);
                    if (result != null) params[2] = result;
				}
				return null;
			}
		});
        //draw a dispense task by drawing an arrow up or down
		addCmd("Dispense", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
                DispenseTask dispenseHost = (DispenseTask) host;

                //stop immedietely if this task should not be drawn
                if (!dispenseHost.getVisibility()) return params[2];

                Graphics2D g2d = (Graphics2D) params[0];
                double sF = (double) params[1];
                Point2D destination = (Point2D) params[2];

                int scale = 8;

                //actually draw the task, drawing a different icon for withdrawing and dispensing
                if (dispenseHost.getVolume() > 0) { //if withdrawing, draw an arrow going up
                    g2d.drawImage(dispenseIcon,
                            (int)(destination.getX()*sF - scale), //top-left x coordinate
                            (int)(destination.getY()*sF - scale), //top-left y coorinate
                            scale*2,                 //width
                            scale*2,                //height
                            null);
                }
                else {                              //otherwise, draw an arrow going down
                    g2d.drawImage(withdrawIcon,
                            (int)(destination.getX()*sF - scale), //top-left x coordinate
                            (int)(destination.getY()*sF - scale), //top-left y coorinate
                            scale*2,                 //width
                            scale*2,                //height
                            null);
                }
                return params[2]; //return location as is
			}
		});
        //draw move tasks by drawing a line from current location to destination
		addCmd("MoveToWell", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
                MoveToWellTask moveToWellHost = (MoveToWellTask) host;

                Graphics2D g2d = (Graphics2D) params[0];
                double sF = (double) params[1];
                Point2D start = (Point2D) params[2];
                ArrayList<Plate> plates = (ArrayList<Plate>) params[3];
                ArrayList<String> identifiers = moveToWellHost.getDestination();
                Point2D destination = null;
                for (Plate plate : plates) {
                    if (plate.getName().equals(identifiers.get(0)))
                        destination = plate.getWellLocation(identifiers.get(1));
                }

                //stop immedietely if this task should not be drawn
                if (!moveToWellHost.getVisibility()) return destination;

                //actually draw the task. if the plate doesn't exist and destination is null, do nothing
                if (destination != null)
                    g2d.drawLine((int) (start.getX()*sF), (int) (start.getY()*sF), (int) (destination.getX()*sF), (int) (destination.getY()*sF));

                return destination; //return the final location
			}
		});
        addCmd("MoveToLoc", new ITaskVisitorCmd(){
            @Override
            public Object apply(String id, IExecuteTask host, Object... params) {
                MoveToLocTask moveToLocHost = (MoveToLocTask) host;

                Graphics2D g2d = (Graphics2D) params[0];
                double sF = (double) params[1];
                Point2D start = (Point2D) params[2];
                Point2D destination = moveToLocHost.getDestination();

                //stop immedietely if this task should not be drawn
                if (!moveToLocHost.getVisibility()) return destination;

                //actually draw the task
                if (destination != null)
                    g2d.drawLine((int) (start.getX()*sF), (int) (start.getY()*sF), (int) (destination.getX()*sF), (int) (destination.getY()*sF));

                return destination; //return the final location
            }
        });
		addCmd("NozzleHeight", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				return null;
			}
		});
        addCmd("Lower", new ITaskVisitorCmd(){
            @Override
            public Object apply(String id, IExecuteTask host, Object... params) {
                return null;
            }
        });
        addCmd("Raise", new ITaskVisitorCmd(){
            @Override
            public Object apply(String id, IExecuteTask host, Object... params) {
                return null;
            }
        });
	}
}
