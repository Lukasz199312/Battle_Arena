package com.mygdx.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import packets.Action_Type;
import packets.MoveDirection;
import packets.Packet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.client.Client;
import com.mygdx.controller.Player_Controller;
import com.mygdx.objects.GameObject;
import com.mygdx.objects.shoot;
import com.mygdx.objects.player.Player;

public class GameScreen implements Screen{
	  private Sprite mySprite;
	  private SpriteBatch batch = new SpriteBatch();
	  private OrthographicCamera orto;
	  private float w = Gdx.graphics.getWidth();
      private float h = Gdx.graphics.getHeight();
      private Stage stage;
      private Stage MapStage;
      private SpriteBatch myBatch = new SpriteBatch();
      private Player_Controller controller = new Player_Controller(180);
      private ArrayList<GameObject> GameObjectList = new ArrayList<GameObject>();
      private Player player;
      private Player Enemy, Enemy2 ,Enemy3, Enemy4;
      private GameLogic gameLogic;
      private int FPS;
      private int FPSCount;
      private long FPS_TIME = 0;
      private int Packet_number = 0;
      
      private SpriteBatch spriteBatch;
      private BitmapFont font;
      private MoveDirection Last_Direction;
      private boolean MouseClicked = false;
      
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	public GameScreen() {	
		
        batch = new SpriteBatch();    
        font = new BitmapFont();
        font.setColor(Color.RED);
		
		orto = new OrthographicCamera(1, h/2);
        orto.update();
    
	    stage = new Stage(new StretchViewport(860, 480)); 
	    MapStage = new Stage(new StretchViewport(860, 480));
	    
	    gameLogic = new GameLogic(stage,MapStage);
	   

	    stage.getBatch().begin();
	    
	    Iterator<GameObject> iter = gameLogic.getMapObject().iterator();
	    
	    while(iter.hasNext()){
	    	GameObject mapObject = iter.next();
	    	stage.getBatch().draw(mapObject.getTexture(), 0, 0, 64*32, 64*32);
	    }
	    
	    stage.getBatch().end();
	    
	    gameLogic.addObject(GameObjectType.StaticObject, -1, 230, 366);
	    gameLogic.addObject(GameObjectType.GRASS_F, -1, 300, 420);
	    
	    for(int i = 0; i<32;i++){
	    	gameLogic.addObject(GameObjectType.Tower, -1, i*64, 0);
	    }
	    
	    for(int i = 0; i<16;i++){
	    	gameLogic.addObject(GameObjectType.Tower, -1, 0, i*128);
	    }
	    for(int i = 0; i<16;i++){
	    	gameLogic.addObject(GameObjectType.Tower, -1, 31*64, i*128);
	    }
	    for(int i = 0; i<32;i++){
	    	gameLogic.addObject(GameObjectType.Tower, -1, i*64, 15*128);
	    }
	    
	    while(true){

	    	gameLogic.BuildMap();
	    	if(gameLogic.getPlayer() != null) break;
	    }
	    controller.gameObject = gameLogic.getPlayer();
	    
	    Gdx.input.setInputProcessor(stage);
	    


	}
	
	@Override
	public void render(float delta) {
		stage.getCamera().position.x = gameLogic.getPlayer().getX() + gameLogic.getPlayer().getWidth() / 2;
		stage.getCamera().position.y = gameLogic.getPlayer().getY() + gameLogic.getPlayer().getHeight() / 2;
		stage.getCamera().update();
		
		
		 if( MouseClicked == false && Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			 
			 System.out.println("Player x: " + gameLogic.getPlayer().getX());
			 System.out.println("Player y: " + gameLogic.getPlayer().getY());
			 
			 float Shot_x = gameLogic.getPlayer().getX() - (Gdx.graphics.getWidth() / 2f) + Gdx.input.getX();
			 float Shot_y = gameLogic.getPlayer().getY() - (Gdx.graphics.getHeight() / 2f) + (Gdx.graphics.getHeight() - Gdx.input.getY());
			 
			 System.out.println("Mouse Position x: " + Shot_x );
			 System.out.println("Mouse Position y: " + Shot_y );
			 float x =  Gdx.input.getX() ;
			 
			 
			    shoot shoot = new shoot(new Texture(Gdx.files.internal("shoot.png")), 200,200);
			  //  shoot.start(gameLogic.getPlayer().getX(), gameLogic.getPlayer().getY(), Shot_x, Shot_y, 0.000001f);
			    shoot.start(gameLogic.getPlayer().getX() + gameLogic.getPlayer().getWidth() / 2, gameLogic.getPlayer().getY(), Shot_x, Shot_y, 5);
			    stage.addActor(shoot);
			    
			    Packet packet = new Packet();
			    packet.ID  = gameLogic.getPlayer().getID();
			    packet.x = shoot.getShootX();
			    packet.y = shoot.getShootY();
			    packet.Type = Action_Type.SHOOT;
			    gameLogic.getClient().getPacketQueueUpdate().offer(packet);
			 
			 MouseClicked = true;
		 }
		 
		 if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) == false) MouseClicked = false;
		
		gameLogic.NetworkUpdate();
		Gdx.gl.glClearColor(255, 255, 255, 0);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    orto.update();
	    stage.act();
	
	    MoveDirection Direction = controller.Move(delta);
	    if(Direction == Last_Direction){
	    	Direction = null;
	    }
	    else{
	    	Last_Direction = Direction;
	    	Packet_number++;
	    }
	    
	    stage.getBatch().begin();
	    
	    	Iterator<GameObject> iter = gameLogic.getMapObject().iterator();
	    	while(iter.hasNext()){
	    		GameObject object = iter.next();
	    		stage.getBatch().draw(object.getTexture(),object.getX(), object.getY());
	    	}
	    	
	    	iter = gameLogic.getMapForeground().iterator();
	    	while(iter.hasNext()){
	    		GameObject object = iter.next();
	    		stage.getBatch().draw(object.getTexture(),object.getX(), object.getY());
	    	}
	    
	    stage.getBatch().end();
	    
	    gameLogic.CheckCollision();
	    MapStage.draw();
	    stage.draw();
	    
	    
	    stage.getBatch().begin();
	    
    	iter = gameLogic.getMapForeground2().iterator();
    	while(iter.hasNext()){
    		GameObject object = iter.next();
    		stage.getBatch().draw(object.getTexture(),object.getX(), object.getY());
    	}
    
    stage.getBatch().end();
	    
	    batch.begin();
	    font.draw(batch, "MS: "  + Long.toString(gameLogic.getClient().getMS()), 600, 25);
	    font.draw(batch, "FPS: "  + Long.toString(FPS), 500, 25);
	    font.draw(batch, "Queue-GET: "  + Long.toString(gameLogic.getClient().getPacketQueue().size()), 200, 25);
	    font.draw(batch, "PACKET-SEND: "  + Long.toString(Packet_number), 10, 25);
	    
	    if(Direction != null)
	    	 font.draw(batch, "DIRECTION: "  + Direction.toString(), 10, 55);
	    batch.end();

	    if(Direction != null)
	    System.out.println(Direction.toString());
        
	
	    stage.getViewport().getCamera().update();
	    MapStage.getViewport().getCamera().update();
	    
	    
	    
	    
	    gameLogic.PlayerUpdate(Direction);
	    
	    FPSCount = FPSCount + 1;
	    if(System.currentTimeMillis() >  1000 + FPS_TIME){
		    FPS = new Integer(FPSCount);
		    FPSCount = 0;
		    FPS_TIME = System.currentTimeMillis();
		    Packet_number = 0;
	    }
	   
	}
	

	@Override
	public void resize(int width, int height) {
		//stage.getViewport().update(width, height);
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
