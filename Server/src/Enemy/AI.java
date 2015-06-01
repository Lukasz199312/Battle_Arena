package Enemy;

import java.util.Random;

import packets.MoveDirection;

public class AI {
	
	private float Position_x = 0;
	private float Position_y = 0;
	private int PlayerTarget = -1;
	private Mode mode = Mode.Sleep;
	private int ID;
	private Random rand = new Random();
	public MoveDirection Direction;
	
	public AI(int id, float x, float y) {
		this.ID = id;
		this.Position_x = x;
		this.Position_y = y;
	}
	
	public MoveDirection Move(float deltaTime){
		if(mode != Mode.Sleep){
			float move = Position_x + deltaTime * 180;
			
			Position_x = move;
			
			return null;
		}
		
		return null;

	}

	
	public float getX(){
		return Position_x;
	}
	
	public float getY(){
		return Position_y;
	}
	
	public void setX(float x) {
		this.Position_x = x;
	}
	
	public void setY(float y) {
		this.Position_y = y;
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public int getID() {
		return ID;
	}
}
