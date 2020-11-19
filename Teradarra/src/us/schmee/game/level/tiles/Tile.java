package us.schmee.game.level.tiles;

import us.schmee.game.gfx.Colors;
import us.schmee.game.gfx.Screen;
import us.schmee.game.level.Level;

public abstract class Tile {

	public static Tile[] tiles = new Tile[256];
	public static final Tile 
			VOID = new BasicSolidTile(0, 0, 0, Colors.get(000, 000, 000, 000), 0xff000000), 
			STONE = new BasicSolidTile(1, 1, 0, Colors.get(-1, 333, -1, -1), 0xff555555), 
			GRASS = new BasicTile(2, 2, 0, Colors.get(-1, 131, 141, 510), 0xff00ff00),
			WATER = new AnimatedTile(3, new int[][] {{0, 5}, {1, 5}, {2, 5}, {1, 5}}, Colors.get(-1, 004, 115, -1), 0xff0000ff, 750),
			ROAD = new BasicTile(4, 3, 0, Colors.get(321, 322, 221, 432), 0xff727272),
			COMBATSTARTLEFT = new BasicTile(101, 0, 11, Colors.get(-1, -1, 333, 222), 0xffe60000),
			COMBATSTARTTOP = new BasicTile(102, 1, 11, Colors.get(232, -1, 333, 222), 0xffe6e600),
			COMBATHORIZONTAL = new BasicTile(103, 2, 11, Colors.get(-1, -1, 333, 222), 0xff00e600),
			COMBATVERTICAL = new BasicTile(104, 3, 11, Colors.get(-1, -1, 333, 222), 0xffe600e6);
	
	protected byte id;
	protected boolean solid, emitter;
	
	private int color;
	
	public Tile(int id, boolean isSolid, boolean isEmitter, int color) {
		this.id = (byte) id;
		if (tiles[id] != null) throw new RuntimeException("Duplicate Tile ID on " + id);
		this.solid = isSolid;
		this.emitter = isEmitter;
		this.color = color;
		tiles[id] = this;
	}
	
	public byte getId() {
		return id;
	}
	
	public boolean isSolid() {
		return solid;
	}

	public boolean isEmitter() {
		return emitter;
	}
	
	public int getColor() {
		return color;
	}

	public abstract void tick();
	
	public abstract void render(Screen screen, Level level, int x, int y);

}
