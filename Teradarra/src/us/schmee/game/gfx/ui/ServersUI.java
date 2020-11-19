package us.schmee.game.gfx.ui;

import us.schmee.game.Game;
import us.schmee.game.gfx.Colors;
import us.schmee.game.gfx.Font;
import us.schmee.game.gfx.Screen;
import us.schmee.game.gfx.ui.buttons.Button;

public class ServersUI {

	public Button join, back;
	public Button[] buttons = new Button[3];
	private Game game;
	
	public ServersUI(Game game) {
		this.game = game;
		buttons[0] = join = new Button(Game.WIDTH / 2 - (("join").length() + 1) / 2 * 8, 75, "join");
		buttons[1] = back = new Button(Game.WIDTH / 2 - (("back").length() + 1) / 2 * 8, 105, "back");
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
