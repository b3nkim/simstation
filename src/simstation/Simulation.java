package simstation;

import java.util.*;
import mvc.*;

public class Simulation extends Model {

	transient private Timer timer; // timers aren't serializable
	private int clock;
	private List<Agent> agents;

	public Simulation() {
		super();
		agents = new ArrayList<Agent>();
	}

	public void start() {
		agents.clear();
		populate();
		for (Agent agent : agents) {
			agent.start();
		}
		startTimer();
	}

	public void suspend () {
		for (Agent agent : agents) {
			agent.suspend();
		}
		stopTimer();
	}

	public void resume() {
		for (Agent agent : agents) {
			agent.resume();
		}
		startTimer();
	}

	public void stop() {
		for (Agent agent : agents) {
			agent.stop();
		}
		agents.clear();
		stopTimer();
	}

	public String stats() {
		List<String> stats = getStatsArray();
		StringBuilder statsString = new StringBuilder();
		for (String stat : stats) {
			statsString.append(stat).append("\n");
		}
		return statsString.toString();
	}

	public List<String> getStatsArray() {
		List<String> stats = new ArrayList<>();
		return stats;
	}

	public Agent getNeighbor(Agent a, double radius) {
		int startIndex = agents.indexOf(a);
		int currentIndex = startIndex;
		int count = agents.size();
		Random random = Utilities.rng;

		while (count > 0) {
			int index = (currentIndex + random.nextInt(agents.size())) % agents.size();
			if (index == startIndex) {
				return null;
			}
			Agent neighbor = agents.get(index);

			if (neighbor != a && a.getDistance(neighbor) <= radius) {
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

	public int getClock() {
		return clock;
	}

	private void startTimer() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new ClockUpdater(), 1000, 1000);
	}

	private void stopTimer() {
		timer.cancel();
		timer.purge();
	}

	public List<Agent> getAgents() {
		return this.agents;
	}

	public void addAgent(Agent a) {
		agents.add(a);
		a.setWorld(this);
	}

	private class ClockUpdater extends TimerTask {
		public void run() {
			clock++;
			changed();
		}
	}
}
