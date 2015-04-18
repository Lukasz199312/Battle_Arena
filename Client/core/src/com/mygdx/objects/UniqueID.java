package com.mygdx.objects;

public class UniqueID {
	private static int  Head_ID = 0;
	
	public static int add(){
		Head_ID++;
		return Head_ID -= 1;
	}
	
	public int getHead(){ return Head_ID; }
}
