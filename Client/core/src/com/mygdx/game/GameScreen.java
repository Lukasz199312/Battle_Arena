package com.mygdx.game;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import packets.Action_Type;
import packets.Packet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	public GameScreen() {	
		
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
		gameLogic.NetworkUpdate();
		Gdx.gl.glClearColor(255, 255, 255, 0);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    orto.update();
	    stage.act();
	    controller.Move(delta);
	    gameLogic.CheckCollision();
	  
	    stage.draw();
	    stage.getViewport().getCamera().update();
	    gameLogic.PlayerUpdate();
	    
	   // orto.update();
	  //  orto = new OrthographicCamera(1, h/2);
	   
	}
	

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		
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
