package model.tasks.basictasks;

import java.io.OutputStream;
import java.io.IOException;

/**
 * Abstract class, defines functionality for execute, which only the anonymous inner class will use. Thus, defines
 * the base functionality for execute for all other tasks to be nothing.
 * @author Christian
 */
public abstract class ASerialTask implements IExecuteTask {
	
	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = -8336180786535595266L;

	/**
	 * Writes string to the serial output buffer.
	 */
	protected void writeString(String string, OutputStream stream){
		if (stream == null){
			System.out.println("No stream selected, but would have sent: " + string);
		}
		else{
			System.out.println(string);
			for (int i = 0; i < string.length(); i++){
				try {
					stream.write(string.charAt(i));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Serial tasks, by definition, don't have any children.
	 */
	public int getChildCount() {
		return 0;
	}
	
	/**
	 * A serial task does not have a child, so this will never be called.
	 */
	public IExecuteTask getChild(int index) {
		return null;
	}
	
	/**
	 * The parent of this task should be deleting it, not the task itself.
	 */
	@Override
	public void traverseOrDelete(Object[] path) {
		System.out.println("Something went wrong, a serial task is trying to delete itself.");
	}

	/**
	 * Only abstract tasks can be inserted into, should never get here.
	 */
	@Override
	public void traverseOrInsert(Object[] path, IExecuteTask taskToAdd) {
		System.out.println("Something went wrong, we are inserting into a Serial task.");
	}
	
}
