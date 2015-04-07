package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.objects.GameObject;
import com.mygdx.objects.Player;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Stage stage;
	Player gameObject;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		stage = new Stage();
		gameObject = new Player();
	
		Gdx.input.setInputProcessor(stage);
		
		gameObject.texture = new Texture(Gdx.files.internal("Player_Texture.png"));
		
		stage.addActor(gameObject);
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
