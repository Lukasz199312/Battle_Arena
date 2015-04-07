package com.mygdx.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Transform;

public class Player_Controller {
	private float Speed;
	
	public Player_Controller(float Speed) { this.Speed = Speed; }
	
	public void Move(Transform transform, float delta){
		Move_UP(transform, delta);
		Move_Down(transform, delta);
		
		Move_Left(transform, delta);
		Move_Right(transform, delta);
	}
	
	public void Move_UP(Transform transform, float delta){
		if( Gdx.input.isKeyPressed(Keys.W)){
			transform.setPosition(new Vector2(transform.getPosition().x, transform.getPosition().y + (delta * Speed) ));
		}
	}
	
	public void Move_Down(Transform transform, float delta){
		if( Gdx.input.isKeyPressed(Keys.S)){
			transform.setPosition(new Vector2(transform.getPosition().x, transform.getPosition().y - (delta * Speed) ));
		}
	}
	
	public void Move_Left(Transform transform, float delta){
		if( Gdx.input.isKeyPressed(Keys.A)){
			transform.setPosition(new Vector2(transform.getPosition().x - (delta * Speed), transform.getPosition().y ));
		}
	}
	
	public void Move_Right(Transform transform, float delta){
		if( Gdx.input.isKeyPressed(Keys.D)){
			transform.setPosition(new Vector2(transform.getPosition().x + (delta * Speed), transform.getPosition().y ));
		}
	}
}
