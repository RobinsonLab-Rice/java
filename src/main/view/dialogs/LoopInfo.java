package main.view.dialogs;

/**
 * Helper class for packaging info from LoopInfoDialog
 *
 * Created by Christian on 7/11/2014.
 */
public class LoopInfo {

    public String variable;
    public String start;
    public String end;
    public String inc;

    public LoopInfo(String variable, String start, String end, String inc) {
        this.variable = variable;
        this.start = start;
        this.end = end;
        this.inc = inc;
    }
}
