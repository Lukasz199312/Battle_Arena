package com.mygdx.objects.player;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.controller.Player_Controller;
import com.mygdx.objects.GameObject;
import com.mygdx.objects.Statistic;

public class Player extends GameObject{
	private Statistic statistic;
	private Player_Controller controller;
	private BlockingQueue<Vector2> Player_Position_queue = new ArrayBlockingQueue<Vector2>(1);
	
	public Player(BlockingQueue<Vector2> Player_Position_queue) { 
		super(); 
		
		statistic = new Statistic();
		statistic.Speed = 80;
		this.Player_Position_queue = Player_Position_queue;
		
		controller = new Player_Controller(statistic.Speed);
	}
	
	
	@Override
	public void act(float delta) {
		controller.Move(transform, delta);
		if(controller.isMoved() == true) {
			try {
				Player_Position_queue.put(transform.getPosition());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
