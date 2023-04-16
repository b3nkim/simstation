package simstation;

import java.io.Serializable;
import mvc.*;

public abstract class Agent implements Serializable, Runnable {

	private static int SIZE = 500;

	protected String name;
	transient protected Thread myThread;
	private int xc, yc;
	private boolean suspended, stopped;
	protected Simulation world;
	protected Heading heading;

	public Agent() {
		this.name = null;
		myThread = null;
		xc = Utilities.rng.nextInt(SIZE);
		yc = Utilities.rng.nextInt(SIZE);
		suspended = false;
		stopped = false;
		world = null;
		heading = Heading.random();
	}

	public Agent(String name) {
		this.name = name;
		suspended = false;
		stopped = false;
		myThread = null;
		world = null;
		heading = Heading.random();
	}

	public void onStart() {}
	public void onInterrupted() {}
	public void onExit() {}

	// thread stuff:
	public synchronized void start() {
		myThread = new Thread(this);
		myThread.start();
	}
	public synchronized void stop() { stopped = true; }
	public synchronized void suspend() { suspended = true; }
	public synchronized void resume() { notify(); }
	public synchronized void join() {
		try {
			if (myThread != null) 
				myThread.join();
		} catch(InterruptedException e) {
			// manager.println(e.getMessage());
		}
	}
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
		onStart();
		while (!isStopped()) {
			try {
				update();
				Thread.sleep(20);
				checkSuspended();
			} catch(InterruptedException e) {
				onInterrupted();
				// manager.println(e.getMessage());
			}
		}
		// manager.stdout.println(name + " stopped");
		onExit();
	}

	public abstract void update();

	public synchronized void move(int steps) {
		for (int i = 0; i < steps; i++) {
			switch (heading) {
				case EAST:
					xc++;
					if (xc >= SIZE - 10)
						heading = Heading.random();
					break;
				case NORTH:
					yc--;
					if (yc <= 0)
						heading = Heading.random();
					break;
				case NORTHEAST:
					xc++;
					yc--;
					if (xc >= SIZE - 10 || yc <= 0)
						heading = Heading.random();
					break;
				case NORTHWEST:
					xc--;
					yc--;
					if (xc <= 0 || yc <= 0)
						heading = Heading.random();
					break;
				case SOUTH:
					yc++;
					if (yc >= SIZE - 10)
						heading = Heading.random();
					break;
				case SOUTHEAST:
					xc++;
					yc++;
					if (xc >= SIZE - 10 || yc >= SIZE - 10)
						heading = Heading.random();
					break;
				case SOUTHWEST:
					xc--;
					yc++;
					if (xc <= 0 || yc >= SIZE - 10)
						heading = Heading.random();
					break;
				case WEST:
					xc--;
					if (xc <= 0)
						heading = Heading.random();
					break;
				default:
					break;
			}
//			if (xc <= 0 || xc >= SIZE - 10 || yc <= 0 || yc >= SIZE - 10)
//				heading = Heading.random();
			world.changed();
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
