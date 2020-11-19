package us.schmee.game.gfx.ui;

import us.schmee.game.Game;
import us.schmee.game.gfx.Colors;
import us.schmee.game.gfx.Font;
import us.schmee.game.gfx.Screen;
import us.schmee.game.gfx.ui.buttons.Button;

public class CharCreationMenu {
	public Button rUp, rDown, gUp, gDown, bUp, bDown, accept, back;
	public Button[] buttons = new Button[8];
	
	public int r = 0, g = 0, b = 0;

	
	private Game game;
	
	public CharCreationMenu(Game game) {
		this.game = game;
		buttons[0] = rUp = new Button(Game.WIDTH / 2 + 12, 75, ">", 0);
		buttons[1] = rDown = new Button(Game.WIDTH / 2 - 8, 75, "<", 1);
		buttons[2] = gUp = new Button(Game.WIDTH / 2 + 12, 90, ">", 2);
		buttons[3] = gDown = new Button(Game.WIDTH / 2 - 8, 90, "<", 3);
		buttons[4] = bUp = new Button(Game.WIDTH / 2 + 12, 105, ">", 4);
		buttons[5] = bDown= new Button(Game.WIDTH / 2 - 8, 105, "<", 5);
		buttons[6] = back = new Button(Game.WIDTH / 2 - (("back").length() + 1) / 2 * 8, 140, "back", 6);
		buttons[7] = accept = new Button(Game.WIDTH / 2 - (("accept").length() + 1) / 2 * 8, 125, "accept", 7);
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
		
		Font.render("Shirt Color", screen, Game.WIDTH / 2 - ("Shirt Color").length() / 2 * 8 - 5, 55, Colors.get(-1, -1, -1, 555), 1);
		
		Font.render("R  " + r, screen, Game.WIDTH / 2 - 22, 75, Colors.get(-1, -1, -1, 555), 1);
		Font.render("G  " + g, screen, Game.WIDTH / 2 - 22, 90, Colors.get(-1, -1, -1, 555), 1);
		Font.render("B  " + b, screen, Game.WIDTH / 2 - 22, 105, Colors.get(-1, -1, -1, 555), 1);
	}
}
