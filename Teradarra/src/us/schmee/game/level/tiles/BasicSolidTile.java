package us.schmee.game.level.tiles;

public class BasicSolidTile extends BasicTile {

	public BasicSolidTile(int id, int x, int y, int tileColor, int color) {
		super(id, x, y, tileColor, color);
		this.solid = true;
	}

}
