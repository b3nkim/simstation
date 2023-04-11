package mvc;

abstract public class Model extends Bean {

	private boolean unsavedChanges = false;
	private String fileName = null;
	
	// changed() for when we make a new model
	// changed() for when we change a prpperty
	
	protected void changed() {
		setUnsavedChanges(true);
	}

	public boolean getUnsavedChanges() {
		return unsavedChanges;
	}

	public void setUnsavedChanges(boolean unsavedChanges) {
		boolean flag = this.unsavedChanges;
		this.unsavedChanges = unsavedChanges;
		firePropertyChange("unsavedChanges", flag, unsavedChanges);
		this.unsavedChanges = false;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}

