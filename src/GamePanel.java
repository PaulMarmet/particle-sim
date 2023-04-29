import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//SCREEN SETTINGS
	static int screenWidth = 800;
	static int screenHeight = 600;
	
	
	static int r = 100;
	static int g = 40;
	static int b = 40;
	static int offset = 0;
	static boolean offsetDir = true;
	static Random rand = new Random();
	
	static long lastOrb = System.nanoTime();
	
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	
	static ArrayList<Orb> orbs = new ArrayList<Orb>();
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		//this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		while(gameThread != null) {
			//System.out.println("game be runnin");
			//Update stuff
			update();
			
			//render/draw stuff
			repaint();
		}
		
	}
	public void update() {
		Orb.updateTime();
		Rectangle bounds = this.getBounds();
		screenWidth = bounds.width;
		screenHeight = bounds.height;
		if(orbs.size() > 0) {
			for(int i = 0; i<orbs.size(); i++) {
				//if(orbs.get(i).updating == false) {
					//orbs.get(i).updating = true;
					orbs.get(i).physicsUpdate(i);
					//orbs.get(i).updating = false;
				//}
			}
		}
		if(1/Orb.passedTime >= 60 && Orb.currentTime >= lastOrb + 1e8) { // && Orb.currentTime >= lastOrb + 5e8
			//newOrb();
			lastOrb = Orb.currentTime;
		}
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		if(orbs.size() > 0) {
			for(int i = 0; i<orbs.size(); i++) {
				g2.setColor(orbs.get(i).color);
				g2.fillOval((Math.round(orbs.get(i).pos.x) - Math.round(orbs.get(i).radius)), (Math.round(orbs.get(i).pos.y) - Math.round(orbs.get(i).radius)), Math.round(orbs.get(i).radius)*2, Math.round(orbs.get(i).radius)*2);
			}
		}
		
		g2.setColor(Color.white);
		g2.drawString("FPS: "+1/Orb.passedTime, 10, 10);
		g2.drawString("Orbs: "+orbs.size(), 10, 20);
		g2.dispose();
	}
	
	public static void newOrb() {
		orbs.add(new Orb(new Vector2(
				rand.nextFloat() * screenWidth, 
				rand.nextFloat() * screenHeight), 
				(float) (Config.getF("DEFAULT_ORB_RADIUS") + ((rand.nextFloat() - 0.5) * 2 * Config.getF("RADIUS_RANGE"))),
				new Color((float) r/100, (float) g/100, (float) b/100)));
		
		//orbs.get(orbs.size()-1).speed.x = -100;
		//orbs.get(orbs.size()-1).pos.x = screenWidth/2 - 50;
		//orbs.get(orbs.size()-1).pos.x = (screenWidth/2 + offset);
		//updateOffset();
		updateColor();
	}
	
	public static void updateOffset() {
		if(offset == -100 || offset < 100 && offsetDir == true) {
			offsetDir = true;
			offset += 2;
		} else if(offset == 100 || offset > -100 && offsetDir == false) {
			offsetDir = false;
			offset -= 2;
		}
	}
	
	public static void updateColor() {
		if(r == 100 || (g != 100 && b == 40)) {
			r -= 2;
			g += 2;
		} else if(g == 100 || (b != 100 && r == 40)) {
			g -= 2;
			b += 2;
		} else if(b == 100 || (r != 100 && g == 40)) {
			b -= 2;
			r += 2;
		}	
	}
	
	public static void resetOrb() {
		orbs.clear();
		r = 100;
		g = 40;
		b = 40;
	}
}
