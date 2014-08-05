package main.view.dialogs;

/**
 * Created by Christian on 8/4/2014.
 */
public class ReplaceInfo {

    public String toReplace;
    public String start;
    public String increment;

    public ReplaceInfo(String toReplace, String start) {
        this.toReplace = toReplace;
        this.start = start;
    }

    public ReplaceInfo(String toReplace, String start, String increment) {
        this.toReplace = toReplace;
        this.start = start;
        this.increment = increment;
    }
}
