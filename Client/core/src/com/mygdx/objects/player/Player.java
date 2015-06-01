package com.mygdx.objects.player;

import packets.MoveDirection;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.controller.Player_Controller;
import com.mygdx.objects.GameObject;

public class Player extends GameObject{
	
	
	public Player(Texture texture, int ID, float position_x, float position_y, float width, float height) {
		super(texture, position_x, position_y, width, height);
		this.setID(ID);
	}
	
	@Override
	public void act(float delta) {				
		old_x = getX();
		old_y = getY();
		super.act(delta);
	}
}
