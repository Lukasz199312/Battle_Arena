package server.gameobject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

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
	
	public Player_Socket(Socket socket, int id, Thread MainThread) { 
		this.socket = socket;
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
		boolean Exit = false;
		
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
			
			
			try {
				//System.out.println("SWysylanie");
				Packet packet = new Packet();
				packet.Type = Action_Type.START_UPDATE;
				setData.writeObject(packet);
//		    	try {
//					Thread.currentThread().sleep(100);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			    Iterator<Player_Socket> iter = Player_List.iterator();
			    while(iter.hasNext()){

					packet = new Packet();
					packet.ID = iter.next().id;
					packet.Type = Action_Type.PLAYER_INFORMATION_UPDATE;
				//	System.out.println("Wysylanie player list");
					setData.writeObject(packet);
					//System.out.println("Wysylanie player list DONE");
			    }
			    
				packet = new Packet();
				packet.Type = Action_Type.END_UPDATE;
				setData.writeObject(packet);

				
			} catch (IOException e) {
				Exit = true;
				e.printStackTrace();
			}
			
		}
		
		System.out.println(Thread.currentThread().getName() +" ID: " + id);
		super.run();
	}
	
	public void RegisterMe(){
		boolean Exit = false;
		
		while(!Exit){
			try {
				System.out.println("ID: " + (id + 1));
				Packet packet = new Packet();
				packet.ID = this.id;
				packet.Type = Action_Type.CONNECT_ME;
				setData.writeObject(packet);
				Exit = true;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public int getID() { return this.id; }
	public void setPlayerList(ArrayList<Player_Socket> PlayerList) { this.Player_List = PlayerList; }
}
