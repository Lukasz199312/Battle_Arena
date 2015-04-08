package com.mygdx.game;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.client.Client;
import com.mygdx.objects.GameObject;
import com.mygdx.objects.Player_net;
import com.mygdx.objects.player.Player;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Stage stage;
	Player gameObject;

	BlockingQueue<Vector2> Player_Position_queue = new ArrayBlockingQueue<Vector2>(1);
	
	@Override
	public void create () {
		Client client = new Client(Thread.currentThread(), Player_Position_queue);
		new Thread(client).start();
		
		batch = new SpriteBatch();
		stage = new Stage();
		gameObject = new Player(Player_Position_queue);
	
		Gdx.input.setInputProcessor(stage);
		
		gameObject.texture = new Texture(Gdx.files.internal("Player_Texture.png"));
		
		
		Player_net player_net = new Player_net();
		player_net.transform = new Transform(new Vector2(50, 50), 0);
		player_net.texture = new Texture(Gdx.files.internal("Player_Texture.png"));
		
		stage.addActor(gameObject);
		stage.addActor(player_net);
		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(255, 255, 255, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		batch.end();
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}
}
