package us.schmee.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import us.schmee.game.Game;
import us.schmee.game.entites.mobs.PlayerMP;
import us.schmee.game.net.packets.Packet;
import us.schmee.game.net.packets.Packet.PacketTypes;
import us.schmee.game.net.packets.Packet00Login;
import us.schmee.game.net.packets.Packet01Disconnect;
import us.schmee.game.net.packets.Packet02Move;

public class GameServer extends Thread {
	
	private DatagramSocket socket;
	private Game game;
	private int serverPort = 1331;
	private List<PlayerMP> players = new ArrayList<PlayerMP>();
	
	public GameServer(Game game) {
		this.game = game;
		try {
			this.socket = new DatagramSocket(serverPort); // TODO: Add Port inside paranthesis
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
			
//			String msg = new String(packet.getData());
//			if (msg.trim().equalsIgnoreCase("ping")) sendData("pong".getBytes(), packet.getAddress(), packet.getPort());;
//			
//			System.out.println("CLIENT > " + msg); 
			
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String msg = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(msg.substring(0, 2));
		Packet packet = null;
		switch (type) {
			default:
			case INVALID:
				break;
				
			case LOGIN:
				packet = new Packet00Login(data);
				System.out.println("[" + address.getHostAddress() + " : " + port + "] " + ((Packet00Login)packet).getUsername() + " has connected.");
				PlayerMP player = new PlayerMP(game.level, 100, 100, ((Packet00Login)packet).getUsername(), address, port, ((Packet00Login) packet).getId());
				player.setColor(((Packet00Login) packet).getColor());
				this.addConnection(player, ((Packet00Login)packet));
				break;
				
			case MOVE:
				packet = new Packet02Move(data);
				this.handleMove((Packet02Move)packet);
				break;
				
			case DISCONNECT:
				packet = new Packet01Disconnect(data);
				System.out.println("[" + address.getHostAddress() + " : " + port + "] " + ((Packet01Disconnect)packet).getUsername() + " has disconnected.");				this.removeConnection((Packet01Disconnect)packet);
				break;
		}
	}

	

	public void addConnection(PlayerMP player, Packet00Login packet) {
		boolean alreadyConnected = false;
		
		for (PlayerMP p : this.players) {
			
			if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
				if (p.ipAddress == null) {
					p.ipAddress = player.ipAddress;
				}
				
				if (p.port == -1) {
					p.port = player.port;
				}
				
				alreadyConnected = true;
			} else {
				sendDataToAllClients(packet.getData());
				
				for (PlayerMP pm : this.players) {
					packet = new Packet00Login(pm.getUsername(), pm.x, pm.y, pm.getColor(), pm.getId());
					sendData(packet.getData(), player.ipAddress, player.port);
				}
				break;
			}
		}
		
		if (!alreadyConnected) {
			this.players.add(player);
		}
		
	}
	
	public void removeConnection(Packet01Disconnect packet) {
		this.players.remove(getPlayerMPIndex(packet.getUsername()));
		sendDataToAllClients(packet.getData());
	}
	
	public PlayerMP getPlayerMP(String username) {
		for (PlayerMP p : this.players) {
			if (p.getUsername().equalsIgnoreCase(username)) {
				return p;
			}
		}
		return null;
	}
	
	public int getPlayerMPIndex(String username) {
		int index = 0;
		for (PlayerMP p : this.players) {
			if (p.getUsername().equalsIgnoreCase(username)) {
				break;
			}
			index++;
		}
		return index;
	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		for (PlayerMP p : players) {
			sendData(data, p.ipAddress, p.port);
		}
	}
	
	private void handleMove(Packet02Move packet) {
		if (getPlayerMP(packet.getUsername()) != null) {
			int index = getPlayerMPIndex(packet.getUsername());
			PlayerMP p = this.players.get(index);
			p.x = packet.getX();
			p.y = packet.getY();
			p.setMoving(packet.isMoving());
			p.setMovingDir(packet.getMovingDir());
			p.setNumSteps(packet.getNumSteps());
			packet.writeData(this);
		}
	}
	
}
