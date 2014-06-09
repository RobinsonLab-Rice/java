package view;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DemoDeviceControlPanel extends JPanel {
	private JTextField txtWellNum;
	private JTextField txtDispenseAmount;
	private JTextField txtCommand;

	/**
	 * Create the panel.
	 */
	public DemoDeviceControlPanel(final TaskAdapter taskModelAdapter, final SerialCommAdapter serialModelAdapter) {
		setLayout(new MigLayout("", "[grow][grow]", "[grow][][][][][][][][grow]"));
		
		JLabel lblNewLabel = new JLabel("Create Basic Task");
		add(lblNewLabel, "cell 0 1 2 1,alignx center");
		
		JLabel lblWellNumber = new JLabel("Well Number");
		add(lblWellNumber, "cell 0 2,alignx right");
		
		txtWellNum = new JTextField();
		add(txtWellNum, "cell 1 2,growx");
		txtWellNum.setColumns(10);
		
		JLabel lblAmountToDispense = new JLabel("Amount to dispense");
		add(lblAmountToDispense, "cell 0 3,alignx trailing");
		
		txtDispenseAmount = new JTextField();
		add(txtDispenseAmount, "cell 1 3,growx");
		txtDispenseAmount.setColumns(10);
		
		Action addToQueue = new AbstractAction("Add to queue"){
			private static final long serialVersionUID = -3581392539384053458L;

			public void actionPerformed(ActionEvent e){
				taskModelAdapter.addSingleWellTask(Integer.parseInt(txtWellNum.getText()), Integer.parseInt(txtDispenseAmount.getText()));
			}
		};
		
		txtDispenseAmount.setAction(addToQueue);
		txtWellNum.setAction(addToQueue);
		
		JButton btnNewButton = new JButton(addToQueue);
		add(btnNewButton, "cell 0 4 2 1,alignx center");
		
		JLabel lblNewLabel_1 = new JLabel("Send Directly To Arduino");
		add(lblNewLabel_1, "cell 0 5 2 1,alignx center");
		
		txtCommand = new JTextField();
		add(txtCommand, "cell 0 6 2 1,growx");
		txtCommand.setColumns(10);
		
		JButton btnSendText = new JButton("Send to Arduino");
		btnSendText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serialModelAdapter.sendText(txtCommand.getText());
			}
		});
		add(btnSendText, "cell 0 7 2 1,alignx center");

	}

}
