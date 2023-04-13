package simstation;

import java.util.Timer;
import java.util.*;
import mvc.*;

public class Simulation extends Model {
	
	private Timer timer;
	private int clock;
	private List<Agent> agents = new ArrayList<Agent>();

	public void start() {
		populate();
		for (Agent agent : agents) {
			agent.start();
		}
	}

	public void suspend () {
		for (Agent agent : agents) {
			agent.suspend();
		}
	}

	public void resume() {
		for (Agent agent : agents) {
			agent.resume();
		}
	}

	public void stop() {
		for (Agent agent : agents) {
			agent.stop();
		}
	}

	public Agent getNeighbor(Agent a, double radius) {
		int startIndex = agents.indexOf(a);
		int currentIndex = startIndex;
		int count = agents.size();
		Random random = new Random();

		while (count > 0) {
			int index = (currentIndex + random.nextInt(agents.size())) % agents.size();
			if (index == startIndex) {
				return null;
			}
			Agent neighbor = agents.get(index);

			if (neighbor != a && a.distance(neighbor) <= radius) {
				return neighbor;
			}
			currentIndex = index;
			count--;
		}
		return null;
	}

	public void populate() {
		// Populate is an empty method that will be specified in subclasses
		// It's called by start and populates the simulation
	}

	private void startTimer() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new ClockUpdater(), 1000, 1000);
	}

	private void stopTimer() {
		timer.cancel();
		timer.purge();
	}

	private class ClockUpdater extends TimerTask {
		public void run() {
			clock++;
			changed();
		}
	}
}
