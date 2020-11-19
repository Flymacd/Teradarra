package us.schmee.game.level.tiles;

import us.schmee.game.gfx.Screen;
import us.schmee.game.level.Level;

public class BasicTile extends Tile {

	protected int tileId;
	protected int tileColor;
	protected int x, y;
	
	public BasicTile(int id, int x, int y, int tileColor, int color) {
		super(id, false, false, color);
		tileId = x + y * 32;
		this.x = x;
		this.y = y;
		this.tileColor = tileColor;
	}

	public void render(Screen screen, Level level, int x, int y) {
		screen.render(x, y, tileId, tileColor, 0x00, 1);
	}
	
	public void tick() {
		
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	

}
