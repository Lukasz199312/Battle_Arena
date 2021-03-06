package server.gameobject;

import java.net.Socket;

public class CreateConnection {

	private static int  Head_ID = 0;
	
	public static Player_Socket New(Socket socket, Thread thread){
		Player_Socket Player = new Player_Socket(socket, Head_ID, thread);
		Head_ID++;
		return Player;
	}
	
	public static int addUniqueID(){
		Head_ID++;
		return Head_ID -= 1;
	}
	
	public int getHead(){ return Head_ID; }
}
