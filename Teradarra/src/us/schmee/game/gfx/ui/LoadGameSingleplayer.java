package us.schmee.game.gfx.ui;

import us.schmee.game.Game;
import us.schmee.game.entites.mobs.Player;
import us.schmee.game.gfx.Colors;
import us.schmee.game.gfx.Font;
import us.schmee.game.gfx.Screen;
import us.schmee.game.gfx.ui.buttons.Button;
import us.schmee.game.gfx.ui.list.LoadGameObject;

public class LoadGameSingleplayer {
	
	public Button load, back;
	public Button[] buttons = new Button[2];
	public LoadGameObject[] files;
	public boolean isFileSelected = false;
	
	public Player player;
	
	private Game game;
	
	public LoadGameSingleplayer(Game game) {
		this.game = game;
		buttons[0] = back = new Button((("back").length() + 1) / 2 * 8, 90, "back", 6);
		buttons[1] = load = new Button((("load").length() + 1) / 2 * 8, 75, "load", 7);
		
		game.checkForSaveFiles();
		
		if (!game.firstRun) {
			files = new LoadGameObject[game.loadGameFiles.length];
			
			for (int i = 0; i < game.loadGameFiles.length; i++) {
				files[i] = new LoadGameObject(game.loadGameFiles[i]);
			}
		}
	}
	
	public void tick() {
		
	}
	
	public void render(Screen screen) {
		if (buttons[0] != null) {
			for (Button b : buttons) {
				int color = 555;
				if (b.isHovered()) {
					color = 151;
				}
				
				if (b.getString().equalsIgnoreCase("load") && !isFileSelected) { 
					color = 222;
				}
				
				Font.render(b.getString(), screen, b.getX(), b.getY(), Colors.get(-1, -1, -1, color), 1);
			}
		}
		
		Font.render("Select a File to Load", screen, Game.WIDTH / 2 - ("Select a File to Load").length() / 2 * 8, 15, Colors.get(-1, -1, -1, 555), 1);
		
		
	}
	
}
