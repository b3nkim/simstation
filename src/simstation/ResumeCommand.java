package simstation;

import mvc.*;

public class ResumeCommand extends Command {

	public ResumeCommand(Model model) {
		super(model);
	}

	@Override
	public void execute() throws Exception {
		Simulation simulation = (Simulation)this.model;
		simulation.resume();
	}

}
