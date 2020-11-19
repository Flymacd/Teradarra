package us.schmee.game.net.packets;

import us.schmee.game.net.GameClient;
import us.schmee.game.net.GameServer;

public class Packet02Move extends Packet {

	private String username;
	private int x, y;
	
	private int numSteps = 0, movingDir = 1, id;
	private boolean isMoving;
	
	public Packet02Move(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);
		this.numSteps = Integer.parseInt(dataArray[3]);
		this.isMoving = Integer.parseInt(dataArray[4]) == 1;
		this.movingDir = Integer.parseInt(dataArray[5]);
		this.id = Integer.parseInt(dataArray[6]);
	}
	
	public Packet02Move(String username, int x, int y, int numSteps, boolean isMoving, int movingDir, int id) {
		super(02);
		this.username = username;
		this.x = x;
		this.y = y;
		this.numSteps = numSteps;
		this.isMoving = isMoving;
		this.movingDir = movingDir;
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
		return ("02" + this.username + "," + this.x + "," + this.y + "," + this.numSteps + "," + (isMoving? 1 : 0) + "," + this.movingDir + "," + this.id).getBytes();
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

	public int getNumSteps() {
		return numSteps;
	}

	public int getMovingDir() {
		return movingDir;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
