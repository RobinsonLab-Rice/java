package main.model.tasks.basictasks;

import main.model.plate.objects.ArmState;
import main.model.tasks.taskvisitors.ITaskVisitor;

import java.io.OutputStream;

/**
 * Task existing for the purpose of being able to interact with a user-made function of the Arduino (something not built in).
 *
 * Created by christianhenry on 8/6/14.
 */
public class RawTask extends ALeafTask {

    private String text = "Overwrite(me)";

    public RawTask() {
    }

    /**
     * Sends "text" out through the output stream.
     * @param armState - current position of the arm, when this task is executed
     * @param outputStream - ouput stream tasks will execute through
     */
    @Override
    public void execute(ArmState armState, OutputStream outputStream) {
        String cmdString = text;
        this.writeString(cmdString, outputStream);
    }

    /**
     * Call "Raw" case of input visitor.
     * @param visitor The visitor to execute
     * @param params The input parameters supplied to the visitor when its appropriate case is called.
     * @return
     */
    @Override
    public Object executeVisitor(ITaskVisitor visitor, Object... params) {
        return visitor.caseAt("Raw", this, params);
    }

    /**
     * When user changes text in JTree, just set text to be the new input.
     */
    @Override
    public void setUserObject(Object object) {
        this.text = (String) object;
    }

    /**
     * Display raw tasks as the text string they will send over.
     */
    public String toString() {
        return text;
    }
}
