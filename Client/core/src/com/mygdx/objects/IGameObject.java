package com.mygdx.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Transform;

public interface IGameObject {
	public int getID();
	public void setTranform(Transform transform);
	public void setTexture(Texture texture);
}
