package us.schmee.game.gfx.ui;

import us.schmee.game.Game;
import us.schmee.game.gfx.Colors;
import us.schmee.game.gfx.Font;
import us.schmee.game.gfx.Screen;
import us.schmee.game.gfx.ui.buttons.Button;

public class SettingsMenu {
	public Button showfps, back, apply;
	public Button[] buttons = new Button[3];
	
	private Game game;
	
	public SettingsMenu(Game game) {
		this.game = game;
		buttons[0] = back = new Button(Game.WIDTH / 2 - (("back").length() + 1) / 2 * 8, Game.HEIGHT / 2 + 50, "back");
		buttons[1] = showfps = new Button(Game.WIDTH / 2 - (("showfps").length() + 1) / 2 * 8 - 25, 50, "showfps");
		buttons[2] = apply = new Button(Game.WIDTH / 2 - (("apply").length()) / 2 * 10, Game.HEIGHT / 2 + 30, "apply");
	}
	
	public void render(Screen screen) {
		if (buttons[0] != null) {
			for (Button b : buttons) {
				int color = 555;
				if (b.isHovered()) {
					color = 151;
				}
				if (b.getString().equalsIgnoreCase("apply") && game.settings.isChanged()) {
					Font.render(b.getString(), screen, b.getX(), b.getY(), Colors.get(-1, -1, -1, color), 1);
				} else if (!(b.getString().equalsIgnoreCase("apply"))) {
					Font.render(b.getString(), screen, b.getX(), b.getY(), Colors.get(-1, -1, -1, color), 1);
				}
			}
		}
		
		Font.render(" = " + game.settings.isShowFps(), screen, Game.WIDTH / 2, 50, Colors.get(-1, -1, -1, 555), 1);
	}
}
