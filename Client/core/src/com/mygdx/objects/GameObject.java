package com.mygdx.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameObject extends Actor{
	protected Texture texture;
	protected Rectangle bounds;
	protected float old_x;
	protected float old_y;
	protected int ID;
	
	public GameObject(Texture texture, float position_x, float position_y) {
		this.texture = texture;
		this.setX(position_x);
		this.setY(position_y);
		
		this.old_x = getX();
		this.old_y = getY();
	}
	
	public GameObject(Texture texture, float position_x, float position_y, float width, float height) {
		this.texture = texture;
		this.setX(position_x);
		this.setY(position_y);
		this.setWidth(width);
		this.setHeight(height);
		
		this.old_x = getX();
		this.old_y = getY();
		
		InitRectangle();
	}
	
	private void InitRectangle(){ 
		bounds = new Rectangle(getX(), getY(), getWidth(), getHeight()); 
	}
	
	public void UpDateBounds(){
		bounds.x = getX();
		bounds.y = getY();
	}
	
	public Rectangle getBounds() { 
		return bounds; 
	}
	
	public void setOld(){
		setX(old_x);
		setY(old_y);
		UpDateBounds();
	}
	
	public void setOldY(){
		setY(old_y);
		UpDateBounds();
	}
	
	public void setOldX(){
		setX(old_x);
		UpDateBounds();
	}
	
	public float getOldX(){
		return old_x;
	}
	
	public float getOldY(){
		return old_y;
	}
	
	public int getID(){
		return ID;
	}
	
	public void setID(int ID){
		this.ID = ID;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY());
	}
}
