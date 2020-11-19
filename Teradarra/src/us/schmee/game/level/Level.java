package us.schmee.game.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import us.schmee.game.Game;
import us.schmee.game.entites.Entity;
import us.schmee.game.entites.mobs.PlayerMP;
import us.schmee.game.gamestates.State;
import us.schmee.game.gfx.ImageLoader;
import us.schmee.game.gfx.Screen;
import us.schmee.game.level.tiles.Tile;

public class Level {
	
	private byte[] tiles;
	public int width, height;
	private int id = 0;
	public List<Entity> entities = new ArrayList<Entity>(), eToRemove = new ArrayList<Entity>();
	//public Entity[] entities = new Entity[12];
	private String imagePath;
	public BufferedImage image;
	public boolean isAddingPlayers = false, loadEntities = false;
	
	public Level(String imagePath) {
		if (imagePath != null) {
			this.imagePath = imagePath;
			this.loadLevelFromFile();
		} else {
			this.width = 64;
			this.height = 64;
			tiles = new byte[width * height];
			this.generateLevel();
		}
		
	}
	
	public void generateLevel() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int r = (int) (Math.random() * 6);
				if (r == 1) { 
					tiles[x + y * width] = Tile.STONE.getId();
				} else {
					tiles[x + y * width] = Tile.GRASS.getId();
				}
			}
		}
	}
	
	public int getIdFromServer(int id) {
		return id;
	}
	
	private void loadLevelFromFile() {
		this.image = ImageLoader.loadImage(this.imagePath);
		this.width = image.getWidth();
		this.height = image.getHeight();
		tiles = new byte[width * height];
		this.loadTiles();
	}
	
	public synchronized List<Entity> getEntities() {
		return entities;
	}
	
	public void tick() {
		if (Game.game.state == State.game) {
			for (Entity e : getEntities()) {
					e.tick();
			}
		}
		
		for (Tile t : Tile.tiles) {
			if (t == null) {break;} else t.tick();
		}
	}
	
	private void loadTiles() {
		int[] tileColors = this.image.getRGB(0, 0, width, height, null, 0, width);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tileCheck : for (Tile t : Tile.tiles) {
					if (t != null && t.getColor() == tileColors[x + y * width]) {
						this.tiles[x + y * width] = t.getId();
						break tileCheck;
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void saveLevelToFile() {
		try {
			ImageIO.write(image, "png", new File(Level.class.getResource(this.imagePath).getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void alterTile(int x, int y, Tile newTile) {
		this.tiles[x + y * width] = newTile.getId();
		image.setRGB(x, y, newTile.getColor());
	}
	
	public void renderTiles(Screen screen, int xOffset, int yOffset) {
		if (xOffset < 0) xOffset = 0;
		if (xOffset > ((width << 3) - screen.width)) xOffset = (width << 3) - screen.width;
		if (yOffset < 0) yOffset = 0;
		if (yOffset > ((height << 3) - screen.height)) yOffset = (height << 3) - screen.height;
		
		screen.setOffset(xOffset, yOffset);
		
		
		for (int y = (yOffset >> 3); y < (yOffset + screen.height >> 3) + 1 ; y++) {
			for (int x = (xOffset >> 3); x < (xOffset + screen.width >> 3) + 1; x++) {
				getTile(x, y).render(screen, this, x << 3, y << 3);
			}
		}
		
		
	}
	
	public void renderEntities(Screen screen) {
		if (Game.game.state == State.game) {
			for (Entity e : getEntities()) {
				e.render(screen);
			}
		}
	}

	public Tile getTile(int x, int y) {
		if (0 > x || x >= width || 0 > y || y >= height) return Tile.VOID;
		return Tile.tiles[tiles[x + y * width]];
	}
	
	public int getTileInt(int x, int y) {
		if (0 > x || x >= width || 0 > y || y >= height) return -1;
		return tiles[x + y * width];
	}
	
	public void addEntity(Entity entity) {
		if (entity instanceof PlayerMP) {
			((PlayerMP) entity).setId(entities.size());
		}
		
		
		
		entities.add(entity);
		
		if (entities.size() == 1) {
			loadEntities = true;
		}
		
	}
	
	public int getIdNewPlayer() {
		return id++;
	}

	public void removePlayerMP(String username) {
		int index = 0;
        for (Entity e : getEntities()) {
            if (e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username)) {
                break;
            }
            index++;
        }
        this.getEntities().remove(index);
	}
	private int getPlayerMPIndex(String username) {
		int index = 0;
		for (Entity e : getEntities()) { 
			if (e instanceof PlayerMP && ((PlayerMP) e).getUsername().equalsIgnoreCase(username)) {
				break;
			}
			index++;
		}
		
		return index;
	}
	
	public void movePlayer(String username, int x, int y, int numSteps, boolean isMoving, int movingDir) {
		int index = getPlayerMPIndex(username);
		PlayerMP p = (PlayerMP) getEntities().get(index);
		p.x = x;
		p.y = y;
		p.setMoving(isMoving);
		p.setNumSteps(numSteps);
		p.setMovingDir(movingDir);
	}

	public byte[] getTiles() {
		return tiles;
	}

	
	
}
