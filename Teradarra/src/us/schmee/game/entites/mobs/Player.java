package us.schmee.game.entites.mobs;

import java.util.ArrayList;

import us.schmee.game.Game;
import us.schmee.game.gfx.Colors;
import us.schmee.game.gfx.Font;
import us.schmee.game.gfx.Screen;
import us.schmee.game.handlers.input.InputHandler;
import us.schmee.game.level.Level;
import us.schmee.game.net.packets.Packet02Move;

public class Player extends Mob {

	
	private int moveStop = 0;
	private InputHandler input;
	private ArrayList<String> combatCoords = new ArrayList<String>();
	protected int color;
	private int scale = 1;
	protected boolean isSwimming = false, inCombat = false, canBeInCombat = true, sprinting = false, usingEnergy = false;
	private int tickCount = 0, id, mx = 0, my = 0, walkingSpeed = 4;
	private long combatDelay = 0;
	private String username;
	
	private double health = 10.0, maxHealth = 10.0, energy = 10.0, maxEnergy = 10.0, mana = 10.0, maxMana = 10.0;
	
	public Player(Level level, int x, int y, InputHandler input, String username, int id) {
		super(level, "Player", x, y, 1);
		this.input = input;
		this.username = username;
		this.color = Colors.get(-1, 111, 145, 543);
		this.id = id;
	}

	public void tick() {
		int xa = 0;
		int ya = 0;
		
		if (input != null) {
			if (input.up.isPressed()) {ya--;}
			if (input.left.isPressed()) {xa--;}
			if (input.down.isPressed()) {ya++;}
			if (input.right.isPressed()) {xa++;}
			if (input.sprint.isPressed() && energy > 0 && isMoving) {
				speed = 2;
				walkingSpeed = 3;
				energy -= 0.006;
				usingEnergy = true;
			} else {
				speed = 1;
				walkingSpeed = 4;
				usingEnergy = false;
			}
			
			if (xa != 0 || ya != 0) {
				move(xa, ya);
				isMoving = true;
				if (Game.game.isMultiplayer) {
					Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y, this.numSteps, this.isMoving, this.movingDir, this.id);
					packet.writeData(Game.game.client);
					moveStop = 1;
				}
				
			} else {
				isMoving = false;
			}
			
		}
		
		if (level.getTile(this.x >> 3, this.y >> 3).getId() == 3) {
			isSwimming = true;
		} else {
			isSwimming = false;
		}
		
		if (!isMoving) {
			if (moveStop == 1) {
				if (Game.game.isMultiplayer) {
					Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y, this.numSteps, this.isMoving, this.movingDir, this.id);
					packet.writeData(Game.game.client);
					moveStop = 0;
				}
			}
		}
		
		if (System.currentTimeMillis() - combatDelay >= 250) {
			canBeInCombat = true;
		}
		
		if (!usingEnergy && energy < maxEnergy) {
			energy += 0.003;
		}
		
		tickCount++;
	}
	
	
	public void render(Screen screen) {
		
		int xTile = 0;
		int yTile = 28;
		
		int flipTop = (numSteps >> walkingSpeed) & 1, flipBottom = (numSteps >> walkingSpeed) & 1;
		
		if (movingDir == 1) {
			xTile += 2;
		} else if (movingDir > 1) {
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
		}
		
		int modifier = 8 * scale, xOffset = x - modifier / 2, yOffset = y - modifier / 2 - 4;
		
		if (isSwimming) {
			int waterColor = 0;
			yOffset += 4;
			if (tickCount % 60 < 15) {
				waterColor = Colors.get(-1, -1, 225, -1);
			} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
				yOffset -= 1;
				waterColor = Colors.get(-1, 225, 115, -1);
			} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
				waterColor = Colors.get(-1, 115, -1, 225);
			} else {
				yOffset -= 1;
				waterColor = Colors.get(-1, 225, 115, -1);
			}
			screen.render(xOffset, yOffset + 3, 0 + 27 * 32, waterColor, 0x00, 1);
			screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, waterColor, 0x01, 1);
		}
		
		if (!isMoving) {
			if (movingDir == 0) {
				// Top
				screen.render(xOffset, yOffset, 10 + yTile * 32, color, 0x00, scale);
				screen.render(xOffset + modifier, yOffset, (11) + yTile * 32, color, 0x00, scale);
				
				// Bottom
				if (!isSwimming) {
					screen.render(xOffset , yOffset + modifier, 10 + (yTile + 1) * 32, color, 0x00, scale);
					screen.render(xOffset + modifier, yOffset + modifier, (11) + (yTile + 1) * 32, color, 0x00, scale);
				}
			} else if (movingDir == 1) {
				// Top
				screen.render(xOffset, yOffset, 8 + yTile * 32, color, 0x00, scale);
				screen.render(xOffset + modifier, yOffset, (9) + yTile * 32, color, 0x00, scale);
				
				// Bottom
				if (!isSwimming) {
					screen.render(xOffset , yOffset + modifier, 8 + (yTile + 1) * 32, color, 0x00, scale);
					screen.render(xOffset + modifier, yOffset + modifier, (9) + (yTile + 1) * 32, color, 0x00, scale);
				}
			} else if (movingDir == 2) {
				// Top
				screen.render(xOffset + modifier, yOffset, 4 + yTile * 32, color, 0x01, scale);
				screen.render(xOffset, yOffset, (5) + yTile * 32, color, 0x01, scale);
				
				// Bottom
				if (!isSwimming) {
					screen.render(xOffset , yOffset + modifier, 4 + (yTile + 1) * 32, color, 0x00, scale);
					screen.render(xOffset + modifier, yOffset + modifier, (5) + (yTile + 1) * 32, color, 0x00, scale);
				}
			} else {
				// Top
				screen.render(xOffset, yOffset, 4 + yTile * 32, color, 0x00, scale);
				screen.render(xOffset + modifier, yOffset, (5) + yTile * 32, color, 0x00, scale);
				
				// Bottom
				if (!isSwimming) {
					screen.render(xOffset , yOffset + modifier, 4 + (yTile + 1) * 32, color, 0x00, scale);
					screen.render(xOffset + modifier, yOffset + modifier, (5) + (yTile + 1) * 32, color, 0x00, scale);
				}
			}
		} else {
			screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, color, flipTop, scale);
			screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, color, flipTop, scale);
			
			if (!isSwimming) {
				screen.render(xOffset + (modifier * flipBottom) , yOffset + modifier, xTile + (yTile + 1) * 32, color, flipBottom, scale);
				screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, color, flipBottom, scale);
			}
		}
		
		if (Game.game.isMultiplayer) {
			if (username != null) {
				Font.render(username, screen, xOffset - ((username.length() - 1) / 2 * 8), yOffset - 10, Colors.get(-1, -1, -1, 555), 1);
			}
		}
		
		
	}
	
	public boolean hasCollided(int xa, int ya) {
		int xMin = 0, xMax = 7, yMin = 3, yMax = 7;
		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMin)) {
				return true;
			}
		}
		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMax)) {
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMin, y)) {
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMax, y)) {
				return true;
			}
		}
		return false;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public int getColor() {
		return color;
	}
	
	public void setColor(int color) {
		this.color = color;
	}

	public String getString() {
		return username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isInCombat() {
		return inCombat;
	}

	public void setInCombat(boolean inCombat) {
		this.inCombat = inCombat;
	}

	public void setMouseX(int mx) {
		this.mx = mx;
	}
	
	public void setMouseY(int my) {
		this.my = my;
	}
	
	public void setMouseCoords(int mx, int my) {
		this.mx = (mx / Game.SCALE);
		this.my = (my / Game.SCALE);
		getCombatCoords().add(mx / Game.SCALE + "," + my / Game.SCALE);
	}

	public boolean isCanBeInCombat() {
		return canBeInCombat;
	}

	public void setCanBeInCombat(boolean canBeInCombat) {
		this.canBeInCombat = canBeInCombat;
	}
	
	public synchronized ArrayList<String> getCombatCoords() {
		return combatCoords;
	}

	public double getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public double getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public double getMaxEnergy() {
		return maxEnergy;
	}

	public void setMaxEnergy(int maxEnergy) {
		this.maxEnergy = maxEnergy;
	}

	public double getMaxMana() {
		return maxMana;
	}

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}

	public boolean isSprinting() {
		return sprinting;
	}

	public void setSprinting(boolean sprinting) {
		this.sprinting = sprinting;
	}
	
}

