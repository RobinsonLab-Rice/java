package model.tasks;

import java.util.ArrayList;

import model.tasks.basictasks.*;
import model.tasks.basictasks.ALeafTask;

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
		addCmd("Dispense", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				((ArrayList<ALeafTask>) params[0]).add((DispenseTask) host);
				return null;
			}
		});
		addCmd("Move", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				((ArrayList<ALeafTask>) params[0]).add((MoveTask) host);
				return null;
			}
		});
		addCmd("NozzleHeight", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				((ArrayList<ALeafTask>) params[0]).add((NozzleHeightTask) host);
				return null;
			}
		});
		addCmd("Lower", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				((ArrayList<ALeafTask>) params[0]).add((LowerTask) host);
				return null;
			}
		});
		addCmd("Raise", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				((ArrayList<ALeafTask>) params[0]).add((RaiseTask) host);
				return null;
			}
		});
	}
}
