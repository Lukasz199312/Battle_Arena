package server.gameobject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Player_Socket extends Thread{
	
	private int id;
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	
	public Player_Socket(Socket socket, int id) { this.socket = socket; this.id = id; }
	
	@Override
	public void run() {
		
		try {
			in = new DataInputStream(socket.getInputStream());
			System.out.println(in.readUTF());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(Thread.currentThread().getName() +" ID: " + id);
		super.run();
	}

}
