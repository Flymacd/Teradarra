package us.schmee.game.files;

import java.io.*;

import us.schmee.game.Game;
import us.schmee.game.entites.Entity;
import us.schmee.game.entites.mobs.Player;

public class ReadObjects extends SaveObjects {

	
	public ReadObjects(Game game) {
		super(game);
	}

	public void load(String fileName) {
		// Wrap all in a try/catch block to trap I/O errors.
		try {
			// Open file to read from
			FileInputStream saveFile = new FileInputStream(fileName);

			// Create an ObjectInputStream to get objects from save file.
			ObjectInputStream save = new ObjectInputStream(saveFile);

			// Now we do the restore.
			// readObject() returns a generic Object, we cast those back
			// into their original class type.
			// For primitive types, use the corresponding reference class.
			for (int i = 0; i < players.size(); i++) {
				players.add((Entity) save.readObject());
			}
			

			// Close the file.
			save.close(); // This also closes saveFile.
		} catch (Exception exc) {
			exc.printStackTrace(); // If there was an error, print the info.
		}
		// All done.
	}
	
	public Player loadSingleplayerGame (String fileName) {
		Player p = null;
		// Wrap all in a try/catch block to trap I/O errors.
		try {
			// Open file to read from
			FileInputStream saveFile = new FileInputStream(fileName);

			// Create an ObjectInputStream to get objects from save file.
			ObjectInputStream save = new ObjectInputStream(saveFile);

			// Now we do the restore.
			// readObject() returns a generic Object, we cast those back
			// into their original class type.
			// For primitive types, use the corresponding reference class.
			p = ((Player) save.readObject());
			

			// Close the file.
			save.close(); // This also closes saveFile.
		} catch (Exception exc) {
			exc.printStackTrace(); // If there was an error, print the info.
		}
		// All done.
		return p;
	}
	
	public void loadSettings(String fileName) {
		// Wrap all in a try/catch block to trap I/O errors.
		try {
			// Open file to read from
			FileInputStream saveFile = new FileInputStream(fileName);

			// Create an ObjectInputStream to get objects from save file.
			ObjectInputStream save = new ObjectInputStream(saveFile);

			// Now we do the restore.
			// readObject() returns a generic Object, we cast those back
			// into their original class type.
			// For primitive types, use the corresponding reference class.
			showFps = (Boolean) save.readObject();

			game.settings.setShowFps(showFps);
			
			// Close the file.
			save.close(); // This also closes saveFile.
		} catch (Exception exc) {
			exc.printStackTrace(); // If there was an error, print the info.
		}
		// All done.
	}
	
	@SuppressWarnings("unused")
	public void loadFirstRun(String fileName) {
		// Wrap all in a try/catch block to trap I/O errors.
		try {
			// Open file to read from
			FileInputStream saveFile = new FileInputStream(fileName);

			if (saveFile == null) {
				game.firstRun = true;
				return;
			}
			
			// Create an ObjectInputStream to get objects from save file.
			ObjectInputStream save = new ObjectInputStream(saveFile);

			// Now we do the restore.
			// readObject() returns a generic Object, we cast those back
			// into their original class type.
			// For primitive types, use the corresponding reference class.
			boolean firstRun = (Boolean) save.readObject();

			game.firstRun = firstRun;
			
			// Close the file.
			save.close(); // This also closes saveFile.
		} catch (Exception exc) {
			exc.printStackTrace(); // If there was an error, print the info.
		}
		// All done.
	}
	
	public File[] finder(String dirName) {
		File dir = new File(dirName);

		return dir.listFiles(new FilenameFilter() { 
		public boolean accept(File dir, String filename){ 
			return filename.endsWith(".sav");
			} 
		});
	 }
}
