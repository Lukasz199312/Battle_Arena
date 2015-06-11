package com.mygdx.objects;

import sun.net.www.content.text.plain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class shoot extends GameObject{

	public shoot(Texture texture, float position_x, float position_y) {
		super(texture, position_x, position_y);
		time = System.currentTimeMillis() + LIFE_TIME;
	}

	private final long LIFE_TIME = 8000;
	private long time;
	private float y;
	private float x;

	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY());
	}
	
	@Override
	public void act(float delta) {
		setY(getY() + y);
		setX(getX() + x);
		//System.out.println("posistion x : " + getX());
		//System.out.println("posistion y: " + getY());
		
		if(time <= System.currentTimeMillis()) this.remove();
		super.act(delta);
		
		
	}
	
	public void start(float x1, float y1, float x2, float y2, float delta){
		
		
//		
//		(x2-x1)/deltax=n
//
//				deltax=const
//
//				zadana przez Ciebie
//
//				(y2-y1)/n=deltay
//
//				x=x1+deltax
//
//				y=y1+deltay


		
		setX(x1);
		setY(y1);
	
		//float n=sqrt((x2-x1)^2+(y2-y1)^2)/deltad
		
		float n = (float) (Math.sqrt( Math.pow(x2 - x1, 2) + Math.pow(y2-y1, 2)) / delta);
		
	
		
		x = (x2-x1)/n;
		y =(y2-y1)/n;
				
		//if(x1 < x2) y= y * -1;
		//System.out.println("Function: " + y);
		
	}
	

	public float getShootX(){
		return x;
	}
	
	public float getShootY(){
		return y;
	}
	
}
