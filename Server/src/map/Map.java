package map;

public class Map {
	private static MapProperties Properties = new MapProperties();
	private static MapObjects [][]Background;
	private static MapObjects [] Foreground;
	
	public static void GenerateMap(int x, int y){
		Properties.setX(x);
		Properties.setY(y);
		
		Background = new MapObjects[x][y];
		Foreground = new MapObjects[1];
		GenerateGround();
	}
	
	private static void GenerateGround(){
		for(int i = 0; i < Properties.getX(); i++){
			for(int j = 0; j < Properties.getY(); j++){
				Background[i][j] = MapObjects.getRandom();
			}
		}
		
	
	}
	
	public static MapObjects[][] getBackground(){
		return Background;
	}
	
	public static MapProperties getProperties(){
		return Properties;
	}
	
	public static MapObjects[] getForeground(){
		return Foreground;
	}
}
