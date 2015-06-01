package Enemy;

import java.util.Random;

import packets.MoveDirection;
import sun.font.LayoutPathImpl.EndType;

public class AI {
	
	private float Position_x = 0;
	private float Position_y = 0;
	private int PlayerTarget = -1;
	private Mode mode = Mode.Sleep;
	private int ID;
	private Random rand = new Random();
	public MoveDirection Direction = MoveDirection.RIGHT;
	public MoveDirection LastMove = MoveDirection.STOP;
	private int Speed = 40;
	
	public long NextMove;
	
	public AI(int id, float x, float y) {
		this.ID = id;
		this.Position_x = x;
		this.Position_y = y;
	}
	
	public void Move(float deltaTime){
		LastMove = Direction;
		
		switch(Direction){
		case RIGHT:
			Position_x = Position_x + deltaTime * Speed;
			break;
		case RIGHT_UP:
			Position_x = Position_x + deltaTime * Speed;
			Position_y = Position_y + deltaTime * Speed;
			break;
			
			default:
			break;
		}

		
		if(NextMove <= System.currentTimeMillis()){
		//	int i = rand.nextInt((2 - 1) + 1) + 1;
			int i = 0;
			if(Direction == MoveDirection.RIGHT) Direction = MoveDirection.RIGHT_UP;
			else if(Direction == MoveDirection.RIGHT_UP) Direction = MoveDirection.RIGHT;
			
			if(LastMove != Direction) System.out.println("Zmiana kierunku: " + getID());
			
			
			
			NextMove = System.currentTimeMillis() + 500;
			
			System.out.println( LastMove + "  " + getID());
			System.out.println( Direction + "  " + getID());
		}

		

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
	
	public int getSpeed(){
		return this.Speed;
	}
	
	public void setSpeed(int Speed){
		this.Speed = Speed;
	}
}
