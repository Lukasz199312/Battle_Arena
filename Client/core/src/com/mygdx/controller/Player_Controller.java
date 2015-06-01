package com.mygdx.controller;

import packets.MoveDirection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Transform;
import com.mygdx.objects.GameObject;
import com.sun.javafx.scene.traversal.Direction;

public class Player_Controller {
	private float Speed;
	private boolean isMoved;
	public  GameObject gameObject;
	public Player_Controller(float Speed) { this.Speed = Speed; }
	public MoveDirection Direction;
	
	public MoveDirection Move(float delta){
		if(gameObject == null) return null;
		isMoved = false;
		this.Direction = MoveDirection.STOP;
		
		Move_UP(gameObject.getY(), delta);
		Move_Down(gameObject.getY(), delta);
		
		Move_Left(gameObject.getX(), delta);
		Move_Right(gameObject.getX(), delta);
		
		return this.Direction;
		
	}
	
	public void Move_UP(float position_y, float delta){
		if( Gdx.input.isKeyPressed(Keys.W)){
			gameObject.setY( position_y + (delta * Speed) );
			gameObject.UpDateBounds();
			isMoved = true;
			
			if(this.Direction != MoveDirection.STOP){
				if(this.Direction == MoveDirection.LEFT) this.Direction = MoveDirection.LEFT_UP;
				else if(this.Direction == MoveDirection.RIGHT) this.Direction = MoveDirection.RIGHT_UP;
			}	
			else this.Direction = MoveDirection.UP;
			
		}
	}
	
	public void Move_Down(float position_y, float delta){
		if( Gdx.input.isKeyPressed(Keys.S)){
			gameObject.setY( position_y - (delta * Speed) );
			gameObject.UpDateBounds();
			isMoved = true;
			
			if(this.Direction != MoveDirection.STOP){
				if(this.Direction == MoveDirection.LEFT) this.Direction = MoveDirection.LEFT_DOWN;
				else if(this.Direction == MoveDirection.RIGHT) this.Direction = MoveDirection.RIGHT_DOWN;
			}
			else this.Direction = MoveDirection.DOWN;
		}
	}
	
	public void Move_Left(float position_x, float delta){
		if( Gdx.input.isKeyPressed(Keys.A)){
			gameObject.setX( position_x - (delta * Speed) );
			gameObject.UpDateBounds();
			isMoved = true;
			
			if(this.Direction != MoveDirection.STOP){
				if(this.Direction == MoveDirection.UP) this.Direction = MoveDirection.LEFT_UP;
				else if(this.Direction == MoveDirection.DOWN) this.Direction = MoveDirection.LEFT_DOWN;
			}
			else this.Direction = MoveDirection.LEFT;
		}
	}
	
	public void Move_Right(float position_x, float delta){
		if( Gdx.input.isKeyPressed(Keys.D)){
			gameObject.setX( position_x + (delta * Speed) );
			gameObject.UpDateBounds();
			isMoved = true;
			
			if(this.Direction != MoveDirection.STOP){
				if(this.Direction == MoveDirection.UP) this.Direction = MoveDirection.RIGHT_UP;
				else if(this.Direction == MoveDirection.DOWN) this.Direction = MoveDirection.RIGHT_DOWN;
			}
			else this.Direction = MoveDirection.RIGHT;
		}
	}
	
	public boolean isMoved() { return isMoved; }
}
