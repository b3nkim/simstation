package mvc;

abstract public class Model extends Bean {

	private boolean unsavedChanges = false;
	private String fileName = null;

	public void changed() {
		unsavedChanges = true;
		firePropertyChange(null, null, null);
	}

	public void changed(String name, Object oldVal, Object newVal) {
		unsavedChanges = true;
		firePropertyChange(name, oldVal, newVal);
	}

	public boolean getUnsavedChanges() {
		return unsavedChanges;
	}

	public void setUnsavedChanges(boolean unsavedChanges) {
//		boolean flag = this.unsavedChanges;
		this.unsavedChanges = unsavedChanges;
//		firePropertyChange("unsavedChanges", flag, unsavedChanges);
//		this.unsavedChanges = false;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}

