package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;

import model.plate.objects.PlateNumbering;
import model.plate.objects.PlateSpecifications;
import model.serialization.SaveType;
import model.tasks.basictasks.MultiTask;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JTabbedPane;


public class MainGUI<TFactoryItem> extends JFrame {
	
	/**
	 * Adapter to allow view to talk to model through specified methods.
	 */
	private IView2PlateAdapter plateModelAdapter;
	
	/**
	 * Adapter to allow view to talk to serial model through specified methods.
	 */
	private SerialCommAdapter serialModelAdapter;
	
	/**
	 * Adapter to allow view to talk to task model through specified methods.
	 */
	private TaskAdapter<TFactoryItem> taskModelAdapter;
	
	/**
	 * Adapter to allow view to talk to task model through specified methods.
	 */
	private IView2SerializationAdapter IView2SerializationAdapter;
	
	/**
	 * Auto generated serial ID 
	 */
	private static final long serialVersionUID = -6312809072912568634L;
	
	final JComboBox<String> cmbSavedSpecs = new JComboBox<String>();
	private JComboBox<String> COMMPortDropList;
	private JPanel contentPane;
	private JPanel modelPanel;
	private JTextField txtTLCornerX;
	private JTextField txtNumRows;
	private JTextField txtNumCols;
	private JTextField txtWidth;
	private JTextField txtLength;
	private JTextField txtXpos;
	private JTextField txtYpos;
	private JLabel lblPlateSpecifications;
	private JLabel lblPosOfCorner;
	private JLabel lblNewLabel_2;
	private JLabel lblOfRows;
	private JLabel lblOfCols;
	private JLabel lblWidth;
	private JLabel lblLength;
	private JLabel lblXpos;
	private JLabel lblYpos;
	private JButton btnMakePlate;
	private JLabel lblX;
	private JLabel lblY;
	private JTextField txtTLCornerY;
	private JLabel lblWellD;
	private JTextField txtWellDiameter;
	private JLabel lblToChange;
	private JLabel lblHeight;
	private JTextField BoundingHeightText;
	private JButton UpdateButton;
	private JLabel lblWidth_2;
	private JTextField BoundingWidthText;
	private JTabbedPane tabbedPane;
	private JButton ClearPlatesButton;
	private JButton ConnectButton;
	private JLabel lblWellVol;
	private JTextField txtWellVolume;
	private JLabel lblNewLabel;
	private JLabel lblWellWellSpacing;
	private JTextField txtWellWellSpacing;
	private JButton btnSaveSpecs;
	private JTextField txtPlateNickname;
	private JButton btnLoadSpecs;
	private JButton btnDeleteSpecs;
	private JPanel pnlDemoDeviceControl;
	private JLabel lblCreateBasicTasks;
	private TaskCreationPanel pnlTaskManagement;
	

	/**
	 * Create the frame.
	 */
	public MainGUI(IView2PlateAdapter IView2PlateAdapter, SerialCommAdapter serialAdapter,
			TaskAdapter<TFactoryItem> taskAdapter, IView2SerializationAdapter IView2SerializationAdapter) {
		plateModelAdapter = IView2PlateAdapter;
		serialModelAdapter = serialAdapter;
		taskModelAdapter = taskAdapter;
		this.IView2SerializationAdapter = IView2SerializationAdapter;
		initGUI();
	}
	
	/**
	 * (Mostly) generated code to create various panels on the window.
	 */
	public void initGUI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		/**
		 * Panel system will actually draw objects on.
		 */
		modelPanel = new JPanel() {
			private static final long serialVersionUID = 6634648507431653549L;

			/**
			* Overridden paintComponent method to paint a model of the aperture on.
			* @param g The Graphics object to paint on.
			**/
			public void paintComponent(Graphics g) {
			    super.paintComponent(g);   	// Do everything normally done first, e.g. clear the screen.
			    plateModelAdapter.paint(g);  	// call back to the model to paint the aperture
			}
		};
		modelPanel.setToolTipText("");
		modelPanel.setBackground(Color.WHITE);
		
		contentPane.add(modelPanel, BorderLayout.CENTER);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.EAST);
		
		JPanel plateSetupPanel = new JPanel();
		tabbedPane.addTab("Plate Setup", null, plateSetupPanel, null);
		plateSetupPanel.setLayout(new MigLayout("", "[grow][grow][grow][grow]", "[grow][][][][][][][][][][][][][][][][][][][-1.00][][][][][][][grow]"));
		
		lblPlateSpecifications = new JLabel("Plate Specifications (in mm)");
		plateSetupPanel.add(lblPlateSpecifications, "cell 0 2 3 1,alignx center");
		
		lblX = new JLabel("x");
		plateSetupPanel.add(lblX, "cell 1 3,alignx center");
		
		lblY = new JLabel("y");
		plateSetupPanel.add(lblY, "cell 2 3,alignx center");
		
		lblNewLabel_2 = new JLabel("TL Corner Well");
		lblNewLabel_2.setToolTipText("Coordinate for middle of well in the top-left corner.");
		plateSetupPanel.add(lblNewLabel_2, "cell 0 4,alignx center");
		
		txtTLCornerX = new JTextField();
		txtTLCornerX.setText("23.16");
		plateSetupPanel.add(txtTLCornerX, "cell 1 4,growx");
		txtTLCornerX.setColumns(10);
		
		txtTLCornerY = new JTextField();
		txtTLCornerY.setText("24.76");
		plateSetupPanel.add(txtTLCornerY, "cell 2 4,growx");
		txtTLCornerY.setColumns(10);
		
		lblWellWellSpacing = new JLabel("Well-Well D");
		plateSetupPanel.add(lblWellWellSpacing, "cell 0 5,alignx center");
		
		txtWellWellSpacing = new JTextField();
		txtWellWellSpacing.setText("39.12");
		plateSetupPanel.add(txtWellWellSpacing, "cell 1 5 2 1,growx");
		txtWellWellSpacing.setColumns(10);
		
		lblOfRows = new JLabel("# of rows");
		plateSetupPanel.add(lblOfRows, "cell 0 6,alignx center");
		
		txtNumRows = new JTextField();
		txtNumRows.setText("2");
		plateSetupPanel.add(txtNumRows, "cell 1 6 2 1,growx");
		txtNumRows.setColumns(10);
		
		lblOfCols = new JLabel("# of cols");
		plateSetupPanel.add(lblOfCols, "cell 0 7,alignx center");
		
		txtNumCols = new JTextField();
		txtNumCols.setText("3");
		plateSetupPanel.add(txtNumCols, "cell 1 7 2 1,growx");
		txtNumCols.setColumns(10);
		
		lblWidth = new JLabel("width");
		lblWidth.setToolTipText("Width of entire plate (short side)");
		plateSetupPanel.add(lblWidth, "cell 0 8,alignx center");
		
		txtWidth = new JTextField();
		txtWidth.setText("85.48");
		plateSetupPanel.add(txtWidth, "cell 1 8 2 1,growx");
		txtWidth.setColumns(10);
		
		lblLength = new JLabel("length");
		lblLength.setToolTipText("Length of entire plate (long side)");
		plateSetupPanel.add(lblLength, "cell 0 9,alignx center");
		
		txtLength = new JTextField();
		txtLength.setText("127.76");
		plateSetupPanel.add(txtLength, "cell 1 9 2 1,growx");
		txtLength.setColumns(10);
		
		lblWellD = new JLabel("well diameter");
		lblWellD.setToolTipText("Diameter of a single well.");
		plateSetupPanel.add(lblWellD, "cell 0 10,alignx center");
		
		txtWellDiameter = new JTextField();
		txtWellDiameter.setText("35.43");
		plateSetupPanel.add(txtWellDiameter, "cell 1 10 2 1,growx");
		txtWellDiameter.setColumns(10);
		
		lblWellVol = new JLabel("well volume");
		lblWellVol.setToolTipText("Volume of a single well, in microLiters.");
		plateSetupPanel.add(lblWellVol, "cell 0 11,alignx center");
		
		txtWellVolume = new JTextField();
		txtWellVolume.setText("10");
		plateSetupPanel.add(txtWellVolume, "cell 1 11 2 1,growx");
		txtWellVolume.setColumns(10);
		
		txtPlateNickname = new JTextField();
		txtPlateNickname.setToolTipText("Insert nickname you would like to later refer to this specification as.");
		txtPlateNickname.setText("6-well");
		plateSetupPanel.add(txtPlateNickname, "cell 0 12,growx");
		txtPlateNickname.setColumns(10);
		
		plateSetupPanel.add(cmbSavedSpecs, "cell 1 12 2 1,growx");
		
		btnSaveSpecs = new JButton("Save Specs");
		btnSaveSpecs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IView2SerializationAdapter.saveSpecs(txtPlateNickname.getText(), packagePlateSpecs());
				updateDataCmb(IView2SerializationAdapter.updateDataList(SaveType.PLATE_SPEC), cmbSavedSpecs);
			}
		});
		plateSetupPanel.add(btnSaveSpecs, "cell 0 13,growx");
		
		btnLoadSpecs = new JButton("Load Specs");
		btnLoadSpecs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmbSavedSpecs.getItemCount() > 0){
					//get the plate spec currently selected in the combobox
					PlateSpecifications loadedSpecs = IView2SerializationAdapter.loadSpecs((String) cmbSavedSpecs.getSelectedItem());
					//reload the data into the fields
					txtTLCornerX.setText(Double.toString(loadedSpecs.getWellCorner().getX()));
					txtTLCornerY.setText(Double.toString(loadedSpecs.getWellCorner().getY()));
					txtWellWellSpacing.setText(Double.toString(loadedSpecs.getWellSpacing()));
					txtNumRows.setText(Integer.toString(loadedSpecs.getNumRows()));
					txtNumCols.setText(Integer.toString(loadedSpecs.getNumCols()));
					txtWidth.setText(Double.toString(loadedSpecs.getBorderDimensions().getY()));
					txtLength.setText(Double.toString(loadedSpecs.getBorderDimensions().getX()));
					txtWellDiameter.setText(Double.toString(loadedSpecs.getWellDiameter()));
					txtWellVolume.setText(Double.toString(loadedSpecs.getWellVolume()));
				}
			}
		});
		plateSetupPanel.add(btnLoadSpecs, "cell 1 13,growx");
		
		btnDeleteSpecs = new JButton("Delete Specs");
		btnDeleteSpecs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IView2SerializationAdapter.deleteData((String) cmbSavedSpecs.getSelectedItem(), SaveType.PLATE_SPEC);
				updateDataCmb(IView2SerializationAdapter.updateDataList(SaveType.PLATE_SPEC), cmbSavedSpecs);
			}
		});
		plateSetupPanel.add(btnDeleteSpecs, "cell 2 13,growx");
		
		lblPosOfCorner = new JLabel("Position of TL Corner");
		plateSetupPanel.add(lblPosOfCorner, "cell 0 16 3 1,alignx center");
		
		lblXpos = new JLabel("x-pos");
		lblXpos.setToolTipText("X coordinate for top-left corner of the plate.");
		plateSetupPanel.add(lblXpos, "cell 0 17,alignx center");
		
		txtXpos = new JTextField();
		txtXpos.setText("13.1");
		plateSetupPanel.add(txtXpos, "cell 1 17 2 1,growx");
		txtXpos.setColumns(10);
		
		lblYpos = new JLabel("y-pos");
		lblYpos.setToolTipText("Y coordinate for top-left corner of the plate.");
		plateSetupPanel.add(lblYpos, "cell 0 18,alignx center");
		
		txtYpos = new JTextField();
		txtYpos.setText("16.5");
		plateSetupPanel.add(txtYpos, "cell 1 18 2 1,growx");
		txtYpos.setColumns(10);
		
		final JComboBox<PlateNumbering> cmbNumberingOrderDroplist = new JComboBox<PlateNumbering>();
		cmbNumberingOrderDroplist.addItem(PlateNumbering.ROW);
		cmbNumberingOrderDroplist.addItem(PlateNumbering.COLUMN);
		plateSetupPanel.add(cmbNumberingOrderDroplist, "cell 0 20 3 1,growx");
		
		btnMakePlate = new JButton("Make Plate");
		btnMakePlate.addActionListener(new ActionListener() {
			/**
			 * Compiles plate specification data into a PlateSpecification wrapper, then
			 * sends that and the bottom-left coordinate of the plate to the model.
			 */
			public void actionPerformed(ActionEvent e) {
				PlateSpecifications specs = packagePlateSpecs();
				plateModelAdapter.addPlate((PlateNumbering) cmbNumberingOrderDroplist.getSelectedItem(), new Point2D.Double(Double.parseDouble(txtXpos.getText()), Double.parseDouble(txtYpos.getText())), specs);
			}
		});
		
		lblNewLabel = new JLabel("Numbering Order");
		plateSetupPanel.add(lblNewLabel, "cell 0 19 3 1,alignx center");
		
		plateSetupPanel.add(btnMakePlate, "cell 0 21 3 1,growx");
		
		ClearPlatesButton = new JButton("Clear All Plates");
		ClearPlatesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plateModelAdapter.clearAllPlates();
			}
		});
		plateSetupPanel.add(ClearPlatesButton, "cell 0 22 3 1,growx");
		
		lblToChange = new JLabel("To Change Bounding Box Size:");
		plateSetupPanel.add(lblToChange, "cell 0 23 3 1,alignx center");
		
		UpdateButton = new JButton("Update!");
		UpdateButton.addActionListener(new ActionListener() {
			/**
			 * Updates the height/width of the bounding box for the border frame.
			 */
			public void actionPerformed(ActionEvent e) {
				plateModelAdapter.setFrame(new Point2D.Double(Double.parseDouble(BoundingWidthText.getText()), Double.parseDouble(BoundingHeightText.getText())), modelPanel);
			}
		});
		
		lblWidth_2 = new JLabel("width");
		lblWidth_2.setToolTipText("Width of area arm can traverse");
		plateSetupPanel.add(lblWidth_2, "cell 0 24,alignx center");
		
		BoundingWidthText = new JTextField();
		BoundingWidthText.setText("223.46");
		plateSetupPanel.add(BoundingWidthText, "cell 1 24,growx");
		BoundingWidthText.setColumns(10);
		plateSetupPanel.add(UpdateButton, "cell 2 24 1 2,alignx center,aligny center");
		
		lblHeight = new JLabel("height");
		lblHeight.setToolTipText("Height of area arm can traverse");
		plateSetupPanel.add(lblHeight, "cell 0 25,alignx center");
		
		BoundingHeightText = new JTextField();
		BoundingHeightText.setText("93");
		plateSetupPanel.add(BoundingHeightText, "cell 1 25,growx");
		BoundingHeightText.setColumns(10);
		
		JPanel pnlArduinoSetup = new JPanel();
		tabbedPane.addTab("Arduino Setup", null, pnlArduinoSetup, null);
		pnlArduinoSetup.setLayout(new MigLayout("", "[grow][grow]", "[grow][][][][grow]"));
		
		JLabel lblArduino = new JLabel("Arduino");
		pnlArduinoSetup.add(lblArduino, "cell 0 1 2 1,alignx center");
		
		COMMPortDropList = new JComboBox<String>();
		pnlArduinoSetup.add(COMMPortDropList, "cell 0 2,growx");
		
		ConnectButton = new JButton("Connect!");
		ConnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serialModelAdapter.connectToPort((String)COMMPortDropList.getSelectedItem());
			}
		});
		pnlArduinoSetup.add(ConnectButton, "cell 1 2 1 2,growx");
		
		JButton btnRescan = new JButton("Rescan");
		btnRescan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//when we need to find ports again, request it from the serial model
				serialModelAdapter.scanForPorts();
			}
		});
		pnlArduinoSetup.add(btnRescan, "cell 0 3,growx");
		
		pnlDemoDeviceControl = new DemoDeviceControlPanel(taskModelAdapter, serialModelAdapter);
		tabbedPane.addTab("Create Basic Tasks", null, pnlDemoDeviceControl, null);
		
		pnlTaskManagement = new TaskCreationPanel(taskModelAdapter, IView2SerializationAdapter);
		tabbedPane.addTab("Task Management", null, pnlTaskManagement, null);
		
		/**
		 * Handle all well selection GUI code here.
		 */
		modelPanel.addMouseListener(new MouseListener(){
			
			private Point sourceLocation;
			
            @Override
            public void mouseClicked(MouseEvent e) {
            	
            }

			@Override
			public void mousePressed(MouseEvent e) {
				sourceLocation = e.getPoint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * Starts the already initialized frame, making it 
	 * visible and ready to interact with the user. 
	 */ 
	public void start(){
		/**
		 * Now that we know what we're going to draw everything on, draw a border representing the arm's bounds.
		 */
		plateModelAdapter.setFrame(new Point2D.Double(Double.parseDouble(BoundingWidthText.getText()), Double.parseDouble(BoundingHeightText.getText())), modelPanel);
		updateDataCmb(IView2SerializationAdapter.updateDataList(SaveType.PLATE_SPEC), cmbSavedSpecs);
		pnlTaskManagement.start();
		setVisible(true);
	}
	
	/**
	 * When a request is made, repaint all windows.
	 */
	public void update(){
		contentPane.repaint();
	}
	
	/**
	 * Helper function that takes current spec fields and produces a new PlateSpecifications
	 * object from them.
	 * @return PlateSpecifications object with currently entered data
	 */
	private PlateSpecifications packagePlateSpecs(){
		return new PlateSpecifications(
				Double.parseDouble(txtTLCornerX.getText()), Double.parseDouble(txtTLCornerY.getText()),
				Double.parseDouble(txtWellWellSpacing.getText()),
				Integer.parseInt(txtNumRows.getText()),
				Integer.parseInt(txtNumCols.getText()),
				Double.parseDouble(txtWidth.getText()),
				Double.parseDouble(txtLength.getText()),
				Double.parseDouble(txtWellDiameter.getText()),
				Double.parseDouble(txtWellVolume.getText()));
	}
	
	/**
	 * Updates the plate specification combobox to reflect input filenames.
	 * @param filenames - list of names to put in the combobox
	 */
	private void updateDataCmb(Iterable<String> filenames, JComboBox<String> cmb){
		cmb.removeAllItems();
		for (String filename : filenames){
			cmb.addItem(filename);
		}
	}
	
	/**
	 * Displays whatever serial ports the serial model found. Preps the droplist, puts in whatever the serial model found,
	 * then selects the first one (if there is one).
	 */
	public void addPortsToList(ArrayList<String> ports){
		//remove all items to avoid duplicates, items are only relevant since last scan anyways
		COMMPortDropList.removeAllItems();
		
		//add all ports found to the COMMPortDropList
		for (String portName : ports){
			COMMPortDropList.insertItemAt(portName, 0);
		}
		
		//And select the first one (if there is one), just to get something selected.
		if (COMMPortDropList.getItemCount() > 0){
			COMMPortDropList.setSelectedIndex(0);
		}
	}

	public void setTask(MultiTask taskQueue) {
		pnlTaskManagement.setTask(taskQueue);
	}
}
