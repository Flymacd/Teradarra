package us.schmee.game.handlers.input;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import us.schmee.game.Game;
import us.schmee.game.gamestates.State;
import us.schmee.game.gfx.Colors;
import us.schmee.game.gfx.Screen;
import us.schmee.game.gfx.ui.buttons.Button;
import us.schmee.game.level.Level;

public class Mouse extends MouseAdapter {
  
	private Game game;
  
	// Delcaring
	public int mouseX = 0, mouseY = 0;
	public boolean pressed = false;
	public long tick = 0;
	
	public int typeMenuAccepts = 0;
	public String name = "", ip = "";
  
	public Mouse(Game game) {
		this.game = game;
		game.addMouseListener(this);
	}
  
	public void tick() {
		// Mouse Cursor
		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		Point c = game.getLocationOnScreen();
		mouseX = (int) b.getX() - (int) c.getX();
		mouseY = (int) b.getY() - (int) c.getY();
		// Mouse Cursor End
		
		if (game.state == State.mainmenu) { // Main Menu Buttons (Hover)
			for (Button bt : game.mainMenu.buttons) {
				if (mouseOver(mouseX, mouseY, bt.getX() * Game.SCALE + 10, bt.getY() * Game.SCALE - 12, bt.getWidth(), bt.getHeight())) {
					bt.setHovered(true);
				} else {
					bt.setHovered(false);
				}
			}
		} else if (game.state == State.multiplayer) { // Multiplayer Menu Buttons (Hover)
			for (Button bt : game.mpMenu.buttons) {
				if (mouseOver(mouseX, mouseY, bt.getX() * Game.SCALE + 10, bt.getY() * Game.SCALE - 12, bt.getWidth(), bt.getHeight())) {
					bt.setHovered(true);
				} else {
					bt.setHovered(false);
				}
			}
		} else if (game.state == State.settings) { // Settings Menu Buttons (Hover)
			for (Button bt : game.sMenu.buttons) {
				if (mouseOver(mouseX, mouseY, bt.getX() * Game.SCALE + 10, bt.getY() * Game.SCALE - 12, bt.getWidth(), bt.getHeight())) {
					bt.setHovered(true);
				} else {
					bt.setHovered(false);
				}
			}
		} else if (game.state == State.createchar) { // Create Character Menu Buttons (Hover)
			for (Button bt : game.ccMenu.buttons) {
				if (mouseOver(mouseX, mouseY, bt.getX() * Game.SCALE + 10, bt.getY() * Game.SCALE - 12, bt.getWidth(), bt.getHeight())) {
					bt.setHovered(true);
				} else {
					bt.setHovered(false);
				}
			}
		} else if (game.state == State.typeMenu) { // Type Menu Buttons (Hover)
			for (Button bt : game.typeMenu.buttons) {
				if (mouseOver(mouseX, mouseY, bt.getX() * Game.SCALE + 10, bt.getY() * Game.SCALE - 12, bt.getWidth(), bt.getHeight())) {
					bt.setHovered(true);
				} else {
					bt.setHovered(false);
				}
			}
		} else if (game.state == State.game || game.state == State.paused && game.displayInGameGUI) { // In Game GUI Buttons (Hover)
			for (Button bt : game.igGUI.buttons) {
				if (mouseOver(mouseX, mouseY, (bt.getX() / Game.SCALE / 7 + 10), (bt.getY() / Game.SCALE / 3 + (2 * 6)), bt.getWidth(), bt.getHeight())) {
					bt.setHovered(true);
				} else {
					bt.setHovered(false);
				}
			}
		} else if (game.state == State.loadgame) { // Create Character Menu Buttons (Hover)
			for (Button bt : game.lgs.buttons) {
				if (mouseOver(mouseX, mouseY, bt.getX() * Game.SCALE + 10, bt.getY() * Game.SCALE - 12, bt.getWidth(), bt.getHeight())) {
					bt.setHovered(true);
				} else {
					bt.setHovered(false);
				}
			}
		}
		
		
	}
  
	public void mousePressed(MouseEvent e) {
		if (game.state == State.game) {
			pressed = true;
		}
		
		if (game.state == State.mainmenu) { // Main Menu Buttons
			for (Button bt : game.mainMenu.buttons) {
				if (mouseOver(e.getX(), e.getY(), bt.getX() * Game.SCALE + 10, bt.getY() * Game.SCALE - 12, bt.getWidth(), bt.getHeight())) {
					if (bt.getString().equalsIgnoreCase("singleplayer")) {
						game.singlePlayer();
					} else if (bt.getString().equalsIgnoreCase("multiplayer")) {
						game.multiPlayer();
					} else if (bt.getString().equalsIgnoreCase("settings")) {
						game.settings();
					} else if (bt.getString().equalsIgnoreCase("quit")) {
						game.quit();
					}
				}
			}
		} else if (game.state == State.multiplayer) { // Multiplayer Menu Buttons
			for (Button bt : game.mpMenu.buttons) {
				if (mouseOver(e.getX(), e.getY(), bt.getX() * Game.SCALE + 10, bt.getY() * Game.SCALE - 12, bt.getWidth(), bt.getHeight())) {
					if (bt.getString().equalsIgnoreCase("host")) {
						game.hosting = 1;
						game.state = State.createchar;
					} else if (bt.getString().equalsIgnoreCase("join")) {
						game.hosting = 0;
						game.state = State.createchar;
					} else {
						game.state = State.mainmenu;
					}
				}
			}
		} else if (game.state == State.settings) { // Settings Menu Buttons
			for (Button bt : game.sMenu.buttons) {
				if (mouseOver(e.getX(), e.getY(), bt.getX() * Game.SCALE + 10, bt.getY() * Game.SCALE - 12, bt.getWidth(), bt.getHeight())) {
					if (bt.getString().equalsIgnoreCase("showfps")) {
						game.settings.setShowFps(!game.settings.isShowFps());
						game.settings.setChanged(true);
					} else if (bt.getString().equalsIgnoreCase("apply")) {
						if (game.settings.isChanged()) {
							if (!game.settingsFile.equalsIgnoreCase("")) {
								game.settings.apply(game.settingsFiles[0].getName());
								game.state = State.mainmenu;
							} else {
								game.gettingSettingsName = true;
								game.state = State.typeMenu;
							}
						}
					} else {
						game.state = State.mainmenu;
					}
				}
			}
		} else if (game.state == State.createchar) { // Settings Menu Buttons
			for (Button bt : game.ccMenu.buttons) {
				if (mouseOver(e.getX(), e.getY(), bt.getX() * Game.SCALE + 10, bt.getY() * Game.SCALE - 12, bt.getWidth(), bt.getHeight())) {
					if (bt.getString().equalsIgnoreCase("back")) {
						game.state = State.multiplayer;
					} else if (bt.getString().equalsIgnoreCase("accept")) {
						game.state = State.typeMenu;
					} else if (bt.getId() == game.ccMenu.rUp.getId()) {
						if (game.ccMenu.r < 5 && game.ccMenu.r >= 0) {
							game.ccMenu.r++;
						}
					} else if (bt.getId() == game.ccMenu.rDown.getId()) {
						if (game.ccMenu.r <= 5 && game.ccMenu.r > 0) {
							game.ccMenu.r--;
						}
					} else if (bt.getId() == game.ccMenu.gUp.getId()) {
						if (game.ccMenu.g < 5 && game.ccMenu.g >= 0) {
							game.ccMenu.g++;
						}
					} else if (bt.getId() == game.ccMenu.gDown.getId()) {
						if (game.ccMenu.g <= 5 && game.ccMenu.g > 0) {
							game.ccMenu.g--;
						}
					} else if (bt.getId() == game.ccMenu.bUp.getId()) {
						if (game.ccMenu.b < 5 && game.ccMenu.b >= 0) {
							game.ccMenu.b++;
						}
					} else if (bt.getId() == game.ccMenu.bDown.getId()) {
						if (game.ccMenu.b <= 5 && game.ccMenu.b > 0) {
							game.ccMenu.b--;
						}
					}
				}
			}
		} else if (game.state == State.typeMenu) {
			for (Button bt : game.typeMenu.buttons) {
				if (mouseOver(e.getX(), e.getY(), bt.getX() * Game.SCALE + 10, bt.getY() * Game.SCALE - 12, bt.getWidth(), bt.getHeight())) {
					if (bt.getString().equalsIgnoreCase("back")) {
						game.typeMenu.typeString = "";
						typeMenuAccepts = 0;
						if (game.gettingSettingsName) {
							game.state = State.settings;
						} else {
							game.state = State.createchar;
						}
					} else if (bt.getString().equalsIgnoreCase("accept")) {
						typeMenuAccept();
					}
				}
			}
		} else if (game.state == State.game || game.state == State.paused && game.displayInGameGUI) { // In Game GUI Buttons (Hover)
			for (Button bt : game.igGUI.buttons) {
				if (mouseOver(mouseX, mouseY, (bt.getX() / Game.SCALE / 7 + 10), (bt.getY() / Game.SCALE / 3 + (2 * 6)), bt.getWidth(), bt.getHeight())) {
					if (bt.getString().equalsIgnoreCase("main menu")) {
						game.mainMenu();
					}
				}
			}
		} else if (game.state == State.loadgame) { // Multiplayer Menu Buttons
			for (Button bt : game.lgs.buttons) {
				if (mouseOver(e.getX(), e.getY(), bt.getX() * Game.SCALE + 10, bt.getY() * Game.SCALE - 12, bt.getWidth(), bt.getHeight())) {
					if (bt.getString().equalsIgnoreCase("back")) {
						game.state = State.mainmenu;
					} else if (bt.getString().equalsIgnoreCase("load")) {
						
					}
				}
			}
		}
	}
	
	public void typeMenuAccept() {
		String totalColor = game.ccMenu.r + "" + game.ccMenu.g + "" + game.ccMenu.b;
		int color = Integer.parseInt(totalColor);
		if (game.hosting == 1 && !game.gettingSettingsName) {
			name = game.typeMenu.typeString;
			game.typeMenu.typeString = "";
			game.createCharacter(color, name, ip);
		} else if (game.hosting == 0 && !game.gettingSettingsName) {
			typeMenuAccepts++;
			if (typeMenuAccepts == 1) {
				name = game.typeMenu.typeString;
				game.typeMenu.typeString = "";
			} else {
				ip = game.typeMenu.typeString;
				game.typeMenu.typeString = "";
				game.createCharacter(color, name, ip);
			}
		} else if (game.gettingSettingsName) {
			game.settingsFile = game.typeMenu.typeString;
			game.typeMenu.typeString = "";
			game.gettingSettingsName = false;
			game.settings.applyFirstTime(game.settingsFile);
			game.state = State.mainmenu;
		}
	}
  
	public void mouseReleased(MouseEvent e) {
		if (game.state == State.game) {
			pressed = false;
		}
	}
	
	public void render(Screen screen, int xOffset, int yOffset) {
		if (xOffset < 0) {
			xOffset = 0;
		} else if (xOffset + screen.width > game.level.width * 8) {
			xOffset = game.level.width * 8 - screen.width;
		}
		
		if (yOffset <= 0) {
			yOffset = -3;
		} else if (yOffset + screen.height > game.level.height * 8) {
			yOffset = game.level.height * 8 - screen.height - 3;
		}
		
		if (game.state != State.game || game.state != State.paused) {
			xOffset = xOffset - 1;
			yOffset = yOffset - 1;
		}
		screen.render(mouseX / Game.SCALE + xOffset, mouseY / Game.SCALE + yOffset, 16 * 32, Colors.get(321, -1, 444, 151), 0x00, 1);
		
	}
  
	public boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
		if(mx > x && mx < x + width) {
			if(my > y && my < y + height) {
				return true;
			} else return false;
		} else return false;
	}
  
}