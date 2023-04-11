package mvc;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class View extends JPanel implements PropertyChangeListener {
	
	public static Integer SIZE = 500;
	
	protected Model model;

	public View(Model model) {
		// TODO Auto-generated constructor stub
		this.model = model;
		model.addPropertyChangeListener(this);
		setSize(SIZE, SIZE);
		Border blackline = BorderFactory.createLineBorder(Color.black);
        setBorder(blackline);
        setBackground(Color.LIGHT_GRAY);
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		repaint();
	}
	
	public void setModel(Model model) {
		this.model.removePropertyChangeListener(this);
		this.model = model;
		this.model.initSupport();
		this.model.addPropertyChangeListener(this);
		repaint();
	}

}
