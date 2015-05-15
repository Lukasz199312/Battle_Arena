package window;

import java.awt.*;

import javax.swing.*;

import server.Server;

class MainFrame extends JFrame{
	private static final String Title = "Battle Arena - Server v 1.0";
	private boolean StartServer = false;
	private boolean StartGame = false;
	
	public MainFrame() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		
		setSize(screenSize.width / 3, screenSize.height / 2 );
		setTitle(Title);
		setLocationByPlatform(true);
		setResizable(false);
		
	}
	
}

public class main {

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				MainFrame frame = new MainFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				
			}
		});
		
		Server server = new Server("localhost", 6002, Thread.currentThread());
		
		new Thread(server).start();

	}

}
