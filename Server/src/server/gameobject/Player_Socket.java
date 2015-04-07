package server.gameobject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import packets.Packet;

public class Player_Socket extends Thread{
	
	private int id;
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private ObjectInputStream getData;
	private Thread MainThread;
	
	public Player_Socket(Socket socket, int id, Thread MainThread) { 
		this.socket = socket;
		this.id = id;
		this.MainThread = MainThread;
		
		try {
			getData = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		
		boolean Exit = false;
		
		while(!Exit){
			
			Packet packet;
			try {
				packet = (Packet) getData.readObject();
				System.out.println("Pozycja Obiektu: " + packet.x + " : " + packet.y);
				
			} catch (ClassNotFoundException | IOException e) {
				if(MainThread.isAlive() == false ) Exit = true;
			} 
			
			
		}
		
		System.out.println(Thread.currentThread().getName() +" ID: " + id);
		super.run();
	}

}
