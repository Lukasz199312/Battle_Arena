package server.gameobject;

import java.net.Socket;

public class CreateConnection {

	private static int  Head_ID = 0;
	
	public static Player_Socket New(Socket socket){
		Player_Socket Player = new Player_Socket(socket, Head_ID);
		Head_ID++;
		return Player;
	}
	
	public int getHead(){ return Head_ID; }
}
