package com.mygdx.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.badlogic.gdx.math.Vector2;

import packets.Packet;

public class Client extends Thread {
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private ObjectOutputStream sendData;
	private Thread MainThread;
	private BlockingQueue<Vector2> Player_Position_queue = new ArrayBlockingQueue<Vector2>(1);
	
	public Client(Thread MainThread, BlockingQueue<Vector2> Player_Position_queue) {
		try {
			socket = new Socket("localhost", 82);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.MainThread = MainThread;
		this.Player_Position_queue =  Player_Position_queue;
	}
	
	@Override
	public void run() {
		try {
			sendData = new ObjectOutputStream(socket.getOutputStream());
						
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean Exit = false;
		while(!Exit){
			try {
				Vector2 vector = Player_Position_queue.take();
				
				Packet packet = new Packet();
				packet.x = vector.x;
				packet.y = vector.y;
				
				sendData.writeObject(packet);
				if(MainThread.isAlive() == false) Exit = true;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}
}
