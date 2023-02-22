import java.awt.Color;
import java.util.ArrayList;

public class Orb {
	//orb motion
	public Vector2 pos = new Vector2(400f, 100f);
	public Vector2 speed = new Vector2();
	public Vector2 acceleration = new Vector2();
	public float radius = 5; //the radius of the orbs
	public float mass = (float) ((radius*radius)*Math.PI);
	
	public float gravity = 98;
	public static float friction = 1.9f;
	
	//orb appearance
	public Color color = Color.white;
	
	//time stuff
	public static long lastTime = System.nanoTime(); //currentTimeMillis
	public static long currentTime = System.nanoTime();
	public static double passedTime;
	
	//other
	public boolean updating = false;
	
	public static void updateTime() {
		currentTime = System.nanoTime(); //get the current time 
		//in millisecond(1e3) change to nanosecond(1e9) for large amount of orbs
		passedTime = (currentTime - lastTime) / 1e9; //finds how much time has passed
		//System.out.println(passedTime + " " +currentTime +" "+lastTime);
		lastTime = currentTime; //set the past time for the next update
		//System.out.println(passedTime);
	}
	
	public void physicsUpdate(int id) {
		acceleration.x = 0;
		acceleration.y = 0;
		applyGravity();
		updatePosition();
		applyCollision(id);
		//applyConstraints();
	}
	
	public void applyGravity() {
		//downward gravity
//		if(y <= GamePanel.screenHeight - radius)
//		{
//			acceleration.y -= 98;
//		}
		
		//central gravity
		Vector2 distanceV = new Vector2();
		distanceV.x = pos.x-GamePanel.screenWidth/2;
		distanceV.y = pos.y-GamePanel.screenHeight/2;
		float distance = (float) Math.hypot(distanceV.x, distanceV.y);
		//System.out.println(pos.x+" and "+distance);
		if(distance > radius) {
			acceleration.x = (distanceV.x/distance)*gravity;
			acceleration.y = (distanceV.y/distance)*gravity;
		}
	}
	
	public void updatePosition() {
		speed.x += acceleration.x * passedTime;
		speed.y += acceleration.y * passedTime;
		//System.out.print("");
		
		pos.x -= speed.x * passedTime;
		pos.y -= speed.y * passedTime;
		//System.out.print("");
	}
	
	public void applyConstraints() {
		if(pos.y <= radius) //top constraint
		{
			pos.y = radius;
			if(speed.y > 0) {
				speed.y = -speed.y*0.3f;
			}
		}
		if(pos.y >= GamePanel.screenHeight - radius) //bottom constraint
		{
			pos.y = GamePanel.screenHeight - radius;
			if(speed.y < 0) {
				speed.y = -speed.y*0.3f;
			}
		}
		if(pos.x <= radius) //top constraint
		{
			pos.x = radius;
			if(speed.x > 0) {
				speed.x = -speed.x*0.3f;
			}
		}
		if(pos.x >= GamePanel.screenWidth - radius) //bottom constraint
		{
			pos.x = GamePanel.screenWidth - radius;
			if(speed.x < 0) {
				speed.x = -speed.x*0.3f;
			}
		}
	}
	
	public void applyCollision(int id) {
		if(GamePanel.orbs.size() >= 2) {
			for(int i = id+1; i < GamePanel.orbs.size(); i++) {
				Orb target = GamePanel.orbs.get(i);
				Vector2 vecteur = new Vector2(); 
				float distance = vecteur.distance(pos, target.pos);
				//System.out.println(pos.x+" : "+target.pos.x+" : "+distance);
				if(Math.abs(distance) < target.radius + radius) {
					//position based collision
					float totRadius = radius+target.radius;
					float totOverlap = (float) (totRadius-distance);
					float overlapRatio = totOverlap/totRadius;
					
					Vector2 overlap = new Vector2((pos.x-target.pos.x)*overlapRatio, (pos.y-target.pos.y)*overlapRatio);
					
					pos.x += (radius/totRadius)*overlap.x;
					target.pos.x -= (target.radius/totRadius)*overlap.x;
					pos.y += (radius/totRadius)*overlap.y;
					target.pos.y -= (target.radius/totRadius)*overlap.y;
					
					//modify the speed
					/*Vector2 newSpeed = new Vector2((speed.x+target.speed.x)/2, (speed.y+target.speed.y)/2);
					
					speed.x = newSpeed.x;
					target.speed.x = newSpeed.x;
					speed.y = newSpeed.y;
					target.speed.y = newSpeed.y;*/
					Vector2 normal = new Vector2(target.pos.x-pos.x, target.pos.y-pos.y);
					float magnitude = normal.magnitude(normal);
					Vector2 unitNormal = normal.unitVector(normal, magnitude);
					Vector2 unitTangent = new Vector2(-unitNormal.y, unitNormal.x);
					
					//Velocity Normal & Velocity Tangent
					float v1N = unitNormal.dotProduct(unitNormal, speed);
					float v1T = unitNormal.dotProduct(unitTangent, speed);
					float v2N = unitNormal.dotProduct(unitNormal, target.speed);
					float v2T = unitNormal.dotProduct(unitTangent, target.speed);
					
					//Prime velocity tangent
					float pV1T = v1T;
					float pV2T = v2T;
					
					//Prime velocity normal
					float pV1N = (v1N * (mass - target.mass) + friction * target.mass * v2N) / (mass + target.mass);
					float pV2N = (v2N * (target.mass - mass) + friction * mass * v1N) / (target.mass + mass);
					
					//new speed vectors
					Vector2 v_pV1N = unitNormal.multiply(unitNormal, pV1N);
					Vector2 v_pV1T = unitTangent.multiply(unitTangent, pV1T);
					Vector2 v_pV2N = unitNormal.multiply(unitNormal, pV2N);
					Vector2 v_pV2T = unitTangent.multiply(unitTangent, pV2T);
					
					//apply new speed
					speed.x = v_pV1N.x + v_pV1T.x;
					speed.y = v_pV1N.y + v_pV1T.y;
					target.speed.x = v_pV2N.x + v_pV2T.x;
					target.speed.y = v_pV2N.y + v_pV2T.y;
					
				}
			}
		}
		
	}
	
	
	
}
