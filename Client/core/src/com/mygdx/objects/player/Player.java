package com.mygdx.objects.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.controller.Player_Controller;
import com.mygdx.objects.GameObject;
import com.mygdx.objects.Statistic;

public class Player extends GameObject{
	private Statistic statistic;
	private Player_Controller controller;
	
	public Player() { 
		super(); 
		
		statistic = new Statistic();
		statistic.Speed = 80;
		
		controller = new Player_Controller(statistic.Speed);
	}
	
	@Override
	public void act(float delta) {
		controller.Move(transform, delta);
	}
}
