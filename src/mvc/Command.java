package mvc;

abstract public class Command {
	
	protected Model model;
	
	public Command(Model model) {
		this.model = model;
	}

	abstract public void execute() throws Exception;
//	abstract public void undo();
}
