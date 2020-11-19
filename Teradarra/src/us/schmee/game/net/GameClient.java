package us.schmee.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import us.schmee.game.Game;
import us.schmee.game.entites.mobs.PlayerMP;
import us.schmee.game.net.packets.Packet;
import us.schmee.game.net.packets.Packet00Login;
import us.schmee.game.net.packets.Packet01Disconnect;
import us.schmee.game.net.packets.Packet02Move;
import us.schmee.game.net.packets.Packet.PacketTypes;

public class GameClient extends Thread {
	
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Game game;
	
	public GameClient(Game game, String ipAddress) {
		this.game = game;
		try {
			this.socket = new DatagramSocket(); // TODO: Add Port inside paranthesis
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
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
//			System.out.println("SERVER > " + msg);
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
				handleLogin((Packet00Login)packet, address, port);
				break;
				
			case MOVE:
				packet = new Packet02Move(data);
				handleMove((Packet02Move)packet);
				break;
				
			case DISCONNECT:
				packet = new Packet01Disconnect(data);
				System.out.println("[" + address.getHostAddress() + " : " + port + "] " + ((Packet01Disconnect)packet).getUsername() + " has left the game.");
				game.level.removePlayerMP(((Packet01Disconnect)packet).getUsername());
				break;
		}
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleLogin(Packet00Login packet, InetAddress address, int port) {
		PlayerMP player = new PlayerMP(game.level, packet.getX(), packet.getY(), packet.getUsername(), address, port, packet.getId());
		player.setColor(packet.getColor());
		game.level.addEntity(player);
		System.out.println("[" + address.getHostAddress() + " : " + port + "] " + packet.getUsername() + " has joined the game.");
	}
	
	private void handleMove(Packet02Move packet) {
		game.level.movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getNumSteps(), packet.isMoving(), packet.getMovingDir());
	}

}
