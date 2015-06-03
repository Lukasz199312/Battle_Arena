package map;

import java.util.Random;

public enum MapObjects {
	GRASS_0,
	GRASS_1,
	GRASS_2;
	
	public static MapObjects getRandom(){
		Random random = new Random();
		return values()[random.nextInt(values().length)];
	}
}
