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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

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
	private long Last_ID = -100;
	
	private final float START_POSITION_X = 0;
	private final float START_POSITION_Y = 0;
	
	private float Position_x = 0;
	private float Position_y = 0;

	
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
		
		AddNewPlayer();
		getPlayers();
		
		Packet packet = new Packet();
		Packet ReceivePacket = new Packet();
		int Packet_Size = 0;
		packet.Type = Action_Type.NEW_PLAYER;
		packet.ID = this.id;
		
		SendAndConfirm(packet);
		
		
//		packet.Type = Action_Type.CLIENT_READ_MODE;
//		packet.ID = -9;
//
//		SendPacket(packet);
//		
		
		boolean Exit = false;		
		while(!Exit){
			
			Sleep(20);
			
			if(queue.size() > 0){
				System.out.println(Thread.currentThread().getName() + " Queue-size: " + queue.size());
				packet.Type = Action_Type.CLIENT_READ_MODE;
				packet.ID = -5;
				
				SendPacket(packet);
	
				Iterator<Packet> iter = queue.iterator();
				while(iter.hasNext()){
					packet = iter.next();
					SendPacket(packet);
					iter.remove();
				}

				
			}
			packet.Type = Action_Type.END;
			packet.ID = -2;
			
			SendPacket(packet);
			packet.Type = Action_Type.CLIENT_SEND_MODE;
			packet.ID = -1;
			SendPacket(packet);
			setSoTime(200);
			Packet_Size = 0;
			while(true){
				//System.out.println(Thread.currentThread().getName() + " Ilosc graczy: " + Player_List.size());
				
				ReceivePacket = GetPacket();
				if(ReceivePacket == null) break;
				if(ReceivePacket.Type == Action_Type.END) break;
				
				System.out.println(ReceivePacket.ID + " - " + ReceivePacket.Type);
				
				System.out.println(ReceivePacket.ID + " - " + ReceivePacket.Type);
				if(ReceivePacket != null && ReceivePacket.Type == Action_Type.PLAYER_UPDATE) {
					Packet_Size++;
					
					List list = Collections.synchronizedList(Player_List);
					synchronized (list) {
						
						Iterator<Player_Socket> iter = list.iterator();
						while(iter.hasNext()){
							Player_Socket playerlist = iter.next();
							if(iter.hasNext()){
								if(playerlist.id == ReceivePacket.ID) iter.next();
							}
							else break;
							
							playerlist.queue.offer(ReceivePacket);
						}
					}
					
					
				}
			}
		//	System.out.println(queue.size());
			//Sleep(5);
		}
		
	}
	
	
	
	private Packet SendAndConfirm(Packet packet){
		setSoTime(5);
		Packet ReceivePacket = new Packet();
		
		while(true){
			SendPacket(packet);
			ReceivePacket = GetPacket();
			if(ReceivePacket != null) break;
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
	
	private void AddNewPlayer(){
		
		List list = Collections.synchronizedList(Player_List);
		
		synchronized (list) {
			Iterator<Player_Socket> iter = list.iterator();
			while(iter.hasNext()){
				Player_Socket player_Socket = iter.next();
				if(player_Socket.id == this.id) continue;
				
				Packet packet = new Packet();
				packet.Type = Action_Type.PLAYER_UPDATE;
				packet.x = START_POSITION_X + 1 *id;
				packet.y = START_POSITION_Y;
				packet.ID = this.id;
				player_Socket.queue.add(packet);
			}
		}
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
	
	
	private void getPlayers(){
		
		List list = Collections.synchronizedList(Player_List);
		
		synchronized (list) {
			Iterator<Player_Socket> iter = list.iterator();
			while(iter.hasNext()){
				Player_Socket player_Socket = iter.next();
				if(player_Socket.id == this.id) continue;
				
				Packet PlayerPacket = new Packet();
				PlayerPacket.Type = Action_Type.PLAYER_UPDATE;
				PlayerPacket.x = player_Socket.Position_x;
				PlayerPacket.y = player_Socket.Position_y;
				PlayerPacket.ID = player_Socket.id;
				this.queue.add(PlayerPacket);
			}
		}
	}
	
	public int getID() { return this.id; }
	public void setPlayerList(ArrayList<Player_Socket> PlayerList) { this.Player_List = PlayerList; }
}
