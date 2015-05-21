package com.mygdx.objects.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.objects.GameObject;

public class Enemy extends GameObject{

	public Enemy(Texture texture,int ID, float position_x, float position_y, float width, float height) {
		
		super(texture, position_x, position_y, width, height);
		this.ID = ID;

	}

}
