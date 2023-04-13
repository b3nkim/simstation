package simstation;

import mvc.*;

public class StatsCommand extends Command {

	public StatsCommand(Model model) {
		super(model);
	}

	@Override
	public void execute() throws Exception {
		Simulation simulation = (Simulation)this.model;
		simulation.stats();
	}
}
