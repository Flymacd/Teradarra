package us.schmee.game.gfx.ui;

import java.awt.Color;
import java.awt.Graphics;

import us.schmee.game.Game;
import us.schmee.game.gfx.Colors;
import us.schmee.game.gfx.Font;
import us.schmee.game.gfx.Screen;
import us.schmee.game.gfx.ui.buttons.Button;

public class MultiplayerMenu {
	
	public Button host, join, back;
	public Button[] buttons = new Button[3];
	private Game game;
	
	public MultiplayerMenu(Game game) {
		this.game = game;
		buttons[0] = host = new Button(Game.WIDTH / 2 - (("host").length() + 1) / 2 * 8, 75, "host");
		buttons[1] = join = new Button(Game.WIDTH / 2 - (("join").length() + 1) / 2 * 8, 90, "join");
		buttons[2] = back = new Button(Game.WIDTH / 2 - (("back").length() + 1) / 2 * 8, 105, "back");
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
	}
}
