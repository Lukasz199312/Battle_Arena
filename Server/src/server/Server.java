package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import server.gameobject.CreateConnection;
import server.gameobject.Player_Socket;

public class Server extends Thread{
	
	private ServerSocket serverSocket;
	private int Port;
	private String Server_Address;
	private ArrayList<Player_Socket> PlayerList = new ArrayList<Player_Socket>();
	
	public Server(String Server_Address, int Port) {
		this.Port = Port;
		this.Server_Address = Server_Address;
	}
	
	@Override
	public void run() {
		
		try {
		serverSocket = new ServerSocket(Port);
		} catch (IOException e) {
			System.out.println("Port: " + Port + " Jest Zajety");
		}
		
		System.out.println("Server Running on: " + serverSocket.getInetAddress().getHostAddress());
		while(true){
			try {
				Player_Socket player =  CreateConnection.New(serverSocket.accept());
				PlayerList.add(player);
				
				new Thread(player).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
