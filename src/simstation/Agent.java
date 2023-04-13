package simstation;

import java.io.Serializable;

public abstract class Agent implements Serializable, Runnable {

	protected String name;
	protected Thread myThread;
	private int xc, yc;
	private boolean suspended, stopped;
	protected Simulation world; // TODO

	public Agent(String name) {
		this.name = name;
		suspended = false;
		stopped = false;
		myThread = null;
	}

	public void setWorld(Simulation world) { this.world = world; }
	public String getName() { return name; }
	public synchronized String toString() {
		String result = name;
		if (stopped) result += " (stopped)";
		else if (suspended) result += " (suspended)";
		else result += " (running)";
		return result;
	}
	// thread stuff:
	public synchronized void stop() { stopped = true; }
	public synchronized boolean isStopped() { return stopped; }
	public synchronized void suspend() { suspended = true; }
	public synchronized boolean isSuspended() { return suspended;  }
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

	public void start() {

	}

	public abstract void update();	

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
}
