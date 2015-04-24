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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Date;

import com.sun.xml.internal.ws.encoding.HasEncoding;

import packets.Action_Type;
import packets.Packet;

public class Player_Socket extends Thread{
	
	private int id;
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private ObjectInputStream getData;
	private ObjectOutputStream setData;
	private Thread MainThread;
	private ArrayList<Player_Socket> Player_List = new ArrayList<>();
	private Queue<Packet> queue = new LinkedList<Packet>();
	private boolean Exit = false;
	
	private final float START_POSITION_X = 0;
	private final float START_POSITION_Y = 0;
	
	public Player_Socket(Socket socket, int id, Thread MainThread) { 
		this.socket = socket;
		try {
			socket.setSoTimeout(100);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.id = id;
		this.MainThread = MainThread;
		
		try {
			getData = new ObjectInputStream(socket.getInputStream());
			setData = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		
		RegisterMe();
		
		
		while(!Exit){
			//System.out.println("SERVER LIFE");
			//System.out.println("Ilosc graczy: " + Player_List.size());
			
//			try {	
//				Object obj = getData.readObject();
//				
//				if(obj instanceof Packet){
//					Packet packet = new Packet();
//					packet = ((Packet)obj);
//					
//				}
//				
//			} catch (ClassNotFoundException | IOException e) {
//				if(MainThread.isAlive() == false ) Exit = true;
//			} 
			
			
//			try {
//				//System.out.println("SWysylanie");
//				Packet packet = new Packet();
//				packet.Type = Action_Type.START_UPDATE;
//				setData.writeObject(packet);
//			    Iterator<Player_Socket> iter = Player_List.iterator();
//			    while(iter.hasNext()){
//
//					packet = new Packet();
//					packet.ID = iter.next().id;
//					packet.Type = Action_Type.PLAYER_INFORMATION_UPDATE;
//					setData.writeObject(packet);
//					
//			    }
//			    
//				packet = new Packet();
//				packet.Type = Action_Type.END_UPDATE;
//				setData.writeObject(packet);
//
				System.out.println("Wysylanie player list DONE" + new Date().getTime());
		    	try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				
//			} catch (IOException e) {
//				Exit = true;
//				e.printStackTrace();
//			}
			
		}
		
		System.out.println(Thread.currentThread().getName() +" ID: " + id);
		super.run();
	}
	
	@SuppressWarnings("deprecation")
	public void RegisterMe(){
		
		Packet packet = new Packet();
		packet.Type = Action_Type.NEW_PLAYER;
		
		packet.x = START_POSITION_X;
		packet.y = START_POSITION_Y;

		long time;
		time = System.currentTimeMillis() + 1000;
		while(true){
			 try {
				 if(time < System.currentTimeMillis()){
					 System.out.println("Odczyt obiektu");
					 Object obj = getData.readObject();
					 break;
					 
					 
				 }
				 
			setData.writeObject(packet);
			currentThread().sleep(100);
			System.out.println("Nawiazywanie polaczenia");
				
				
			} catch (SocketTimeoutException e){
				System.out.println("TIME OUT");
				time = System.currentTimeMillis() + 1000;
			} catch (IOException e) {
				Exit = true;
				e.printStackTrace();
				break;
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	public int getID() { return this.id; }
	public void setPlayerList(ArrayList<Player_Socket> PlayerList) { this.Player_List = PlayerList; }
}
