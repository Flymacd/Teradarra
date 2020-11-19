package us.schmee.game.gfx.ui;


import us.schmee.game.Game;
import us.schmee.game.gfx.Colors;
import us.schmee.game.gfx.Font;
import us.schmee.game.gfx.Screen;
import us.schmee.game.gfx.ui.buttons.Button;

public class InGameGUI {
	
	public Button mainMenu;
	public Button[] buttons = new Button[1];
	private Game game;
	
	public InGameGUI(Game game) {
		this.game = game;
		buttons[0] = mainMenu = new Button(0, 0, "main menu");
	}
	
	public void render(Screen screen, int xOffset, int yOffset) {
		if (xOffset < 0) {
			xOffset = screen.width / 2;
		} else if (xOffset + screen.width > game.level.width * 8) {
			xOffset = game.level.width * 8 - screen.width;
		} else { 
			xOffset = xOffset + screen.width / 2;
		}
		
		if (yOffset <= 0) {
			yOffset = 90;
		} else if (yOffset + screen.height > game.level.height * 8) {
			yOffset = game.level.height * 8 - screen.height;
		} else {
			yOffset = yOffset + 90;
		}
		
		if (buttons[0] != null) {
			for (Button b : buttons) {
				int color = 555;
				if (b.isHovered()) {
					color = 151;
				}
				b.setX((xOffset - (b.getString().length() / 2 * 9) / 9));
				b.setY(yOffset);
				Font.render(b.getString(), screen, xOffset - (b.getString().length() / 2 * 9), yOffset, Colors.get(-1, -1, -1, color), 1);
			}
		}
	}

}
