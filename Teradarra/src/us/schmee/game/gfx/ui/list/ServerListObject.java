package us.schmee.game.gfx.ui.list;

public class ServerListObject {
	
	private int x, y;
	private String name, ip, port;
	
	public ServerListObject(int x, int y, String name, String ip, String port) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.ip = ip;
		this.port = port;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

}
