package us.schmee.game.gfx.ui;

import us.schmee.game.Game;
import us.schmee.game.gfx.Colors;
import us.schmee.game.gfx.Font;
import us.schmee.game.gfx.Screen;
import us.schmee.game.gfx.ui.buttons.Button;

public class MainMenuGUI {
	
	public Button singleplayer, multiplayer, settings, quit;
	public Button[] buttons = new Button[4];
	
	private Game game;
	
	public MainMenuGUI(Game game) {
		this.game = game;
		buttons[0] = singleplayer = new Button(Game.WIDTH / 2 - (("singleplayer").length() + 1) / 2 * 8 + 1, 75, "singleplayer");
		buttons[1] = multiplayer = new Button(Game.WIDTH / 2 - (("multiplayer").length() + 1) / 2 * 7, 90, "multiplayer");
		buttons[2] = settings = new Button(Game.WIDTH / 2 - (("settings").length() + 1) / 2 * 8, 105, "settings");
		buttons[3] = quit = new Button(Game.WIDTH / 2 - (("quit").length() + 1) / 2 * 8, 120, "quit");
	}
	
	public void render(Screen screen) {
		if (buttons[0] != null) {
			for (Button b : buttons) {
				int color = 555;
				if (b.isHovered()) {
					color = 151;
				}
				Font.render(b.getString(), screen, b.getX(), b.getY(), Colors.get(-1, -1, -1, color), 1);
			}
		}
		
		Font.render("Teradarra", screen, Game.WIDTH / 2 - (("Teradarra").length() - 1) / 2 * 8 - 4, 25, Colors.get(-1, -1, -1, 242), 1);
	}

}
