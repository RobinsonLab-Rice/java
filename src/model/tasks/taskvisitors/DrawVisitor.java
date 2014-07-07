package model.tasks.taskvisitors;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import model.plate.objects.Plate;
import model.tasks.basictasks.*;

/**
 * Visitor that handles all drawing of tasks. Params passed in is an array containing the graphics to paint on, scale
 * factor to draw at, Point2D location of current (virtual) arm location at the execution of this task, and current
 * ArrayList of plates (in that order).
 *
 * @author Christian
 *
 */
public class DrawVisitor extends ATaskVisitor {

	public DrawVisitor(){
		addCmd("Multi", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				MultiTask multiHost = (MultiTask) host;
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
                Graphics2D g2d = (Graphics2D) params[0];
                double sF = (double) params[1];
                Point2D destination = (Point2D) params[2];

                //set the affinetransform to scale/move the arrows
                AffineTransform at = new AffineTransform();
                at.translate(destination.getX()*sF, destination.getY()*sF);
                at.scale(10,10);

                //actually draw the task
                if (dispenseHost.getVolume() > 0) { //if withdrawing, draw an arrow going up
                    Shape toDraw = at.createTransformedShape(new Polygon(
                            new int[] {0,0,0,0,1,0,-1,0},
                            new int[] {0,1,2,3,2,3,2,3},
                            8));
                    g2d.draw(toDraw);
                }
                else {                              //otherwise, draw an arrow going down
                    Shape toDraw = at.createTransformedShape(new Polygon(
                            new int[] {0,0,0,0,1,0,-1,0},
                            new int[] {3,2,1,0,1,0,1,0},
                            8));
                    g2d.draw(toDraw);
                }
                return params[2]; //return location as is
			}
		});
        //draw move tasks by drawing a line from current location to destination
		addCmd("MoveToWell", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
                Graphics2D g2d = (Graphics2D) params[0];
                double sF = (double) params[1];
                Point2D start = (Point2D) params[2];
                ArrayList<Plate> plates = (ArrayList<Plate>) params[3];
                MoveToWellTask moveToWellHost = (MoveToWellTask) host;
                ArrayList<String> identifiers = moveToWellHost.getDestination();
                Point2D destination = null;
                for (Plate plate : plates) {
                    if (plate.getName().equals(identifiers.get(0)))
                        destination = plate.getWellLocation(identifiers.get(1));
                }
                //actually draw the task. if the plate doesn't exist and destination is null, do nothing
                if (destination != null)
                    g2d.drawLine((int) (start.getX()*sF), (int) (start.getY()*sF), (int) (destination.getX()*sF), (int) (destination.getY()*sF));

                return destination; //return the final location
			}
		});
        addCmd("MoveToLoc", new ITaskVisitorCmd(){
            @Override
            public Object apply(String id, IExecuteTask host, Object... params) {
                Graphics2D g2d = (Graphics2D) params[0];
                double sF = (double) params[1];
                Point2D start = (Point2D) params[2];
                MoveToLocTask moveToLocHost = (MoveToLocTask) host;
                Point2D destination = moveToLocHost.getDestination();
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
