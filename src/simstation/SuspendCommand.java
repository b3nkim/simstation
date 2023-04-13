package simstation;

import mvc.*;

public class SuspendCommand extends Command {

	public SuspendCommand(Model model) {
		super(model);
	}

	@Override
	public void execute() throws Exception {
		Simulation simulation = (Simulation)this.model;
		simulation.suspend();
	}

}
