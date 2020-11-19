package us.schmee.game.net.packets;

import us.schmee.game.net.GameClient;
import us.schmee.game.net.GameServer;

public abstract class Packet {
	
	public static enum PacketTypes {
		INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02);  
		
		private int packedId;
		private PacketTypes(int packetId) {
			this.packedId = packetId;
		}
		
		public int getId() {
			return packedId;
		}
	}
	
	public byte packetId;
	
	public Packet(int packetId) {
		this.packetId = (byte) packetId;
	}
	
	public abstract void writeData(GameClient client);

	public abstract void writeData(GameServer server);
	
	public abstract byte[] getData();
	
	public String readData(byte[] data) {
		String msg = new String(data).trim();
		return msg.substring(2);
	}
	
	public static PacketTypes lookupPacket(int id) {
		for (PacketTypes p : PacketTypes.values()) {
			if (p.getId() == id) {
				return p;
			}
		}
		return PacketTypes.INVALID;
	}

	public static PacketTypes lookupPacket(String packetId) {
		try {
			return lookupPacket(Integer.parseInt(packetId));
		} catch (NumberFormatException e) {
			 e.printStackTrace();
		}
		return null;
	}
	
}
