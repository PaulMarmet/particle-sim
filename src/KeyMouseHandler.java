import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class KeyMouseHandler implements KeyListener,  MouseListener{

	//public boolean shiftPressed = false;
	public Orb orbed;
	
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
			SimSettings.mainPanel(SimSettings.storedWindow);
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

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {} //left click
		else if(e.getButton() == MouseEvent.BUTTON2) {} //middle click
		//else if(e.getButton() == MouseEvent.BUTTON3) //right click
		//{
		//	GamePanel.newOrb((float) e.getPoint().x, (float) e.getPoint().y);
		//}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) //middle click
		{
			Vector2 mouse = new Vector2(e.getPoint().x, e.getPoint().y);
			for(int i = 0; i < GamePanel.orbs.size(); i++)
			{
				if(Vector2.distance(mouse, GamePanel.orbs.get(i).pos) <= GamePanel.orbs.get(i).radius)
				{
					if(orbed == null)
					{
						orbed = GamePanel.orbs.get(i);
					}
					else if(orbed != GamePanel.orbs.get(i))
					{
						new Link(orbed, GamePanel.orbs.get(i), 1);
						orbed = null;
					}
					break;
				}
			}
		}
		else if(e.getButton() == MouseEvent.BUTTON2) //middle click
		{
			Vector2 mouse = new Vector2(e.getPoint().x, e.getPoint().y);
			for(int i = 0; i < GamePanel.orbs.size(); i++)
			{
				if(Vector2.distance(mouse, GamePanel.orbs.get(i).pos) <= GamePanel.orbs.get(i).radius)
				{
					GamePanel.orbs.get(i).updatePos = false;
					//System.out.println("You just got orbed B)");
					GamePanel.orbs.get(i).color = Color.white;
					break;
				}
			}
		}
		else if(e.getButton() == MouseEvent.BUTTON3) //right click
		{
			GamePanel.newOrb((float) e.getPoint().x, (float) e.getPoint().y);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}

}
