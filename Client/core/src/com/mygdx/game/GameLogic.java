package com.mygdx.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import packets.Action_Type;
import packets.MoveDirection;
import packets.Packet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.client.Client;
import com.mygdx.client.NetworkController;
import com.mygdx.objects.GameObject;
import com.mygdx.objects.enemy.Enemy;
import com.mygdx.objects.player.Player;
import com.sun.javafx.scene.traversal.Direction;

public class GameLogic {
	private Stage stage;
	private Player Player_Me = null;
	private ArrayList<Player> ConnectedPlayerList = new ArrayList<Player>();
	private ArrayList<Enemy> EnemyList = new ArrayList<Enemy>();
	private ArrayList<GameObject> StaticGameObjectList = new ArrayList<GameObject>();
	private NetworkController networkController = new NetworkController();
	private long time = 0;
	private long FPS = 0;
	
	public GameLogic(Stage stage) {
		this.stage = stage;
	}
	

	public void CheckCollision(){
		CollisionDetection.Check(Player_Me, StaticGameObjectList);
	}
	
	public void addObject(GameObjectType Type, int ID, float position_x, float position_y){
		if(Type == GameObjectType.Player){
			this.Player_Me = new Player(new Texture(Gdx.files.internal("Player_Texture.png")), ID ,0, 0, 64, 64);
			this.stage.addActor(Player_Me);
		}
		else if(Type == GameObjectType.StaticObject){
			this.StaticGameObjectList.add(new GameObject(new Texture(Gdx.files.internal("Enemy.png")), position_x, position_y, 32, 32));
			this.stage.addActor(StaticGameObjectList.get(StaticGameObjectList.size()-1));
		}
		else if(Type == GameObjectType.ConnectedPlayer){
			this.ConnectedPlayerList.add( new Player(new Texture(Gdx.files.internal("Player_Texture.png")), ID ,position_x, position_y, 64, 64) );
			this.stage.addActor(ConnectedPlayerList.get(ConnectedPlayerList.size()-1));
		}
		else if(Type == GameObjectType.Enemy){
			this.EnemyList.add( new Enemy(new Texture(Gdx.files.internal("zombie.png")), ID, position_x, position_y, 64, 64) );
			this.stage.addActor(EnemyList.get(EnemyList.size() - 1));
		}
		
	}
	
	
	
	public Player getPlayer() {
		return this.Player_Me;
	}
	
	public void NetworkUpdate(){
		while(true){
			Packet packet = networkController.getPacket();
			if(packet == null) return;
			
			System.out.println(packet.Type);
			
			if(packet.Type == Action_Type.PLAYER_UPDATE){
				Iterator<Player> iter = ConnectedPlayerList.iterator();
				while(iter.hasNext()){
					Player player = iter.next();
					if(packet.ID == player.getID()){
						player.setX(packet.x);
						player.setY(packet.y);
						player.Direction = packet.Direction;
						packet = null;
						break;
					}
				}
				if(packet != null){
					addObject(GameObjectType.ConnectedPlayer,packet.ID , packet.x, packet.y);
				}
				
			}
			
			else if(packet.Type == Action_Type.REMOVE_PLAYER){
				Iterator<Player> iter = ConnectedPlayerList.iterator();
				while(iter.hasNext()){
					Player player = iter.next();
					if(packet.ID == player.getID()){
						
						Iterator<Actor> iterActor = stage.getActors().iterator();
						while(iterActor.hasNext()){
							Actor actor = iterActor.next();

							if(actor == player) {
								iterActor.remove();
								break;
							}
						}
						
						iter.remove();
						break;
					}
				}
			}
			else if( packet.Type == Action_Type.ENEMY_UPDATE){
				Iterator<Enemy> iter = EnemyList.iterator();
				while(iter.hasNext()){
					Enemy enemy = iter.next();
					if(packet.ID == enemy.getID()){
//						enemy.setX(packet.x);
//						enemy.setY(packet.y);
						enemy.Direction = packet.Direction;
						packet = null;
						break;
					}
				}
				if(packet != null){
					addObject(GameObjectType.Enemy, packet.ID , packet.x, packet.y);
					EnemyList.get(EnemyList.size() - 1).Direction = packet.Direction;
					EnemyList.get(EnemyList.size() - 1).setSpeed(10);
				}
			}

		}
	}
	
	public void PlayerUpdate(MoveDirection Direction){
		
	//	if(System.currentTimeMillis() - time > 0){
			if(Direction == null) return;
			
			Packet packet = new Packet();
			packet.Type = Action_Type.PLAYER_UPDATE;
			
			packet.ID = Player_Me.getID();
			packet.x = Player_Me.getX();
			packet.y = Player_Me.getY();
			packet.Direction = Direction;
			
			networkController.getPacketQueueUpdate().offer(packet);
			
//			if(getClient().getPacketQueue().size() >= 50) getClient().getPacketQueue().clear();
//			if(getClient().getPacketQueueUpdate().size() >= 50) getClient().getPacketQueueUpdate().clear();
			
			time = System.currentTimeMillis();
	//	}

		
	}
	
	public void RegisterPlayer(){
		Packet packet = networkController.getPacket();
		if(packet == null) return;
		if(packet.Type == Action_Type.NEW_PLAYER){
			addObject(GameObjectType.Player, packet.ID , packet.x, packet.y);
		}
	}
	
	public boolean BuildMap(){
		Packet packet = networkController.getPacket();
		if(packet == null) return false;
		else if(packet.Type == Action_Type.GRASS_0){
			GameObject mapobject = new GameObject(new Texture(Gdx.files.internal("grass_0.png")), 110, 100);
			stage.addActor(mapobject);
			System.out.println("Dodalem obiekt");
			return false;
		}
		else if (packet.Type == Action_Type.NEW_PLAYER){
			addObject(GameObjectType.Player, packet.ID , packet.x, packet.y);
			return true;
		}
		return false;
	}
	
	public Client getClient(){
		return networkController.getClient();
	}
	
	
}
