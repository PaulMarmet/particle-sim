import javax.swing.JFrame;

public class Main {
	public static void main(String args[]) {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("PhysOrbs");
		
		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		//ask for settings
		Config.main(args);
		SimSettings.mainPanel(window);
//		Orb.bounds = false;
//		Integer bounds = JOptionPane.showConfirmDialog(window, "Can orbs go off screen?\nNot recommended with downwards gravity", "Bounds", JOptionPane.YES_NO_OPTION);
//		if(bounds != null && bounds == 1) {
//			Orb.bounds = true;
//		}
//		Orb.gravDir = false;
//		Integer gravDir = JOptionPane.showConfirmDialog(window, "Where is gravity, center of screen or downwards", "Bounds", JOptionPane.YES_NO_OPTION);
//		if(gravDir != null && gravDir == 1) {
//			Orb.gravDir = true;
//		}
		
		gamePanel.startGameThread();
	}
}
