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
import com.sun.glass.ui.Application;
import com.sun.org.apache.regexp.internal.recompile;

public class Client extends Thread{
	private Socket socket;
	private ObjectOutputStream sendData;
	private ObjectInputStream getData;
	private BlockingQueue<Packet> PacketQueue = new ArrayBlockingQueue<Packet>(100);
	private BlockingQueue<Packet> PacketQueueUpdate = new ArrayBlockingQueue<Packet>(100);
	private boolean Exit = false;
	private long MS;
	private long MAX_MS = 0;
	private long time;
	private long RESET_TIME;
	
	@Override
	public void run() {
		InitConnection();	
		int packet_number;
		Packet packet = new Packet();
		Packet ReceivePacket = new Packet();
		try {
			socket.setTcpNoDelay(true);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		packet.ID = -10;
		setSoTime(0);
//		while(true){
//			ReceivePacket = SendAndConfirm(packet);
//			if(ReceivePacket != null) break;
//		}
		
		ReceivePacket = GetPacket();
		RegisterNewPlayer(ReceivePacket);
		System.out.println("ID: " + ReceivePacket.ID);
		
		int ReceivenNumberPacket = 0;
		RESET_TIME = System.currentTimeMillis();
		while(!Exit){
			time = System.currentTimeMillis();
			
			
			Sleep(10);
			ReceivePacket = new Packet();
			ReceivePacket = GetPacket();
			
			switch(ReceivePacket.Type){
			case CLIENT_READ_MODE:
				while(true){
					ReceivePacket = new Packet();
					ReceivePacket = GetPacket();
					if(ReceivePacket != null){
						//System.out.println("Packet " + ReceivePacket.Type);
						if(ReceivePacket.Type == Action_Type.END) break;
						try {
							PacketQueue.put(ReceivePacket);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
				break;
				
			case CLIENT_SEND_MODE:
				
				if(PacketQueueUpdate.size() > 0){
					while(true){
						packet = new Packet();
						packet = PacketQueueUpdate.poll();
						if(packet == null) break;
						SendPacket(packet);
					}
				}
				
				packet = new Packet();
				packet.Type = Action_Type.END;
				SendPacket(packet);
				break;
			}
			
			
			MS = System.currentTimeMillis() - time;
			if(MS > MAX_MS)  MAX_MS = MS;
			if(RESET_TIME + 1000 <= System.currentTimeMillis()){
				RESET_TIME = System.currentTimeMillis();
				MAX_MS = MS;
			}
		}
		
		
		//System.out.println("END CLIENT");
	}
	
	public void InitConnection(){
		try {
			socket = new Socket("127.0.0.1", 6002);
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
			sendData.reset();
			sendData.writeObject(packet);
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
				return (Packet)object;
			} 
			else {
				return null;
			}
		} catch (SocketTimeoutException e) {
			Packet packet = new Packet();
			packet.Type = Action_Type.END;
			System.out.println("Time Out ");
			return packet;
		} catch (SocketException e) {
			System.exit(0);
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
	
	public long getMS(){
		return this.MAX_MS;
	}

}
