package us.schmee.game.files;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import us.schmee.game.Game;
import us.schmee.game.entites.Entity;

public class SaveObjects {

	public boolean showFps;
	public ArrayList<Entity> players;
	protected Game game;
	
	public SaveObjects(Game game) {
		this.game = game;
	}
	
	public void newSave(String fileName) {
		try { // Catch errors in I/O if necessary.
			// Open a file to write to
			FileOutputStream saveFile = new FileOutputStream(fileName);

			// Create an ObjectOutputStream to put objects into save file.
			ObjectOutputStream save = new ObjectOutputStream(saveFile);

			// Now we do the save.
			for (Entity e : players) {
				save.writeObject(e);
			}

			save.flush();
			
			// Close the file.
			save.close(); // This also closes saveFile.
		} catch (Exception exc) {
			exc.printStackTrace(); // If there was an error, print the info.
		}
	}
	
	public void newSettingsSave(String fileName, boolean showFps) {
		this.showFps = showFps;
		try { // Catch errors in I/O if necessary.
			// Open a file to write to
			FileOutputStream saveFile = new FileOutputStream(fileName);

			// Create an ObjectOutputStream to put objects into save file.
			ObjectOutputStream save = new ObjectOutputStream(saveFile);

			// Now we do the save.
			save.writeObject(showFps);
			
			save.flush();

			// Close the file.
			save.close(); // This also closes saveFile.
		} catch (Exception exc) {
			exc.printStackTrace(); // If there was an error, print the info.
		}
	}
	
	public void newSaveFirstRun(String fileName, boolean firstRun) {
		try { // Catch errors in I/O if necessary.
			// Open a file to write to
			FileOutputStream saveFile = new FileOutputStream(fileName);

			// Create an ObjectOutputStream to put objects into save file.
			ObjectOutputStream save = new ObjectOutputStream(saveFile);

			// Now we do the save.
			save.writeObject(firstRun);
			
			save.flush();

			// Close the file.
			save.close(); // This also closes saveFile.
		} catch (Exception exc) {
			exc.printStackTrace(); // If there was an error, print the info.
		}
	}
}