import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

	//public boolean shiftPressed = false;
	
	@Override
	public void keyTyped(KeyEvent e) {
//		int code = e.getKeyCode();
//		
//		if(code == KeyEvent.VK_SHIFT) {
//			GamePanel.newOrb();
//		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_SPACE) {
			GamePanel.newOrb();
		}
		if(code == KeyEvent.VK_BACK_SPACE) {
			GamePanel.resetOrb();
		}
		if(code == KeyEvent.VK_ESCAPE) {
			SimSettings.panel(SimSettings.storedWindow);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
//		int code = e.getKeyCode();
//		
//		if(code == KeyEvent.VK_SHIFT) {
//			shiftPressed = false;
//			System.out.println("n");
//		}
	}

}
