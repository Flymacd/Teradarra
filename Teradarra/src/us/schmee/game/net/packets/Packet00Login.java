package us.schmee.game.net.packets;

import us.schmee.game.net.GameClient;
import us.schmee.game.net.GameServer;

public class Packet00Login extends Packet {

	private String username;
	private int x, y, color, id;
	
	public Packet00Login(byte[] data) {
		super(00);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);
		this.color = Integer.parseInt(dataArray[3]);
	}
	
	public Packet00Login(String username, int x, int y, int color, int id) {
		super(00);
		this.username = username;
		this.x = x;
		this.y = y;
		this.color = color;
		this.id = id;
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("00" + this.username + "," + getX() + "," + getY() + "," + getColor()).getBytes();
	}
	
	public String getUsername() {
		return this.username;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getColor() {
		return color;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
