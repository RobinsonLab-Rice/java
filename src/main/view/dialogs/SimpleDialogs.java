package main.view.dialogs;

import main.model.tasks.basictasks.MultiTask;

import javax.swing.*;
import java.awt.*;

/**
 * Created by christianhenry on 7/24/14.
 */
public class SimpleDialogs {

    /**
     * Popup dialog for entering name of data to save.
     * @return String (could be null) that the user input
     */
    public static String popSaveDialog(Component parent, String defaultName) {
        return (String) JOptionPane.showInputDialog(
                parent,
                "Save as:",
                "Save Item",
                JOptionPane.YES_NO_OPTION,
                null,
                null,
                defaultName);
    }

    /**
     * Popup dialog for warning user that their save name is already in use.
     * @return int marking whether they pressed OK or Cancel
     */
    public static int popOverwriteDialog() {
        return JOptionPane.showOptionDialog(null,
                "Saved item with that name already exists. Overwrite it?",
                "Overwrite existing?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                new String[] {"Save Anyway", "Cancel"},  //the titles of buttons
                "Save Anyway"); //default button title
    }

    /**
     * Simple dialog for telling user that data was not saved.
     * @param parent component to pop on top of
     */
    public static void popSaveUnsuccessful(Component parent) {
        JOptionPane.showMessageDialog(parent, "Data could not be saved, possibly incorrectly formatted.");
    }

    /**
     * Simple dialog for telling user that info they input was bad.
     * @param parent component to pop on top of
     */
    public static void popBadInput(Component parent) {
        JOptionPane.showMessageDialog(parent, "Input data not in correct format, try again.");
    }

}
