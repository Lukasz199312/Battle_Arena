package com.mygdx.client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import packets.Packet;

public class NetworkController {
	private Client client;
	private BlockingQueue<Packet> PacketQueue = new ArrayBlockingQueue<Packet>(1);
	
	public NetworkController() {
		client = new Client();
		client.setPacketQueue(PacketQueue);
		new Thread(client).start();
		
	}
	
	public Packet getPacket(){
		return PacketQueue.poll();
	}
	
	public Client getClient(){
		return client;
	}
	
	public BlockingQueue<Packet> getPacketQueue(){
		return PacketQueue;
	}
	
}
