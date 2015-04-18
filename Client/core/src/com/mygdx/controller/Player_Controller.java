package com.mygdx.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Transform;
import com.mygdx.objects.GameObject;

public class Player_Controller {
	private float Speed;
	private boolean isMoved;
	public  GameObject gameObject;
	public Player_Controller(float Speed) { this.Speed = Speed; }
	
	public void Move(float delta){
	
		isMoved = false;
		
		Move_UP(gameObject.getY(), delta);
		Move_Down(gameObject.getY(), delta);
		
		Move_Left(gameObject.getX(), delta);
		Move_Right(gameObject.getX(), delta);
		
	}
	
	public void Move_UP(float position_y, float delta){
		if( Gdx.input.isKeyPressed(Keys.W)){
			gameObject.setY( position_y + (delta * Speed) );
			gameObject.UpDateBounds();
			isMoved = true;
		}
	}
	
	public void Move_Down(float position_y, float delta){
		if( Gdx.input.isKeyPressed(Keys.S)){
			gameObject.setY( position_y - (delta * Speed) );
			gameObject.UpDateBounds();
			isMoved = true;
		}
	}
	
	public void Move_Left(float position_x, float delta){
		if( Gdx.input.isKeyPressed(Keys.A)){
			gameObject.setX( position_x - (delta * Speed) );
			gameObject.UpDateBounds();
			isMoved = true;
		}
	}
	
	public void Move_Right(float position_x, float delta){
		if( Gdx.input.isKeyPressed(Keys.D)){
			gameObject.setX( position_x + (delta * Speed) );
			gameObject.UpDateBounds();
			isMoved = true;
		}
	}
	
	public boolean isMoved() { return isMoved; }
}
