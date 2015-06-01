package com.mygdx.game;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import packets.Action_Type;
import packets.MoveDirection;
import packets.Packet;

import com.badlogic.gdx.Gdx;
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
import com.mygdx.objects.player.Player;

public class GameScreen implements Screen{
	  private Sprite mySprite;
	  private SpriteBatch batch = new SpriteBatch();
	  private OrthographicCamera orto;
	  private float w = Gdx.graphics.getWidth();
      private float h = Gdx.graphics.getHeight();
      private Stage stage;
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
	    gameLogic = new GameLogic(stage);
	   
		
	    gameLogic.addObject(GameObjectType.StaticObject, -1, 230, 366);
	    gameLogic.addObject(GameObjectType.StaticObject, -1, 262, 366);
	    gameLogic.addObject(GameObjectType.StaticObject, -1, 294, 366);

	    
	    while(true){	//Wait for Connecting
	    	try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	gameLogic.RegisterPlayer();
	    	if(gameLogic.getPlayer() != null){
	    		controller.gameObject = gameLogic.getPlayer();
	    		break;
	    	}
	    }
	    
	    System.out.println("ID: " + gameLogic.getPlayer().getID());
	    
	    Gdx.input.setInputProcessor(stage);

	}
	
	@Override
	public void render(float delta) {
		stage.getCamera().position.x = gameLogic.getPlayer().getX() + gameLogic.getPlayer().getWidth() / 2;
		stage.getCamera().position.y = gameLogic.getPlayer().getY() + gameLogic.getPlayer().getHeight() / 2;
		stage.getCamera().update();
		
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
	    
	    gameLogic.CheckCollision();
	    
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
        
	    stage.draw();
	    stage.getViewport().getCamera().update();
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
