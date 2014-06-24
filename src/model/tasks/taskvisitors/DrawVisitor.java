package model.tasks.taskvisitors;

import java.util.ArrayList;

import model.tasks.basictasks.IExecuteTask;
import model.tasks.basictasks.MultiTask;

/**
 * Visitor that handles all drawing of tasks.
 * @author Christian
 *
 */
public class DrawVisitor extends ATaskVisitor {

	public DrawVisitor(){
		addCmd("Multi", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				MultiTask multiHost = (MultiTask) host;
				ArrayList<IExecuteTask> subtasks = multiHost.getSubtasks();
				for (IExecuteTask task : subtasks){
					task.executeVisitor(DrawVisitor.this, params);
				}
				return null;
			}
		});
		addCmd("Dispense", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				return null;
			}
		});
		addCmd("MoveToWell", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				return null;
			}
		});
        addCmd("MoveToLoc", new ITaskVisitorCmd(){
            @Override
            public Object apply(String id, IExecuteTask host, Object... params) {
                return null;
            }
        });
		addCmd("NozzleHeight", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				return null;
			}
		});
	}
}
