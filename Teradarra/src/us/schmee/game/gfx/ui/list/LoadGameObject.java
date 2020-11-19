package us.schmee.game.gfx.ui.list;

import java.io.File;

public class LoadGameObject {

	private File file;
	
	public LoadGameObject(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
}
