package com.mygdx.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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

		while(true){
			//Gdx.app.log("STILL: ", "LIFE");
			try {
				Object obj = getData.readObject();
				if(obj instanceof Packet){
					Packet packet = (Packet)obj;
						//Gdx.app.log("Client: ", "Odebralem pakiet");
						//while(true) {
							//Gdx.app.log("Client: ", "IN QUEUE");
							PacketQueue.offer(packet);
							//Gdx.app.log("Client: ", "Packet Dodany do listy");
						//}
					
				}
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		//
		//Gdx.app.log("client", "threadend");
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
			Object obj = getData.readObject();
			int ID = (Integer) obj;
			System.out.println(Integer.toString(ID));
			
			Packet packet = new Packet();
			packet.ID = ID;
			packet.Type = Action_Type.CONNECT_ME;
			
			try {
				PacketQueue.put(packet);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			while(true){
//				if(PacketQueue.offer(packet)) break;
//			}
			
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
