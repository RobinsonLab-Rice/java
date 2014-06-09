package model.tasks;

import java.util.ArrayList;

import model.tasks.basictasks.ASerialTask;
import model.tasks.basictasks.DispenseTask;
import model.tasks.basictasks.IExecuteTask;
import model.tasks.basictasks.LowerTask;
import model.tasks.basictasks.MLDRTask;
import model.tasks.basictasks.MoveFromExternalTask;
import model.tasks.basictasks.MoveTask;
import model.tasks.basictasks.MoveWellToWellTask;
import model.tasks.basictasks.MultiTask;
import model.tasks.basictasks.NozzleHeightTask;
import model.tasks.basictasks.RaiseTask;

public class DecompileVisitor extends ATaskVisitor{

	public DecompileVisitor(){
		addCmd("Multi", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				MultiTask multiHost = (MultiTask) host;
				ArrayList<IExecuteTask> subtasks = multiHost.getSubtasks();
				for (IExecuteTask task : subtasks){
					task.executeVisitor(DecompileVisitor.this, params[0]);
				}
				return null;
			}
		});
		addCmd("MoveFromExternal", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				MoveFromExternalTask moveFromExternalHost = (MoveFromExternalTask) host;
				moveFromExternalHost.getTask().executeVisitor(DecompileVisitor.this, params[0]);
				return null;
			}
		});
		addCmd("MoveWellToWell", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				MoveWellToWellTask moveWellToWellHost = (MoveWellToWellTask) host;
				moveWellToWellHost.getTask().executeVisitor(DecompileVisitor.this, params[0]);
				return null;
			}
		});
		addCmd("MLDR", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				MLDRTask mldrHost = (MLDRTask) host;
				mldrHost.getTask().executeVisitor(DecompileVisitor.this, params[0]);
				return null;
			}
		});
		addCmd("Dispense", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				((ArrayList<ASerialTask>) params[0]).add((DispenseTask) host);
				return null;
			}
		});
		addCmd("Move", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				((ArrayList<ASerialTask>) params[0]).add((MoveTask) host);
				return null;
			}
		});
		addCmd("NozzleHeight", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				((ArrayList<ASerialTask>) params[0]).add((NozzleHeightTask) host);
				return null;
			}
		});
		addCmd("Lower", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				((ArrayList<ASerialTask>) params[0]).add((LowerTask) host);
				return null;
			}
		});
		addCmd("Raise", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				((ArrayList<ASerialTask>) params[0]).add((RaiseTask) host);
				return null;
			}
		});
	}
}
