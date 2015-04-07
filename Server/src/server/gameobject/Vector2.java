package server.gameobject;

public class Vector2 {
	public float x;
	public float y;
	
	public Vector2() { set(0, 0); }
	public Vector2(float x , float y) { this.x = x; this.y = y; }
	
	public void set(float x, float y) { this.x = x; this.y = y; }
	public Vector2 get(){ return this; }
}
