package simstation;

import java.io.Serializable;
import mvc.*;

public abstract class Agent implements Serializable, Runnable {

	protected String name;
	transient protected Thread myThread;
	private int xc, yc;
	private boolean suspended, stopped;
	protected Simulation world;
	protected Heading heading;
	
	public Agent() {
		this.name = null;
		suspended = false;
		stopped = false;
		myThread = null;
		heading = Heading.random();
	}

	public Agent(String name) {
		this.name = name;
		suspended = false;
		stopped = false;
		myThread = null;
		heading = Heading.random();
	}
	
	public void onStart() {}
	public void onInterrupted() {}
	public void onExit() {}
	
	public void run() {
		myThread = Thread.currentThread();
		while (!isStopped()) {
			try {
				update();
				Thread.sleep(20);
				checkSuspended();
			} catch(InterruptedException e) {
				// manager.println(e.getMessage());
			}
		}
		// manager.stdout.println(name + " stopped");
	}
	
	// thread stuff:
	public synchronized void start() {

	}
	public synchronized void stop() { stopped = true; }
	public synchronized void suspend() { suspended = true; }
	public synchronized void resume() { notify(); }
	// wait for me to die:
	public synchronized void join() {
		try {
			if (myThread != null) myThread.join();
		} catch(InterruptedException e) {
			// manager.println(e.getMessage());
		}
	}
	// wait for notification if I'm not stopped and I am suspended
	private synchronized void checkSuspended() {
		try {
			while(!stopped && suspended) {
				wait();
				suspended = false;
			}
		} catch (InterruptedException e) {
			// manager.println(e.getMessage());
		}
	}

	public abstract void update();

	public void move(int steps) {
		// TODO boundary check
		switch (heading) {
			case EAST:
				xc++;
				break;
			case NORTH:
				yc--;
				break;
			case NORTHEAST:
				xc++;
				yc--;
				break;
			case NORTHWEST:
				xc--;
				yc--;
				break;
			case SOUTH:
				yc++;
				break;
			case SOUTHEAST:
				xc++;
				yc++;
				break;
			case SOUTHWEST:
				xc--;
				yc++;
				break;
			case WEST:
				xc--;
				break;
			default:
				break;
		}
	}

	public Heading getHeading() { return heading; }
	public int getXc() { return xc; }
	public int getYc() { return yc; }
	public double getDistance(Agent neighbor) {
		if (neighbor == null) {
			return Double.MAX_VALUE;
		}
		int nxc = neighbor.getXc();
		int nyc = neighbor.getYc();
		return Math.sqrt(Math.pow((xc - nxc), 2) + Math.pow((yc - nyc), 2));
	}
	
	public synchronized boolean isSuspended() { return suspended; }
	public synchronized boolean isStopped() { return stopped; }
	
	public void setWorld(Simulation world) { this.world = world; }
	public String getName() { return name; }
	public synchronized String toString() {
		String result = name;
		if (stopped) result += " (stopped)";
		else if (suspended) result += " (suspended)";
		else result += " (running)";
		return result;
	}
	
}
