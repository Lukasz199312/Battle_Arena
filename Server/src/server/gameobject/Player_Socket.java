package server.gameobject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import map.Map;
import map.MapObjects;

import com.sun.xml.internal.ws.encoding.HasEncoding;

import packets.Action_Type;
import packets.Packet;

public class Player_Socket extends Thread{
	
	private int id;
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private ObjectInputStream getData;
	private ObjectOutputStream sendData;
	private Thread MainThread;
	private ArrayList<Player_Socket> Player_List = new ArrayList<>();
	private ConcurrentLinkedQueue<Packet> queue = new ConcurrentLinkedQueue<Packet>();
	private boolean Exit = false;
	private int PacketSend = 0;
	private long time;
	
	public Player_Socket(Socket socket, int id, Thread MainThread) { 
		this.socket = socket;
		try {
			socket.setSoTimeout(25);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.id = id;
		this.MainThread = MainThread;
		
		try {
			getData = new ObjectInputStream(socket.getInputStream());
			sendData = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		
//		AddNewPlayer();
//		getPlayers();
		
		try {
			socket.setTcpNoDelay(true);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Packet packet = new Packet();
		Packet ReceivePacket = new Packet();
		int Packet_Size = 0;
			
		

		
		for(int i = 0 ; i < Map.getProperties().getX() ; i++){
			for(int j = 0; j < Map.getProperties().getY(); j ++){
				packet = new Packet();
				packet.MapObjects = Map.getBackground()[i][j];
				packet.Type = Action_Type.GRASS_0;
				packet.x = i;
				packet.y = j;
				SendPacket(packet);
			}
		}
		
		packet = new Packet();
		packet.Type = Action_Type.NEW_PLAYER;
		packet.x = 200;
		packet.y = 200;
		packet.ID = this.id;
		
		SendPacket(packet);

		
		System.out.println("quee: " + queue.size());
		setSoTime(0);
		while(!Exit){
			//System.out.println(Thread.currentThread().getName() + " Player List: " + Player_List.size());
			
			
			if(queue.size() > 0){
				packet.Type = Action_Type.CLIENT_READ_MODE;
				SendPacket(packet);
				int i = 0;
				while(true){
					packet = new Packet();
					packet = queue.poll();
					if(packet == null) break;
					SendPacket(packet);
					PacketSend++;
//					System.out.println(i);
//					i++;
				}
				
				packet = new Packet();
				packet.Type = Action_Type.END;
				SendPacket(packet);
				
			}
			
			

			
			packet = new Packet();
			packet.Type = Action_Type.CLIENT_SEND_MODE;
			SendPacket(packet);
			boolean isEmpty = true;
			while(true){
				ReceivePacket = GetPacket();
				
				if(ReceivePacket == null) break;
				if(ReceivePacket.Type == Action_Type.END) break;
				
				isEmpty = false;
				List<Player_Socket> list = Collections.synchronizedList(Player_List);
				
				synchronized (list) {
					Iterator<Player_Socket> iter = list.iterator();
					while(iter.hasNext()){
						Player_Socket player_Socket = iter.next();
						if(player_Socket.id != this.getID()){
							player_Socket.queue.add(ReceivePacket);
						}
						
						
					}
				}
				
			}
			
			//if(isEmpty == true) Sleep(1);
			
			if(time < System.currentTimeMillis()){
				System.out.println(PacketSend);
				PacketSend = 0;
				time = System.currentTimeMillis() + 1000;
			}
			

		}
		
	}
	
	
	

	private boolean SendPacket(Packet packet){
		try {
			sendData.writeObject(packet);
			sendData.reset();
			sendData.flush();
			return true;
		} catch (SocketException e) { 
			List<Player_Socket> list = Collections.synchronizedList(Player_List);
			
			synchronized (list) {
				Iterator<Player_Socket> iter = list.iterator();
				while(iter.hasNext()){
					Player_Socket player_Socket = iter.next();
					Packet RemovePacket = new Packet();
					RemovePacket.Type = Action_Type.REMOVE_PLAYER;
					RemovePacket.ID = this.id;
					player_Socket.queue.add(RemovePacket);
					if(player_Socket.getID() == this.getID()){
						iter.remove();
					}
				}
			
				Exit = true;
				return false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private Packet GetPacket(){
		try {
			Object object = getData.readObject();
			if(object instanceof Packet){
				
				//if( ((Packet)object).ID == Last_ID ) return null;
				
				return (Packet)object;
			} 
			else {
				return null;
			}
		} catch (SocketTimeoutException e) {
			Packet packet = new Packet();
			packet.Type = Action_Type.END;
			return packet;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Packet packet = new Packet();
			packet.Type = Action_Type.END;
			return packet;
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
	

	public int getID() { return this.id; }
	public void setPlayerList(ArrayList<Player_Socket> PlayerList) { this.Player_List = PlayerList; }
	public ConcurrentLinkedQueue<Packet> getPacketQueue(){ return this.queue; }
}
