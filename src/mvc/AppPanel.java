package mvc;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AppPanel extends JPanel implements ActionListener, PropertyChangeListener {

	protected Model model;
	protected AppFactory factory;
	protected View view;
	protected JPanel controlPanel; // not a separate class!
	private SafeFrame frame;
	private Container cp;
	public static int FRAME_WIDTH = 1000;
	public static int FRAME_HEIGHT = 500;

	public AppPanel(AppFactory factory) {
		super();
		this.factory = factory;
		
		model = factory.makeModel();
		view = factory.makeView(model);
		controlPanel = new JPanel(); // TODO
		
		view.setBackground(Color.DARK_GRAY);
		controlPanel.setBackground(Color.PINK);

		frame = new SafeFrame();
		cp = frame.getContentPane();
		cp.add(this);
		frame.setJMenuBar(this.createMenuBar());
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setTitle(factory.getTitle());
		
		this.setLayout((new GridLayout(1, 2)));
		this.add(controlPanel);
		this.add(view);

		display();
	}

	public void display() {
		frame.setVisible(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		/* override in extensions if needed */
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model.removePropertyChangeListener(this);
		this.model = model;
		this.model.initSupport(); // defined in Bean
		this.model.addPropertyChangeListener(this);
		view.setModel(this.model);
		this.model.changed();
	}


	protected JMenuBar createMenuBar() {
		JMenuBar result = new JMenuBar();
		
		String[] fileItems = {"New", "Save", "Open", "Quit"};
		JMenu fileMenu = Utilities.makeMenu("File", fileItems, this);
		result.add(fileMenu);

		String[] editCommands = factory.getEditCommands();
		JMenu editMenu = Utilities.makeMenu("Edit", editCommands, this);
		result.add(editMenu);

		JMenu helpMenu = Utilities.makeMenu("Help", 
				new String[]{"About", "Help"}, this);
		result.add(helpMenu);

		return result;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String actionCommand = ae.getActionCommand();
		try {
			switch (actionCommand) {
				case "New": {
					if (Utilities.confirm("Are you sure? Unsaved changes will be lost!")) {
						this.setModel(factory.makeModel());
						view = factory.makeView(this.model);
						
						this.removeAll();
						this.add(controlPanel);
						this.add(view);
						
						display();
					}
					break;
				}
				case "Save": {
					String fName = Utilities.getFileName((String) null, false);
					ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fName));
					os.writeObject(this.model);
					os.close();
					break;
				}
				case "Open": {
					if (Utilities.confirm("Are you sure? Unsaved changes will be lost!")) {
						String fName = Utilities.getFileName((String) null, true);
						ObjectInputStream is = new ObjectInputStream(new FileInputStream(fName));
						Model readModel = (Model) is.readObject();
						this.setModel(readModel);
						view = factory.makeView(this.model);
						
						this.removeAll();
						this.add(controlPanel);
						this.add(view);
						
						display();
						is.close();
					}
					break;
				}
				case "Quit": {
					if (Utilities.confirm("Are you sure? Unsaved changes will be lost!"))
						System.exit(0);
					break;
				}
				case "About": {
					Utilities.inform(factory.about());
				}
				case "Help": {
					Utilities.inform(factory.getHelp());
					break;
				}
				default: {					
					Command command = factory.makeEditCommand(model, actionCommand, ae.getSource());
					command.execute();
//					throw new Exception("Unrecognized command: " + actionCommand);
				}
			}

		} catch (Exception e) {
			handleException(e);
		}
	}

	protected void handleException(Exception e) {
		Utilities.error(e);
	}

}
