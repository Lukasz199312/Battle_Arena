package Enemy;

import java.util.Random;

public class Spawn {

	private final int SPAWN_TIME_MIN = 1 * 1000;
	private final int SPAWN_TIME_MAX = 5 * 1000;
	
	private final int SPAWN_NUMBER_MIN = 1;
	private final int SPAWN_NUMBER_MAX = 2;
	
	private int MAX_SPAWNED_MONSTER = 30;
	private int nextMonsterSizeSpawn = 0;
	private long nextSpawnTime = 0;
	private int SpawnedMonster = 0;
	private int HEAD_ID = 0;
	
	private Random rand = new Random();
	
	public Spawn() {
		nextSpawnTime = GenerateNextWaveTime();
		nextMonsterSizeSpawn = GenerateNextWaveMonsterSize();
	}
	
	public AI get(){
		if(System.currentTimeMillis() > nextSpawnTime){
			if(SpawnedMonster >= nextMonsterSizeSpawn) {
				nextSpawnTime = GenerateNextWaveTime();
				nextMonsterSizeSpawn = GenerateNextWaveMonsterSize();
				SpawnedMonster = 0;
				
				return null;
			}
			
			int x = rand.nextInt((500 - 1) + 1) + 1;
			int y = rand.nextInt((500 - 1) + 1) + 1;
			
			AI Enemy = new AI(HEAD_ID, x, y);
			
			HEAD_ID ++;
			SpawnedMonster ++;
			
			return Enemy;
		} else
			return null;
	}
	
	public int getMAX_SPAWNED_MONSTER(){
		return this.MAX_SPAWNED_MONSTER;
	}
	
	private long GenerateNextWaveTime(){
		return System.currentTimeMillis() + (rand.nextInt((SPAWN_TIME_MAX - SPAWN_TIME_MIN) + 1) + SPAWN_TIME_MIN);
	}
	
	private int GenerateNextWaveMonsterSize(){
		return rand.nextInt((SPAWN_NUMBER_MAX - SPAWN_NUMBER_MIN) + 1) + SPAWN_NUMBER_MIN;
	}
	

}
