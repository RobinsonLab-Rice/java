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
}
