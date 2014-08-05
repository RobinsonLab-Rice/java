package main.view.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ReplaceAllDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtToReplace;
    private JTextField txtReplacement;

    public ReplaceAllDialog(Component parent) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        pack();
        setLocationRelativeTo(parent);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        setVisible(false);
        dispose();
    }

    private void onCancel() {
        setVisible(false);
        dispose();
    }

    public ReplaceInfo showDialog() {
        setVisible(true);
        return new ReplaceInfo(txtToReplace.getText(), txtReplacement.getText());
    }
}
