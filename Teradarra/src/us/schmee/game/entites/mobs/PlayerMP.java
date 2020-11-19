package us.schmee.game.entites.mobs;

import java.net.InetAddress;

import us.schmee.game.gfx.Colors;
import us.schmee.game.handlers.input.InputHandler;
import us.schmee.game.level.Level;

public class PlayerMP extends Player {

	public InetAddress ipAddress;
	public int port;
	public int id;
	
	public PlayerMP(Level level, int x, int y, InputHandler input, String username, InetAddress ipAddress, int port, int id) {
		super(level, x, y, input, username, id);
		this.ipAddress = ipAddress;
		this.port = port;
		this.id = id;
	}
	
	public PlayerMP(Level level, int x, int y, String username, InetAddress ipAddress, int port, int id) {
		super(level, x, y, null, username, id);
		this.ipAddress = ipAddress;
		this.port = port;
		this.id = id;
	}
	
	@Override
	public void tick() {
		super.tick();
	}

}
