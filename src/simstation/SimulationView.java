package simstation;

import java.awt.*;

import mvc.*;

public class SimulationView extends View {
	public SimulationView(Simulation m) {
		super(m);
	}

	@Override
	public void setModel(Model newModel) {
		this.model = newModel;
		repaint();
		updateUI();
	}

	public void paintComponent(Graphics gc) {
		super.paintComponent(gc);
		Color oldColor = gc.getColor();
		gc.setColor(Color.BLACK);
		gc.drawLine(0, SIZE, SIZE, SIZE);
		gc.drawLine(SIZE, 0, SIZE, SIZE);
		gc.setColor(Color.WHITE);
		Simulation simulation = (Simulation)this.model;
		for (Agent agent : simulation.getAgents()) {
			gc.drawOval(agent.getXc(), agent.getYc(), 10, 10);
		}
		gc.setColor(oldColor);
	}
}