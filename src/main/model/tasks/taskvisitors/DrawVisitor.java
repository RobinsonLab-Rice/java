package main.model.tasks.taskvisitors;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import main.model.plate.objects.Plate;
import main.model.tasks.basictasks.*;
import main.util.Parser;

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
    private BufferedImage questionMarkIcon;

	public DrawVisitor(){
        //load up the right icons
        try {
            dispenseIcon = ImageIO.read(new File("src/images/dispense_icon.png"));
            withdrawIcon = ImageIO.read(new File("src/images/withdraw_icon.png"));
            questionMarkIcon = ImageIO.read(new File("src/images/question_mark_icon.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

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

                //stop immedietely if this task should not be drawn
                if (!dispenseHost.getVisibility()) return params[2];

                Graphics2D g2d = (Graphics2D) params[0];
                double sF = (Double) params[1];
                Point2D destination = (Point2D) params[2];

                int scale = 8;

                //only draw if we've actually moved a little
                if (destination.distance(0,0) != 0) {
                    //actually draw the task, drawing a different icon for withdrawing and dispensing
                    if (dispenseHost.getVolume() < 0) { //if withdrawing, draw an arrow going up
                        g2d.drawImage(dispenseIcon,
                                (int)(destination.getX()*sF - scale), //top-left x coordinate
                                (int)(destination.getY()*sF - scale), //top-left y coorinate
                                scale*2,                 //width
                                scale*2,                //height
                                null);
                    }
                    else if (dispenseHost.getVolume() < 0) {          //otherwise, draw an arrow going down
                        g2d.drawImage(withdrawIcon,
                                (int)(destination.getX()*sF - scale), //top-left x coordinate
                                (int)(destination.getY()*sF - scale), //top-left y coorinate
                                scale*2,                 //width
                                scale*2,                //height
                                null);
                    }
                    //if the volume is 0, task is not filled out yet. draw a question mark.
                    else {
                        g2d.drawImage(questionMarkIcon,
                                (int)(destination.getX()*sF - scale), //top-left x coordinate
                                (int)(destination.getY()*sF - scale), //top-left y coorinate
                                scale*2,                 //width
                                scale*2,                //height
                                null);
                        }
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
                g2d.setColor(Color.black);
                double sF = (Double) params[1];
                Point2D start = (Point2D) params[2];
                ArrayList<Plate> plates = (ArrayList<Plate>) params[3];
                ArrayList<String> identifiers = moveToWellHost.getDestination();

                //if well is not an idenfitifer
                if (!Parser.isIdentifier(identifiers.get(1))) return start;

                Point2D destination = null;
                for (Plate plate : plates) {
                    if (plate.getName().equals(identifiers.get(0)))
                        destination = plate.getWellLocation(identifiers.get(1));
                }
                if (destination == null) return start;

                //actually draw the task. if the plate doesn't exist and destination is null, do nothing.
                if (destination != null && moveToWellHost.getVisibility()){
                    drawArrows(g2d, new Point2D.Double(start.getX()*sF, start.getY()*sF), new Point2D.Double(destination.getX()*sF, destination.getY()*sF), sF);
                    g2d.drawLine((int) (start.getX()*sF), (int) (start.getY()*sF), (int) (destination.getX()*sF), (int) (destination.getY()*sF));
                }

                return destination; //return the final location
			}
		});
        addCmd("MoveToLoc", new ITaskVisitorCmd(){
            @Override
            public Object apply(String id, IExecuteTask host, Object... params) {
                MoveToLocTask moveToLocHost = (MoveToLocTask) host;

                Graphics2D g2d = (Graphics2D) params[0];
                g2d.setColor(Color.black);
                double sF = (Double) params[1];
                Point2D start = (Point2D) params[2];
                Point2D destination = moveToLocHost.getDestination();

                //actually draw the task
                if (destination != null && moveToLocHost.getVisibility()){
                    drawArrows(g2d, new Point2D.Double(start.getX()*sF, start.getY()*sF), new Point2D.Double(destination.getX()*sF, destination.getY()*sF), sF);
                    g2d.drawLine((int) (start.getX()*sF), (int) (start.getY()*sF), (int) (destination.getX()*sF), (int) (destination.getY()*sF));
                }


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
        addCmd("PumpParams", new ITaskVisitorCmd(){
            @Override
            public Object apply(String id, IExecuteTask host, Object... params) {
                return null;
            }
        });
	}

    /**
     * Draw arrows along line formed by two input points.
     */
    private void drawArrows(Graphics2D g2d, Point2D start, Point2D end, double sF) {

        int cutoff = 150;
        //int scale = 5;
        Point2D midPoint = new Point2D.Double((start.getX() + end.getX())/2, (start.getY() + end.getY())/2);

        //draw arrow regardless
        Polygon arrow = new Polygon(
                new int[] {-1,0,1,0},
                new int[] {0,1,0,1},
                4
        );

        AffineTransform at = new AffineTransform();
        at.translate(midPoint.getX(), midPoint.getY());
        at.rotate(Math.atan2(end.getY() - start.getY(), end.getX() - start.getX())-Math.PI/2);
        at.scale(sF, sF);

        g2d.setTransform(at);
        g2d.setStroke(new BasicStroke((float) (1/sF)));
        g2d.drawPolygon(arrow);
        g2d.setTransform(new AffineTransform());

        //if distance between two points is less than some cutoff, return
        if (start.distance(end) < cutoff) {
            return;
        }
        else {
            drawArrows(g2d, start, midPoint, sF);
            drawArrows(g2d, midPoint, end, sF);
        }
    }
}
