package map;

public class Map {
	private static MapProperties Properties = new MapProperties();
	private static MapObjects [][]MapAre;
	
	public static void GenerateMap(int x, int y){
		Properties.setX(x);
		Properties.setY(y);
		
		MapAre = new MapObjects[x][y];
		GenerateGround();
	}
	
	private static void GenerateGround(){
		for(int i = 0; i < Properties.getX(); i++){
			for(int j = 0; j < Properties.getY(); j++){
				MapAre[i][j] = MapObjects.getRandom();
			}
		}
	}
	
	public static MapObjects[][] getMapAre(){
		return MapAre;
	}
}
