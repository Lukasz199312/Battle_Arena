package Enemy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import packets.*;
import server.gameobject.Player_Socket;

public class EnemyController extends Thread {
	private ArrayList<Player_Socket> Player_List = new ArrayList<>();
	private ArrayList<AI> AI_LIST = new ArrayList<>();
	private Spawn spawn = new Spawn();
	private Thread mainThread;
	private float deltaTime = 0;
	
	public EnemyController(Thread mainThread) {
		this.mainThread = mainThread;
	}
	
	
	@Override
	public void run() {
		try {
			Thread.currentThread().sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long time = System.currentTimeMillis();
		
		while(true){
			List list = Collections.synchronizedList(Player_List);
			//System.out.println("DELTA: " + deltaTime);
	
			while(true){
				if(AI_LIST.size() >= spawn.getMAX_SPAWNED_MONSTER()) break;
				
				AI Enemy = spawn.get();
				if(Enemy == null) break;
				AI_LIST.add(Enemy);
			}
			
			
			Iterator<AI> EnemyMoveIter = AI_LIST.iterator();
			while(EnemyMoveIter.hasNext()){
				AI EnemyMove = EnemyMoveIter.next();
				EnemyMove.Move(deltaTime);
				
			}
			
			
			
			synchronized (list) {
				Iterator<Player_Socket> iter = list.iterator();
				
				while(iter.hasNext()){
					Player_Socket player_Socket = iter.next();
					
					Iterator<AI> EnemyIter = AI_LIST.iterator();
					while(EnemyIter.hasNext()){
						AI Enemy = EnemyIter.next();
						player_Socket.getPacketQueue().add(GeneratePacket(Enemy));
						
					}
					
				}
				
			}
			
			if(mainThread.isAlive() == true) break;
			
			try {
				Thread.currentThread().sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			deltaTime = (System.currentTimeMillis() - time) / 1000f;
			time = System.currentTimeMillis();
		}
		System.out.println("Exit AI");
		
	}
	
	public void setPlayer_List(ArrayList<Player_Socket> PlayerList){ this.Player_List = PlayerList; }
	
	private Packet GeneratePacket(AI Enemy){
		Packet packet = new Packet();
		
		packet.Type = Action_Type.ENEMY_UPDATE;
		packet.ID = Enemy.getID();
		packet.x = Enemy.getX();
		packet.y = Enemy.getY();
		
		return packet;
		
	}

}
