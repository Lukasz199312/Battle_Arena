package com.mygdx.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.objects.IGameObject;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister.Pack;

import packets.Packet;

public class _Client extends Thread {
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private ObjectOutputStream sendData;
	private ObjectInputStream getData;
	private Thread MainThread;
	private BlockingQueue<Vector2> Player_Position_queue = new ArrayBlockingQueue<Vector2>(1);
	private int id;
	private ArrayList<IGameObject> ObjectList;
	private Queue<IGameObject> queue = new LinkedList<IGameObject>();
	
	public _Client(Thread MainThread, BlockingQueue<Vector2> Player_Position_queue) {
		try {
			socket = new Socket("localhost", 82);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			sendData = new ObjectOutputStream(socket.getOutputStream());
			getData = new ObjectInputStream(socket.getInputStream());		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.MainThread = MainThread;
		this.Player_Position_queue =  Player_Position_queue;
	}
	
	@Override
	public void run() {
		RegisterMe();
		
		boolean Exit = false;
		while(!Exit){
			try {
				Vector2 vector = Player_Position_queue.take();
				
				Packet packet = new Packet();
				packet.x = vector.x;
				packet.y = vector.y;
				packet.ID = this.id;
				
				sendData.writeObject(packet);
				
				Object obj = getData.readObject();
				
				if(obj instanceof Packet) {
					
					Iterator<IGameObject> iter = ObjectList.iterator();
					while(iter.hasNext()){
						IGameObject gameobject = iter.next();
						packet = new Packet();
						packet = (Packet)obj;
						
						if(packet.ID != this.id){
							System.out.println("Moje ID: " + this.id);
							System.out.println("Odebralem informacje od: " + gameobject.getID());
						}
					}
					
					//System.out.println("Odebralem informacje od: " + ((Packet)obj).ID);
				}
				
				if(MainThread.isAlive() == false) Exit = true;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		

	}
	
	public void RegisterMe(){
		boolean Exit = false;
		while(!Exit){
			try {
				Object obj = getData.readObject();
				if(obj instanceof Integer){
					id = (Integer)obj;
					System.out.println("Jestem _Client nr: " + id);
				//	queue.add(new __Player(Player_Position_queue, new Transform()));
					Exit = true;
				}
			
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void setObjectList(ArrayList<IGameObject> arrayList){ this.ObjectList = arrayList;}
	public void setQueue(Queue<IGameObject> queue) { this.queue = queue; }
}