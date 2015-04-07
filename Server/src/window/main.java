package window;

import java.awt.*;
import javax.swing.*;

class MainFrame extends JFrame{
	private static final int DEFAULT_WIDTH = 350;
	private static final int DEFAULT_HEIGHT = 400;
	private static final String Title = "Battle Arena - Server v 1.0";
	
	public MainFrame() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setTitle(Title);
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

	}

}
