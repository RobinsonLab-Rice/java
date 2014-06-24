package model.tasks.taskvisitors;

import java.util.Hashtable;
import java.util.Map;

import model.tasks.basictasks.IExecuteTask;

/**
 * Command-based implementation of ITaskVisitor that uses ITaskVisitorCmds stored in a hash table.
 * @author cah6
 *
 */
public abstract class ATaskVisitor implements ITaskVisitor {
	
	/**
	 * The default command to use when caseAt is called with an ID that is not in the hashtable as a key.
	 */
	private ITaskVisitorCmd defaultCmd;
	
	/**
	 * The dictionary of commands, implemented using a hash table.
	 */
	private Map<String, ITaskVisitorCmd> cmds = new Hashtable<String, ITaskVisitorCmd>();

	/**
	 * Constructor that installs a default cmd that throws an IllegalArgumentException on any unknown host.
	 */
	public ATaskVisitor() {
		defaultCmd = new ITaskVisitorCmd() {
			public Object apply(String idx, IExecuteTask host, Object... inps) {
				throw new IllegalArgumentException(
						"ATaskVisitor: Unknown index encountered: " + idx);
			}
		};
	}

	/**
	 * Constructor that sets the default cmd to the supplied cmd.
	 * @param defaultCmd   the default cmd to use.
	 */
	public ATaskVisitor(ITaskVisitorCmd defaultCmd) {
		this.defaultCmd = defaultCmd;
	}

	/**
	 * Runs the command associated with the given id, passing along the given host and vararg parameters.  
	 * If there is no cmd associated with the given id in the dictionary, then use the default cmd.
	 * @param id The ID value that identifies the host
	 * @param host The host IExecuteTask object
	 * @param params  vararg list of parameters that might be used.   Can be called with no input params.
	 * @return the result of executing the associated cmd.
	 */
	public Object caseAt(String id, IExecuteTask host, Object... params) {
		if (cmds.containsKey(id)) {
			return cmds.get(id).apply(id, host, params);
		} else {
			return defaultCmd.apply(id, host, params);
		}
	}

	/**
	 * Add the given command to the dictionary, associated with the given id value.
	 * @param id The id of the host that will use this command
	 * @param cmd The command that will be run when the host calls for it.
	 */
	public void addCmd(String id, ITaskVisitorCmd cmd) {
		cmds.put(id, cmd);
	}

}
