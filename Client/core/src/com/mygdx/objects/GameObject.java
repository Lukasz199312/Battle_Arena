package com.mygdx.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameObject extends Actor{
	public Transform transform;
	public Texture texture;
	
	public GameObject() { this.transform = new Transform(); }
	public GameObject(Transform transform) { this.transform = transform; }
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, transform.getPosition().x, transform.getPosition().y);
		Gdx.app.log("Position", transform.getPosition().toString());
	}
	
}
