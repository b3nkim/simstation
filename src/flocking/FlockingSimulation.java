package flocking;

import mvc.*;
import simstation.*;

class Bird extends Agent {

	private int radius, speed;

	public Bird() {
		super();
		radius = 10;
		speed = Utilities.rng.nextInt(5) + 1;
	}

	@Override
	public void update() {
		Bird partner = (Bird) world.getNeighbor(this, radius);
		if (partner != null) {
			speed = partner.getSpeed();
			heading = partner.getHeading();
			move(speed);
		}
	}

	public int getSpeed() {
		return speed;
	}
}

class FlockingFactory extends SimStationFactory {
	public Model makeModel() { return new FlockingSimulation(); }
	public String getTitle() { return "Flocking";}
}

public class FlockingSimulation extends Simulation {

	private int numBirds = 30;

	public void populate() {
		for (int i = 0; i < numBirds; i++) {
			addAgent(new Bird());
		}
	}

	public String[] getStats() {
		String[] stats = new String[5];
		int[] speeds = new int[5];
		for (Agent agent : getAgents()) {
			Bird bird = (Bird) agent;
			speeds[bird.getSpeed() - 1]++;
		}
		for (int i = 0; i < 5; i++) {
			stats[i] = "#birds @ speed " + (i+1) + " = " + speeds[i];
		}
		return stats;
	}
	
	public static void main(String[] args) {
        AppPanel panel = new SimulationPanel(new FlockingFactory());
        panel.display();
    }

}
