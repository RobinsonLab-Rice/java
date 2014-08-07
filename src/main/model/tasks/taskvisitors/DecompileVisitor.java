package main.model.tasks.taskvisitors;

import java.util.ArrayList;
import java.util.Collections;

import main.model.tasks.basictasks.*;
import main.model.tasks.basictasks.ALeafTask;

public class DecompileVisitor extends ATaskVisitor {

	public DecompileVisitor(){
		addCmd("Multi", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				MultiTask multiHost = (MultiTask) host;
				ArrayList<IExecuteTask> subtasks = Collections.list(multiHost.children());
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
		addCmd("MoveToWell", new ITaskVisitorCmd(){
			@Override
			public Object apply(String id, IExecuteTask host, Object... params) {
				((ArrayList<ALeafTask>) params[0]).add((MoveToWellTask) host);
				return null;
			}
		});
        addCmd("MoveToLoc", new ITaskVisitorCmd(){
            @Override
            public Object apply(String id, IExecuteTask host, Object... params) {
                ((ArrayList<ALeafTask>) params[0]).add((MoveToWellTask) host);
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
        addCmd("PumpParams", new ITaskVisitorCmd(){
            @Override
            public Object apply(String id, IExecuteTask host, Object... params) {
                ((ArrayList<ALeafTask>) params[0]).add((PumpParamsTask) host);
                return null;
            }
        });
        addCmd("Raw", new ITaskVisitorCmd(){
            @Override
            public Object apply(String id, IExecuteTask host, Object... params) {
                ((ArrayList<ALeafTask>) params[0]).add((RawTask) host);
                return null;
            }
        });
        addCmd("Delay", new ITaskVisitorCmd(){
            @Override
            public Object apply(String id, IExecuteTask host, Object... params) {
                ((ArrayList<ALeafTask>) params[0]).add((DelayTask) host);
                return null;
            }
        });
	}
}
