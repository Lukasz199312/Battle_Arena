package packets;

import java.io.Serializable;

import map.MapObjects;

public class Packet implements Serializable{
	public int ID;
	public float x;
	public float y;
	
	public Action_Type Type;
	public MapObjects MapObjects;
	public MoveDirection Direction;
	
	
}
