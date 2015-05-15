package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import packets.Packet;
import server.gameobject.CreateConnection;
import server.gameobject.Player_Socket;

public class Server extends Thread{
	
	private ServerSocket serverSocket;
	private int Port;
	private String Server_Address;
	private ArrayList<Player_Socket> PlayerList = new ArrayList<Player_Socket>();
	private Thread MainThread;
	
	public Server(String Server_Address, int Port, Thread thread) {
		this.Port = Port;
		this.Server_Address = Server_Address;
		this.MainThread = thread;
	}
	
	@Override
	public void run() {
		
		try {
		serverSocket = new ServerSocket(Port);
		} catch (IOException e) {
			System.out.println("Port: " + Port + " Jest Zajety");
			e.printStackTrace();
		}
		
		System.out.println("Server Running on: " + serverSocket.getInetAddress().getHostAddress());
		while(true){
			
			try {
				Player_Socket player =  CreateConnection.New(serverSocket.accept(), MainThread);
				player.setPlayerList(PlayerList);
				
				List list = Collections.synchronizedList(PlayerList);
				list.add(player);
				
				
				new Thread(player).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
		
	}

}
