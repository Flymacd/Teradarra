package us.schmee.game.gfx.ui;

import us.schmee.game.Game;
import us.schmee.game.gamestates.State;
import us.schmee.game.gfx.Colors;
import us.schmee.game.gfx.Font;
import us.schmee.game.gfx.Screen;

public class HUD {

	private Game game;
	
	public HUD(Game game) {
		this.game = game;
	}
	
	public void render(Screen screen, int xOffset, int yOffset) {
		if (game.state == State.game || game.state == State.paused) {
			if (xOffset < 0) {
				xOffset = 0;
			} else if (xOffset + screen.width > game.level.width * 8) {
				xOffset = game.level.width * 8 - screen.width;
			} else {
				
			}
			
			if (yOffset <= 0) {
				yOffset = - 9;
			} else if (yOffset + screen.height > game.level.height * 8) {
				
				yOffset = game.level.height * 8 - screen.height - 9;
			} else {
				yOffset = yOffset - 9;
			}
			
			// Health
			double percentHealth = game.player.getHealth() / game.player.getMaxHealth() * 100;
			int healthColor = 511;
			if (percentHealth <= 100 && percentHealth >= 75) {
				healthColor = 511;
			} else if (percentHealth < 75 && percentHealth >= 50) {
				healthColor = 411;
			} else if (percentHealth < 50 && percentHealth >= 25) {
				healthColor = 311;
			} else if (percentHealth < 25) {
				healthColor = 211;
			}
			screen.render(xOffset, yOffset + screen.height - 20, 15 * 32, Colors.get(511, -1, 000, -1), 0x00, 1);
			Font.render((int) percentHealth + "%", screen, xOffset + 8, yOffset + screen.height - 20, Colors.get(-1, -1, -1, healthColor), 1);
			
			// Mana
			double percentMana = game.player.getMana() / game.player.getMaxMana() * 100;
			int manaColor = 115;
			if (percentMana <= 100 && percentMana >= 75) {
				manaColor = 115;
			} else if (percentMana < 75 && percentMana >= 50) {
				manaColor = 114;
			} else if (percentMana < 50 && percentMana >= 25) {
				manaColor = 113;
			} else if (percentMana < 25) {
				manaColor = 112;
			}
			screen.render(xOffset, yOffset + screen.height - 10, 2 + 15 * 32, Colors.get(115, -1, 000, -1), 0x00, 1);
			Font.render((int) percentMana + "%", screen, xOffset + 8, yOffset + screen.height - 11, Colors.get(-1, -1, -1, manaColor), 1);
					
			// Energy
			double percentEnergy = game.player.getEnergy() / game.player.getMaxEnergy() * 100;
			int energyColor = 151;
			if (percentEnergy <= 100 && percentEnergy >= 75) {
				energyColor = 151;
			} else if (percentEnergy < 75 && percentEnergy >= 50) {
				energyColor = 141;
			} else if (percentEnergy < 50 && percentEnergy >= 25) {
				energyColor = 232;
			} else if (percentEnergy < 25) {
				energyColor = 121;
			}
			screen.render(xOffset, yOffset + screen.height - 1, 1 + 15 * 32, Colors.get(151, -1, 000, -1), 0x00, 1);
			Font.render((int) percentEnergy + "%", screen, xOffset + 8, yOffset + screen.height - 2, Colors.get(-1, -1, -1, energyColor), 1);
		
		}
	}
}
