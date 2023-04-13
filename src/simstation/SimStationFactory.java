package simstation;

import mvc.*;

public class SimStationFactory implements AppFactory {

	@Override
	public Model makeModel() {
		return new Simulation();
	}

	@Override
	public View makeView(Model model) {
		return new SimulationView((Simulation)model);
	}

	@Override
	public String[] getEditCommands() {
		return new String[] {"Start", "Suspend", "Resume", "Stop", "Stats"};
	}

	@Override
	public Command makeEditCommand(Model model, String type, Object source) {
		switch (type) {
			case "Start": {
				return new StartCommand(model);
			}

			case "Suspend": {
				return new SuspendCommand(model);
			}

			case "Resume": {
				return new ResumeCommand(model);
			}

			case "Stop": {
				return new StopCommand(model);
			}

			case "Stats": {
				return new StatsCommand(model);
			}
		}

		return null;
	}

	@Override
	public String getTitle() {
		return "SimStation";
	}

	@Override
	public String[] getHelp() {
		return new String[]{"Start: Runs the simulation", "Suspend: Pauses the simulation",
				"Resume: Resumes the simulation", "Stop: Ends the simulation",
				"Stats: Displays stats for the simulation"};
	}

	@Override
	public String about() {
		return new String("Spring 2023 CS-151 Sec 02 Team 8 - SimStation");
	}
}
