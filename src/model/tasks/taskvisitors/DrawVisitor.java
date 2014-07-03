package model.tasks.taskvisitors;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

import model.plate.objects.Plate;
import model.tasks.basictasks.*;

/**
 * Visitor that handles all drawing of tasks. Params passed in is an array containing the graphics to paint on, scale
 * factor to draw at, and Point2D location of current (virtual) arm location at the execution of this task, and current
 * list of plates (in that order).
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
					params[2] = task.executeVisitor(DrawVisitor.this, params);
				}
				return null;
			}
		});
        //draw a dispense task by drawing an arrow up or down
		addCmd("Dispense", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
                DispenseTask dispenseHost = (DispenseTask) host;
                dispenseHost.executeVisitor(DrawVisitor.this, params);
                //actually draw the task
                if (dispenseHost.getVolume() < 0) { //if withdrawing, draw an arrow going up
                    ((Graphics) params[0]).drawPolygon(
                            new int[] {0,0,0,0,1,0,-1},
                            new int[] {0,1,2,3,2,3,2},
                            7
                    );
                }
                else {                              //otherwise, draw an arrow going down
                    ((Graphics) params[0]).drawPolygon(
                            new int[] {0,0,0,0,1,0,-1},
                            new int[] {3,2,1,0,1,0,1},
                            7
                    );
                }

                return params[2]; //return location as is
			}
		});
        //draw move tasks by drawing a line from current location to destination
		addCmd("MoveToWell", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
                Iterable<Plate> plates = (Iterable<Plate>) params[3];
                MoveToWellTask moveToWellHost = (MoveToWellTask) host;
                ArrayList<String> identifiers = moveToWellHost.getDestination();
                Point2D destination = null;
                while (plates.iterator().hasNext()){
                    Plate currPlate = plates.iterator().next();
                    //if plate names match, get the well
                    if (currPlate.getName() == identifiers.get(0))
                        destination = currPlate.getWellLocation(identifiers.get(1));
                }
                //TODO: actually draw the task

                return destination; //return the final location
			}
		});
        addCmd("MoveToLoc", new ITaskVisitorCmd(){
            @Override
            public Object apply(String id, IExecuteTask host, Object... params) {
                MoveToLocTask moveToLocHost = (MoveToLocTask) host;
                Point2D destination = moveToLocHost.getDestination();
                //actually draw the task

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
