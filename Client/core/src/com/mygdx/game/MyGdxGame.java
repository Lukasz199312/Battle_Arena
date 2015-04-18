package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class MyGdxGame implements ApplicationListener {

	private Screen currentScreen = null;
	
	@Override
	public void create() {
		currentScreen = new GameScreen();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		this.currentScreen.render(Gdx.graphics.getDeltaTime());
		
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
	public void dispose() {
		this.currentScreen.dispose();
		
	}

}
