import java.awt.Color;
import java.util.ArrayList;

public class Orb {
	//orb motion
	public float x = 400f;
	public float y = 100f;
	public float speedX;
	public float speedY;
	public float accelerationX;
	public float accelerationY;
	public float radius = 10; //the radius of the orbs
	public float mass = (float) ((radius*radius)*Math.PI);
	
	public float gravity = 98;
	
	//orb appearance
	public Color color = Color.white;
	
	//time stuff
	public static long lastTime = System.nanoTime();
	public static long currentTime = System.nanoTime();
	public static double passedTime;
	
	//other
	public boolean updating = false;
	
	public static void updateTime() {
		currentTime = System.nanoTime(); //get the current time
		passedTime = (currentTime - lastTime) / 1e9; //finds how much time has passed
		lastTime = currentTime; //set the past time for the next update
		//System.out.println(passedTime);
	}
	
	public void physicsUpdate(int id) {
		accelerationX = 0;
		accelerationY = 0;
		applyGravity();
		updatePosition();
		applyCollision(id);
		//applyConstraints();
	}
	
	public void applyGravity() {
		//downward gravity
//		if(y <= GamePanel.screenHeight - radius)
//		{
//			accelerationY -= 98;
//		}
		
		//central gravity
		float distanceX = x-GamePanel.screenWidth/2;
		float distanceY = y-GamePanel.screenHeight/2;
		float distance = (float) Math.hypot(distanceX, distanceY);
		
		if(distance > radius) {
			accelerationX = (distanceX/distance)*gravity;
			accelerationY = (distanceY/distance)*gravity;
		}
	}
	
	public void updatePosition() {
		speedX += accelerationX * passedTime;
		speedY += accelerationY * passedTime;
		//System.out.print("");
		
		x -= speedX * passedTime;
		y -= speedY * passedTime;
		//System.out.print("");
	}
	
	public void applyConstraints() {
		if(y <= radius) //top constraint
		{
			y = radius;
			if(speedY > 0) {
				speedY = -speedY*0.3f;
			}
		}
		if(y >= GamePanel.screenHeight - radius) //bottom constraint
		{
			y = GamePanel.screenHeight - radius;
			if(speedY < 0) {
				speedY = -speedY*0.3f;
			}
		}
		if(x <= radius) //top constraint
		{
			x = radius;
			if(speedX > 0) {
				speedX = -speedX*0.3f;
			}
		}
		if(x >= GamePanel.screenWidth - radius) //bottom constraint
		{
			x = GamePanel.screenWidth - radius;
			if(speedX < 0) {
				speedX = -speedX*0.3f;
			}
		}
	}
	
	public void applyCollision(int id) {
		if(GamePanel.orbs.size() >= 2) {
			for(int i = id+1; i < GamePanel.orbs.size(); i++) {
				Orb target = GamePanel.orbs.get(i);
				if(Math.abs(Math.hypot(x-target.x, y-target.y)) < target.radius + radius) {
					//position based collision
					float totRadius = radius+target.radius;
					float totOverlap = (float) (totRadius-Math.hypot(x-target.x, y-target.y));
					float overlapRatio = totOverlap/totRadius;
					
					float overlapX = (x-target.x)*overlapRatio;
					float overlapY = (y-target.y)*overlapRatio;
					
					x += (radius/totRadius)*overlapX;
					target.x -= (target.radius/totRadius)*overlapX;
					y += (radius/totRadius)*overlapY;
					target.y -= (target.radius/totRadius)*overlapY;
					
					//modify the speed
					float newSpeedX = (speedX+target.speedX)/2;
					float newSpeedY = (speedY+target.speedY)/2;
					
					speedX = newSpeedX;
					target.speedX = newSpeedX;
					speedY = newSpeedY;
					target.speedY = newSpeedY;
//					float normalX = target.x-x;
//					float normalY = target.y-y;
//					
//					float magnitude = (float) Math.sqrt((normalX*normalX)+(normalY*normalY));
//					float unitNormalX = normalX / magnitude;
//					float unitNormalY = normalY / magnitude;
					
				}
			}
		}
		
	}
	
	
	
}
