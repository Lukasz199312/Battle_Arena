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
			
			
			try {
				
				Object obj = getData.readObject();
				
				if(obj instanceof Packet){
					Packet packet;
					packet = ((Packet)obj);
					
					
					
					System.out.println("Pozycja Obiektu ID: " + packet.ID + "  " + packet.x + " : " + packet.y);
				}
				
				
			} catch (ClassNotFoundException | IOException e) {
				if(MainThread.isAlive() == false ) Exit = true;
			} 
			
		
			try {
				
			    Iterator<Player_Socket> iter = Player_List.iterator();
			    
			    while(iter.hasNext()){
					Packet packet = new Packet();
					packet.ID = iter.next().id;
					setData.writeObject(packet);	//Wywlasczenie ?!
			    }
			    			
				
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
				setData.writeObject(this.id);
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
