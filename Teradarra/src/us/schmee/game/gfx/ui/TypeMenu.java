package us.schmee.game.gfx.ui;

import us.schmee.game.Game;
import us.schmee.game.gamestates.State;
import us.schmee.game.gfx.Colors;
import us.schmee.game.gfx.Font;
import us.schmee.game.gfx.Screen;
import us.schmee.game.gfx.ui.buttons.Button;

public class TypeMenu {
	public Button accept, back;
	public Button[] buttons = new Button[2];
	
	public String typeString = "", displayText = "";
	
	private Game game;
	
	public TypeMenu(Game game) {
		this.game = game;
		buttons[0] = back = new Button(Game.WIDTH / 2 - (("back").length() + 1) / 2 * 8, 140, "back", 6);
		buttons[1] = accept = new Button(Game.WIDTH / 2 - (("accept").length() + 1) / 2 * 8, 125, "accept", 7);
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
				Font.render(b.getString(), screen, b.getX(), b.getY(), Colors.get(-1, -1, -1, color), 1);
			}
		}
		
		Font.render(displayText, screen, Game.WIDTH / 2 - (displayText).length() / 2 * 8, 55, Colors.get(-1, -1, -1, 555), 1);
		
		Font.render(typeString, screen, Game.WIDTH / 2 - (typeString).length() / 2 * 9, 75, Colors.get(-1, -1, -1, 555), 1);
		
	}
	
	public String getName() {
		
		game.state = State.multiplayer;
		return "";
	}
	
	public void removeLastChar() {
		if (typeString != null && typeString.length() > 0) {
			typeString = typeString.substring(0, typeString.length() - 1);
		}
	}
}
