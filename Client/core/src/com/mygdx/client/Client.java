package com.mygdx.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import packets.Action_Type;
import packets.Packet;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.GameLogic;
import com.mygdx.game.GameObjectType;

public class Client extends Thread{
	private Socket socket;
	private ObjectOutputStream sendData;
	private ObjectInputStream getData;
	private BlockingQueue<Packet> PacketQueue = new ArrayBlockingQueue<Packet>(1);
	
	
	@Override
	public void run() {
		InitConnection();
		Register_me();
		
		

	}
	
	public void InitConnection(){
		try {
			socket = new Socket("127.0.1", 82);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Poprawne polaczenie z hostem");
		
		try {
			sendData = new ObjectOutputStream(socket.getOutputStream());
			getData = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void Register_me(){
		try {
			Gdx.app.log("REGISTER", "NEW PLAYER");
			Object obj = getData.readObject();
			Packet packet = (Packet)obj;
			
			try {
				PacketQueue.put(packet);
				packet = new Packet();
				packet.Type = Action_Type.OPERATION_COMPLETE;
				
				sendData.writeObject(packet);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setPacketQueue(BlockingQueue<Packet> queue){
		this.PacketQueue = queue;
	}
	
	public BlockingQueue<Packet> getPacketQueue(){
		return this.PacketQueue;
	}

}
