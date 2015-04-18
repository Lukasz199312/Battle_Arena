package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.objects.GameObject;
import com.mygdx.objects.enemy.Enemy;
import com.mygdx.objects.player.Player;

public class GameLogic {
	private Stage stage;
	private Player Player_Me;
	private ArrayList<Player> ConnectedPlayerList = new ArrayList<Player>();
	private ArrayList<Enemy> EnemyList = new ArrayList<Enemy>();
	private ArrayList<GameObject> StaticGameObjectList = new ArrayList<GameObject>();
	
	public GameLogic(Stage stage) {
		this.stage = stage;
	}
	

	public void CheckCollision(){
		CollisionDetection.Check(Player_Me, StaticGameObjectList);
	}
	
	public void addObject(GameObjectType Type, float position_x, float position_y){
		if(Type == GameObjectType.Player){
			this.Player_Me = new Player(new Texture(Gdx.files.internal("Player_Texture.png")), 
													0, 0, 
													64, 64);
			this.stage.addActor(Player_Me);
		}
		else if(Type == GameObjectType.StaticObject){
			this.StaticGameObjectList.add(new GameObject(new Texture(Gdx.files.internal("Enemy.png")), 
														position_x, 
														position_y, 
														32, 32));
			
			this.stage.addActor(StaticGameObjectList.get(StaticGameObjectList.size()-1));
		}
		
	}
	
	public Player getPlayer() {
		return this.Player_Me;
	}
	
	
}
