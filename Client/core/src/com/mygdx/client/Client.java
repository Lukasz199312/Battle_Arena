package com.mygdx.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import packets.Action_Type;
import packets.Packet;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.GameLogic;
import com.mygdx.game.GameObjectType;
import com.sun.org.apache.regexp.internal.recompile;

public class Client extends Thread{
	private Socket socket;
	private ObjectOutputStream sendData;
	private ObjectInputStream getData;
	private BlockingQueue<Packet> PacketQueue = new ArrayBlockingQueue<Packet>(3000);
	private BlockingQueue<Packet> PacketQueueUpdate = new ArrayBlockingQueue<Packet>(3000);
	private long Last_ID = -100;
	
	@Override
	public void run() {
		InitConnection();	
		long time;
		int packet_number;
		
		Packet packet = new Packet();
		Packet ReceivePacket = new Packet();
		
		packet.ID = - 10;
		ReceivePacket = SendAndConfirm(packet);
		RegisterNewPlayer(ReceivePacket);
		
		//System.out.println("Client Packet: " + ReceivePacket.Type);
		
		boolean Exit = false;
		System.out.println("START CLIENT");
		while(!Exit){
			System.out.println("In while");
			Sleep(20);
			packet_number = 0;
			time = System.currentTimeMillis();
			setSoTime(0);
			ReceivePacket = new Packet();
			ReceivePacket = GetPacket();
			
			if(ReceivePacket != null)
			switch(ReceivePacket.Type){
			case CLIENT_READ_MODE:
				//System.out.println("Client Packet: " + ReceivePacket.Type);
				setSoTime(999);
				while(true){
					ReceivePacket = GetPacket();
					
					if(ReceivePacket == null) break;
					
					if( ReceivePacket.Type == Action_Type.END) break;
					
					if(ReceivePacket.Type == Action_Type.PLAYER_UPDATE){
						packet_number++;
						System.out.println("Packet : " + ReceivePacket.Type );
						PacketQueue.offer(ReceivePacket);
					}
				}
				//System.out.println("Packet Size: " + packet_number );
				//System.out.println("Client END READ");
				break;
				
			case CLIENT_SEND_MODE:
			//	System.out.println("Client Packet: " + ReceivePacket.Type);
				
				Iterator<Packet> iter = PacketQueueUpdate.iterator();
				while(iter.hasNext()){
					packet = iter.next();
					SendPacket(packet);
					iter.remove();
					
				}
				
				//System.out.println("END");
				
				packet.Type = Action_Type.END;
				packet.ID = -1;
				SendPacket(packet);
				//System.out.println("Client END SEND");
				break;
				
			case GAME_EXIT:
				Exit = true;
				break;
				
				default:
					//System.out.println("SYSTEM ERROR SOCKET");
					break;
			}
			
			
			//System.out.println("PING: " + (System.currentTimeMillis() - time) );
		//	Sleep(5);
		}
	
		
		System.out.println("END CLIENT");
	}
	
	public void InitConnection(){
		try {
			socket = new Socket("192.168.0.101", 6002);
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
		Packet ReceivePacket;
		try {
			ReceivePacket = (Packet)getData.readObject();
			PacketQueue.put(ReceivePacket);
			
			long time = System.currentTimeMillis() + 500;
			
			while(true){
				Packet packet = new Packet();
				packet.Type = Action_Type.OPERATION_COMPLETE;
				
				try {
					sendData.writeObject(packet);
				} catch (IOException e){
					e.printStackTrace();
				}
				if(time < System.currentTimeMillis()) break;
				Thread.currentThread().sleep(10);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void RegisterNewPlayer(Packet packet){
		try {
			PacketQueue.put(packet);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private Packet SendAndConfirm(Packet packet){
		setSoTime(5);
		Packet ReceivePacket = new Packet();
		
		while(true){
			SendPacket(packet);
			ReceivePacket = GetPacket();
			if(ReceivePacket != null && ReceivePacket.Type != Action_Type.END) break;
		}
		
		return ReceivePacket;
	}
	
	private boolean SendPacket(Packet packet){
		try {
			sendData.writeObject(packet);
			sendData.reset();
			sendData.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} 
	}

	private Packet GetPacket(){
		try {
			Object object = getData.readObject();
			if(object instanceof Packet){
				//System.out.println("otrzymany pakiet: "+ ((Packet)object).Type);
				
				if( ((Packet)object).ID == Last_ID ) return null;
				
				Last_ID = ((Packet)object).ID;
				return (Packet)object;
			} 
			else {
				return null;
			}
		} catch (SocketTimeoutException e) {
			Packet packet = new Packet();
			packet.Type = Action_Type.END;
			System.out.println("END ");
			return packet;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
	
	private boolean Sleep(int time){
		try {
			Thread.currentThread().sleep(time);
			return true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean setSoTime(int time){
		try {
			socket.setSoTimeout(time);
			return true;
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	public void setPacketQueue(BlockingQueue<Packet> queue, BlockingQueue<Packet> queueUpdate){
		this.PacketQueue = queue;
		this.PacketQueueUpdate = queueUpdate;
	}
	
	public BlockingQueue<Packet> getPacketQueue(){
		return this.PacketQueue;
	}
	
	public BlockingQueue<Packet> getPacketQueueUpdate(){
		return this.PacketQueueUpdate;
	}

}
