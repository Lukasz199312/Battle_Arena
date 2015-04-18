package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.mygdx.objects.GameObject;
import com.mygdx.objects.enemy.Enemy;
import com.mygdx.objects.player.Player;

public class CollisionDetection {
	public static void Check(ArrayList<GameObject> GameObjectList){
	    for(int i = 0; i < GameObjectList.size(); i++ ){
	    	for(int j = 0; j < GameObjectList.size(); j++){
	    		if(i == j){
	    			j++;
	    			if(j >= GameObjectList.size()) continue;
	    		}
	    		if(GameObjectList.get(i).getBounds().overlaps(GameObjectList.get(j).getBounds()) == true) {
	    			float tmpX = GameObjectList.get(i).getX();
	    			float tmpY = GameObjectList.get(i).getY();
	    			
	    			GameObjectList.get(i).setOldY();
	    			if(GameObjectList.get(i).getBounds().overlaps(GameObjectList.get(j).getBounds()) == true){
	    				GameObjectList.get(i).setY(tmpY);
	    				GameObjectList.get(i).setOldX();
	    				
	    				Gdx.app.log("kolizja", "X");
	    				continue;

	    			}
	    			Gdx.app.log("kolizja", "Y");
	    			
	    		}
	    	}
	    }
	}
	
	public static void Check(Player player, ArrayList<GameObject> GameObjectList){
	    for(int i = 0; i < GameObjectList.size(); i++ ){
	    		if(player.getBounds().overlaps(GameObjectList.get(i).getBounds()) == true) {
	    			float tmpX = player.getX();
	    			float tmpY = player.getY();
	    			
	    			player.setOldY();
	    			if(player.getBounds().overlaps(GameObjectList.get(i).getBounds()) == true){
	    				player.setY(tmpY);
	    				player.setOldX();
	    				
	    				Gdx.app.log("kolizja", "X");
	    				continue;

	    			}
	    			Gdx.app.log("kolizja", "Y");
	    			
	    		}
	    }
	}
	

}
