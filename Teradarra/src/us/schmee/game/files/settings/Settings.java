package us.schmee.game.files.settings;

import javax.swing.JOptionPane;

import us.schmee.game.Game;

public class Settings {
	
	private boolean showFps = false, changed = false;
	public boolean loaded = false;
	private Game game;
	
	public Settings(Game game) {
		this.game = game;
	}
	
	public void apply(String fileName) {
		changed = false;
		if (fileName == null) return;
		fileName = System.getProperty("user.dir") + "/saves/settings/" + fileName;
		game.saveObjects.newSettingsSave(fileName, showFps);
	}
	
	public void applyFirstTime(String fileName) {
		changed = false;
		if (fileName == null) return;
		fileName = System.getProperty("user.dir") + "/saves/settings/" + fileName + ".sav";
		game.saveObjects.newSettingsSave(fileName, showFps);
	}
	
	public void load(String fileName) {
		changed = false;
		if (fileName == null) return;
		fileName = System.getProperty("user.dir") + "/saves/settings/" + fileName;
		game.readObjects.loadSettings(fileName);
	}

	public boolean isShowFps() {
		return showFps;
	}

	public void setShowFps(boolean showFps) {
		this.showFps = showFps;
	}

	public boolean isChanged() {
		return changed;
	}
	
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

}
