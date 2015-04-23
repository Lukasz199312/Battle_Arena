package com.mygdx.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import packets.Action_Type;
import packets.Packet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.client.NetworkController;
import com.mygdx.objects.GameObject;
import com.mygdx.objects.enemy.Enemy;
import com.mygdx.objects.player.Player;

public class GameLogic {
	private Stage stage;
	private Player Player_Me = null;
	private ArrayList<Player> ConnectedPlayerList = new ArrayList<Player>();
	private ArrayList<Enemy> EnemyList = new ArrayList<Enemy>();
	private ArrayList<GameObject> StaticGameObjectList = new ArrayList<GameObject>();
	private NetworkController networkController = new NetworkController();
	
	public GameLogic(Stage stage) {
		this.stage = stage;
	}
	

	public void CheckCollision(){
		CollisionDetection.Check(Player_Me, StaticGameObjectList);
	}
	
	public void addObject(GameObjectType Type, float position_x, float position_y){
		if(Type == GameObjectType.Player){
			this.Player_Me = new Player(new Texture(Gdx.files.internal("Player_Texture.png")), 0, 0, 64, 64);
			this.stage.addActor(Player_Me);
		}
		else if(Type == GameObjectType.StaticObject){
			this.StaticGameObjectList.add(new GameObject(new Texture(Gdx.files.internal("Enemy.png")), position_x, position_y, 32, 32));
			this.stage.addActor(StaticGameObjectList.get(StaticGameObjectList.size()-1));
		}
		else if(Type == GameObjectType.ConnectedPlayer){
			this.ConnectedPlayerList.add(new Player(new Texture(Gdx.files.internal("Player_Texture.png")), 0, 0, 64, 64));
			this.stage.addActor(ConnectedPlayerList.get(ConnectedPlayerList.size()-1));
		}
		
	}
	
	public Player getPlayer() {
		return this.Player_Me;
	}
	
	public void NetworkUpdate(){
		
		
		//Gdx.app.log("Packiet", Integer.toString( networkController.getPacketQueue().size() ));
		Packet packet = networkController.getPacket();
		if(packet == null) return;
		Gdx.app.log("Pakiet przekazany do", " GameLogic Typ: " + packet.Type);
		if(packet.Type == Action_Type.CONNECT_ME) {
			this.addObject(GameObjectType.Player, 20, 20);
			Player_Me.setID(packet.ID);
		}
		
		else if(packet.Type == Action_Type.START_UPDATE){
			while(true){
				packet = networkController.getPacket();
				if(packet != null){
					
					if(packet.Type == Action_Type.PLAYER_INFORMATION_UPDATE){
						
						Iterator<Player> iter = ConnectedPlayerList.iterator();
						boolean UpdatedData = false;
						
						while(iter.hasNext()){
							Player tmpPlayer = iter.next();
							
							if(tmpPlayer.getID() == packet.ID) {
//								Gdx.app.log("Aktualizacja ID" , Integer.toString(packet.ID));
								UpdatedData = true;
								break;
							}
						}
						
						if(UpdatedData == false) {
							if(packet.ID != Player_Me.getID()) this.addObject(GameObjectType.ConnectedPlayer, 50, 50);
						}
					}
					
					if(packet.Type == Action_Type.END_UPDATE) {
//						Gdx.app.log("Action" , "END");
						break;
					}
				}
				
			}
			
		}
	}
//	public BlockingQueue<Packet>getPacketQueue(){
//		return PacketQueue;
//	}
	
	
}
